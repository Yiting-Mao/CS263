package exchangebook;
import exchangebook.Book;
import exchangebook.OwnerInfoForBook;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import javax.xml.bind.JAXBElement;

public class BookResource{
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String isbn;
	
	public BookResource(UriInfo uriInfo, Request request, String isbn){
		this.uriInfo=uriInfo;
		this.request=request;
		this.isbn=isbn;
	}
	
  private Book getBook(){
    System.out.println("*******************");
    System.out.println("Getting Book");
    System.out.println("*******************");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
     //get book info
    Key bookKey = KeyFactory.createKey("Book", this.isbn);
    Entity result;
    if (syncCache.contains(bookKey)) {
      result = (Entity)syncCache.get(bookKey);
    } else {
      try{
        result = datastore.get(bookKey);
        syncCache.put(bookKey, result);
      } catch (EntityNotFoundException e) {
        throw new RuntimeException("Get: Book with isbn " + this.isbn+" not found");
      }
    }
    
    //Get all owner info with the number of this book they offer
    List<OwnerInfoForBook> offerList = new ArrayList<OwnerInfoForBook>();
    Query.Filter f_book = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
    Query q = new Query("Offer").setFilter(f_book);
    List<Entity> offer = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp : offer) {
      Key ownerKey = KeyFactory.createKey("Owner", (String)temp.getProperty("userID"));
      
      //get user's personal info
      Entity owner;
      if (syncCache.contains(ownerKey)) {
        owner = (Entity)syncCache.get(ownerKey);
      } else {
        try {
          owner = datastore.get(ownerKey);
          syncCache.put(ownerKey, owner);
        } catch (EntityNotFoundException e) {
          System.out.println("Can't find info for owner of this book");
          owner = new Entity ("Owner");
          owner.setProperty("name", "");
          owner.setProperty("location", "");
        }
      }
      offerList.add(new OwnerInfoForBook((String)temp.getProperty("userID"), (String)owner.getProperty("name"),
         (String)owner.getProperty("location"), (long)temp.getProperty("num")));
    }
    
    //do the same with demand
    List<OwnerInfoForBook> demandList = new ArrayList<OwnerInfoForBook>();
    f_book = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
    q = new Query("Demand").setFilter(f_book);
    List<Entity> demand = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp : demand) {
      Key ownerKey = KeyFactory.createKey("Owner", (String)temp.getProperty("userID"));
      
      //get user's personal info
      Entity owner;
      if (syncCache.contains(ownerKey)) {
        owner = (Entity)syncCache.get(ownerKey);
      } else {
        try {
          owner = datastore.get(ownerKey);
          syncCache.put(ownerKey, owner);
        } catch (EntityNotFoundException e) {
          System.out.println("Can't find info for owner of this book");
          owner = new Entity ("Owner");
          owner.setProperty("name", "");
          owner.setProperty("location", "");
        }
      }
      demandList.add(new OwnerInfoForBook((String)temp.getProperty("userID"), (String)owner.getProperty("name"),
         (String)owner.getProperty("location"), (long)temp.getProperty("num")));
    }
    return new Book((String)result.getProperty("title"), (String)result.getProperty("author"), 
      isbn, offerList, demandList);
  }
	
    //for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public Book getBookHTML() {
    return getBook();

  }

    //for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Book getBookData() {
    //same code as above method
  return getBook();
  }
}





