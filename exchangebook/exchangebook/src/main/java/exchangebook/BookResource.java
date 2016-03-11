package exchangebook;
import exchangebook.Book;
import exchangebook.OwnerInfoForBook;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
// import com.google.appengine.api.users.User;
// import com.google.appengine.api.users.UserService;
// import com.google.appengine.api.users.UserServiceFactory;
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
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    // UserService userService = UserServiceFactory.getUserService();
 //    User user = userService.getCurrentUser();
 //    if (user != null) {
 //      System.out.println(user.getUserId());
 //    }
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
      offerList.add(new OwnerInfoForBook((String)temp.getProperty("userID"), (String)owner.getProperty("name"),
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


    //     @PUT
    //     @Consumes(MediaType.APPLICATION_XML)
    //     public Response putTaskData(String title) {
    //       Response res = null;
    // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    //       MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    //       syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    // Key bookKey=KeyFactory.createKey("Book",this.isbn);
    // Entity inputData;
    // try{
    //   inputData=datastore.get(bookKey);
    //   inputData.setProperty("title",title);
    //   //res.getWriter().println("Entity Updated");
    //   res = Response.noContent().build();
    //
    // }catch(EntityNotFoundException e){
    //   inputData=new Entity("Book",this.isbn);
    //   inputData.setProperty("title",title);
    //   //res.getWriter().println("Entity Created");
    //   res = Response.created(uriInfo.getAbsolutePath()).build();
    // }
    // datastore.put(inputData);
    // syncCache.put(this.isbn, inputData);
    //
    //       return res;
    //     }
	
	//     @PUT
	//     @Consumes(MediaType.APPLICATION_XML)
	// public Response putBook(String title, String author, long peopleOffer, long peopleDemand){
	// 	System.out.println("3 GETting Book Info for " +isbn);
	// 	Response res=null;
	// 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	//     MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	// 	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	// 	Key bookKey=KeyFactory.createKey("Book",this.isbn);
	// 	Entity inputData;
	// 	try{
	// 		inputData=datastore.get(bookKey);
	// 		inputData.setProperty("peopleOffer",(long)inputData.getProperty("peopleOffer")+peopleOffer);
	// 		inputData.setProperty("peopleDemand",(long)inputData.getProperty("peopleDemand")+peopleDemand);
	// 		res=Response.noContent().build();
	// 	}
	// 	catch(EntityNotFoundException e){
	// 		inputData=new Entity("Book", this.isbn);
	// 		inputData.setProperty("author",author);
	// 		inputData.setProperty("title",title);
	// 		inputData.setProperty("peopleOffer",peopleOffer);
	// 		inputData.setProperty("peopleDemand",peopleDemand);
	// 		res = Response.created(uriInfo.getAbsolutePath()).build();
	// 	}
	// 	datastore.put(inputData);
	// 	syncCache.put(this.isbn, inputData);
	// 	return res;
	// }
	
	// @DELETE
	// public void deleteBook(long peopleOffer, long peopleDemand){
	// 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	//     MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	// 	Key bookKey=KeyFactory.createKey("Book",this.isbn);
	// 	Entity target;
	// 	try{
	// 		target=datastore.get(bookKey);
	// 	}
	// 	catch(EntityNotFoundException e){
	// 		throw new RuntimeException("Delete: Book with isbn" + this.isbn+" not found");
	// 	}
	// 	long offer=(long)target.getProperty("peopleOffer")-peopleOffer;
	// 	long demand=(long)target.getProperty("peopleDemand")-peopleDemand;
	// 	if(offer<=0&&demand<=0){
	// 		datastore.delete(bookKey);
	// 		if(syncCache.contains(bookKey)){
	// 			syncCache.delete(bookKey);
	// 		}
	// 	}
	// 	else{
	// 		target.setProperty("peopleOffer",offer);
	// 		target.setProperty("peopleDemand",demand);
	// 		datastore.put(target);
	// 		syncCache.put(this.isbn, target);
	// 	}
	//
	// }
}





