package exchangebook;
import exchangebook.Book;
import exchangebook.Owner;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

//Map this class to /ds route
@Path("/")
public class DatastoreResource {
  // Allows to insert contextual objects into the class,
  // e.g. ServletContext, Request, Response, UriInfo
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  
  //This if for adding books
  @POST
  @Produces(MediaType.TEXT_HTML)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void newBook(@FormParam("isbn") String isbn,
	  @FormParam("author") String author,
  	@FormParam("title") String title,
		@FormParam("option") String option,
		@FormParam("quantity") int quantity, 
		@FormParam("user") String userID,
		@Context HttpServletResponse servletResponse)throws IOException {
      
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  
    //Add the book into Book if there's no such book in the datastore before
  	Entity bookData;
  	Key bookKey = KeyFactory.createKey("Book",isbn);
    if (!syncCache.contains(bookKey)) {
      try {
        bookData = datastore.get(bookKey);
      }
      catch(EntityNotFoundException e){
        bookData = new Entity("Book", isbn);
      	bookData.setProperty("title",title);
      	bookData.setProperty("author",author);
      	datastore.put(bookData);
      	syncCache.put(bookKey, bookData);
      }
    } 
  
  	if(option.equals("offer")) {
      Query.Filter f_user = new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userID);
      Query.Filter f_book = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
      Query.Filter f_offer = Query.CompositeFilterOperator.and(f_user, f_book);
      Query q = new Query("Offer").setFilter(f_offer);
    
      //Though defined as list, should return no more than one entity
      List<Entity> offer = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    
      //update quantity if the owner has offered the book before, or add new entity if not
      if(!offer.isEmpty()) {
        Entity temp = offer.get(0);
        temp.setProperty("num", (long)temp.getProperty("num") + quantity);
      } else {
        Entity temp = new Entity("Offer");
        temp.setProperty("userID", userID);
        temp.setProperty("isbn", isbn);
        temp.setProperty("num", quantity);
        datastore.put(temp);
      }
  	} else {
      Query.Filter f_user = new Query.FilterPredicate("userID", Query.FilterOperator.EQUAL, userID);
      Query.Filter f_book = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
      Query.Filter f_demand = Query.CompositeFilterOperator.and(f_user, f_book);
      Query q = new Query("Demand").setFilter(f_demand);
    
      //Though defined as list, should return no more than one entity
      List<Entity> demand = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    
      //update quantity if the owner has demanded the book before, or add new entity if not
      if(!demand.isEmpty()) {
        Entity temp = demand.get(0);
        temp.setProperty("num", (long)temp.getProperty("num") + quantity);
      } else {
        Entity temp = new Entity("Demand");
        temp.setProperty("userID", userID);
        temp.setProperty("isbn", isbn);
        temp.setProperty("num", quantity);
        datastore.put(temp);
      }
  	}
  }
  
  //The @PathParam annotation says that keyname can be inserted as parameter after this class's route /ds
  @Path("book/{isbn}")
  public BookResource getBookEntity(@PathParam("isbn") String isbn) {
    return new BookResource(uriInfo, request, isbn);
  }
  
  @Path("owner/{userID}")
  public OwnerResource getOwnerEntity(@PathParam("userID") String userID){
	  return new OwnerResource(uriInfo, request, userID);
  }
  
  @Path("message/{userID}")
  public MessageResource getMessage(@PathParam("userID") String userID){
    return new MessageResource(uriInfo, request, userID);
  }
  
  
}