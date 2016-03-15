package exchangebook;
import exchangebook.BookInfoForOwner;
import exchangebook.Owner;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

import javax.xml.bind.JAXBElement;

public class OwnerResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String userID;
	
	public OwnerResource(UriInfo uriInfo, Request request, String userID) {
		this.uriInfo=uriInfo;
		this.request=request;
		this.userID=userID;
	}
	
  private Owner getOwner() {
    System.out.println("*******************");
    System.out.println("Getting Owner");
    System.out.println("*******************");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    
     //get owner info
    Key ownerKey = KeyFactory.createKey("Owner", this.userID);
    Entity result;
    if (syncCache.contains(ownerKey)) {
      result = (Entity)syncCache.get(ownerKey);
    } else {
      try{
        result = datastore.get(ownerKey);
        syncCache.put(ownerKey, result);
      } catch (EntityNotFoundException e) {
        throw new RuntimeException("Get: Owner with userID " + this.userID + " not found");
      }
    }
    
    //Get all book info with their numbers this owner offer
    List<BookInfoForOwner> offerList = new ArrayList<BookInfoForOwner>();
    Query.Filter f_owner = new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userID);
    Query q = new Query("Offer").setFilter(f_owner);
    List<Entity> offer = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp : offer) {
      Key bookKey = KeyFactory.createKey("Book", (String)temp.getProperty("isbn"));
      
      //get book info
      Entity book;
      if (syncCache.contains(bookKey)) {
        book = (Entity)syncCache.get(bookKey);
      } else {
        try {
          book = datastore.get(bookKey);
          syncCache.put(bookKey, book);
        } catch (EntityNotFoundException e) {
          System.out.println("Can't find detailed info for book");
          book = new Entity ("Book");
          book.setProperty("title", "");
          book.setProperty("author", "");
        }
      }
      offerList.add(new BookInfoForOwner((String)book.getProperty("title"), (String)book.getProperty("author"), 
        (String)temp.getProperty("isbn"), (long)temp.getProperty("num")));
    }
    
    //do the same with demand
    List<BookInfoForOwner> demandList = new ArrayList<BookInfoForOwner>();
    f_owner = new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userID);
    q = new Query("Demand").setFilter(f_owner);
    List<Entity> demand = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp : demand) {
      Key bookKey = KeyFactory.createKey("Book", (String)temp.getProperty("isbn"));
      
      //get book info
      Entity book;
      if (syncCache.contains(bookKey)) {
        book = (Entity)syncCache.get(bookKey);
      } else {
        try {
          book = datastore.get(bookKey);
          syncCache.put(bookKey, book);
        } catch (EntityNotFoundException e) {
          System.out.println("Can't find detailed info for book");
          book = new Entity ("Book");
          book.setProperty("title", "");
          book.setProperty("author", "");
        }
      }
      demandList.add(new BookInfoForOwner((String)book.getProperty("title"), (String)book.getProperty("author"), 
        (String)temp.getProperty("isbn"), (long)temp.getProperty("num")));
    }
    
    return new Owner(userID, (String)result.getProperty("name"), (String)result.getProperty("location"), 
      offerList, demandList);
  }
	
  //for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public Owner getOwnerHTML() {
    return getOwner();
  }


  //for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Owner getOwnerData() {
    //same code as above method
    return getOwner();
  }


  //for the browser, it will show the user's personal info and the books he offers and demands
  // @GET
  // @Produces(MediaType.TEXT_HTML)
  // public void getOwnerBrowser(
  //   @Context HttpServletRequest req, @Context HttpServletResponse resp) throws IOException,ServletException{
  //     req.setAttribute(this.getClass().getName(), this);
  //     req.getRequestDispatcher("/otheraccount.jsp?targetID="+userID).forward(req, resp);
  // }

  //Possibly error when the post doesn't has parameter userID
  @Path("addinfo")
  @POST
  @Produces(MediaType.TEXT_HTML)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void addOwnerInfo(@FormParam("name") String name,
  @FormParam("location") String location,
   @Context HttpServletResponse servletResponse)throws IOException {
     System.out.println("*******************");
     System.out.println("Updating Owner Info");
     System.out.println("*******************");
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	Entity userData;
  	Key userKey=KeyFactory.createKey("Owner",userID);
  	try{
  		userData=datastore.get(userKey);
  	}
  	catch(EntityNotFoundException e){
  		userData=new Entity("Owner",userID);
  	}
  	userData.setProperty("name",name);
  	userData.setProperty("location",location);
  	datastore.put(userData);
    syncCache.put(userKey, userData);
  }
  
  //Check whether the book is being offered or demand by others, if not, delete book info
  private void checkDeleteBookInfo(String isbn){
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    Query.Filter f_book = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
    Query q = new Query("Offer").setFilter(f_book);
    List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    if(list.isEmpty()) {
      q = new Query("Demand").setFilter(f_book);
      list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
      
      if(list.isEmpty()) {
        Key bookKey = KeyFactory.createKey("Book", isbn);
      	try {
      		datastore.delete(bookKey);
      	} catch(Exception e) {
      		System.out.println("Something went wrong when deleting book info");
      	}
      	if(syncCache.contains(bookKey)) {
      		syncCache.delete(bookKey);
      	}
      }    
    }
  }
  
  //delete owner's specific book
  @Path("{isbn}")
  @DELETE
  public void deleteBook(@PathParam("isbn") String isbn,
                         @FormParam("option") String option,
                         @FormParam("num") long num) {
    System.out.println("*******************");
    System.out.println("Deleting Book");
    System.out.println("*******************");
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query.Filter f_user = new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userID);
    Query.Filter f_book = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
    Query.Filter f = Query.CompositeFilterOperator.and(f_user, f_book);
    String choice = option.equals("offer") ? "Offer" : "Demand";
  
    Query q = new Query(choice).setFilter(f);
     //Though defined as list, should return no more than one entity
    List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    //Should always not empty
    if(!list.isEmpty()) {
      Entity temp = list.get(0);
      if((long)temp.getProperty("num") > num) {
        temp.setProperty("num", (long)temp.getProperty("num") - num);
        datastore.put(temp);
      } else {
        datastore.delete(temp.getKey());
        checkDeleteBookInfo(isbn);
      }
    }  
  }
  
  //delete owner info together with the books he/s offers/demands
  @DELETE
  public void deleteOwner() {
    System.out.println("*******************");
    System.out.println("Deleting Owner");
    System.out.println("*******************");
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    
    //delete owner's personal info
    Key ownerKey = KeyFactory.createKey("Owner", userID);
  	try {
  		datastore.delete(ownerKey);
  	} catch(Exception e) {
  		System.out.println("Something went wrong when deleting owner info");
  	}
  	if(syncCache.contains(ownerKey)) {
  		syncCache.delete(ownerKey);
  	}
    
    Query.Filter f_user = new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userID);
    String choice = "Offer";
    Query q = new Query(choice).setFilter(f_user);
     //Though defined as list, should return no more than one entity
    List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp: list) {
      String isbn = (String)temp.getProperty("isbn");
      datastore.delete(temp.getKey());
      checkDeleteBookInfo(isbn);
    }  
    
    choice = "Demand";
    q = new Query(choice).setFilter(f_user);
    list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp: list) {
      String isbn = (String)temp.getProperty("isbn");
      datastore.delete(temp.getKey());
      checkDeleteBookInfo(isbn);
    }    
  }
}





