package exchangebook;
import exchangebook.Book;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

//Map this class to /ds route
@Path("/ds")
public class DatastoreResource {
  // Allows to insert contextual objects into the class,
  // e.g. ServletContext, Request, Response, UriInfo
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  // Return the list of entities to the user in the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public List<Book> getEntitiesBrowser() {
    //datastore dump -- only do this if there are a small # of entities
    return getBookList();
  }
  
  // Return the list of entities to applications 
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Book> getEntities() {
    //datastore dump -- only do this if there are a small # of entities
    //same code as above method
    return getBookList();
  }
  
  @POST
  @Produces(MediaType.TEXT_HTML)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void newBook(@FormParam("isbn") String isbn,
	  	@FormParam("author") String author,
  		@FormParam("title") String title,
		@FormParam("action") String action,
		@FormParam("quantity") int quantity, 
		@Context HttpServletResponse servletResponse)throws IOException{
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	int offerNum,demandNum;
	if(action.equals("offer")){
		offerNum=quantity; demandNum=0;
	}
	else{
		offerNum=0;demandNum=quantity;
	}
	Entity inputData;
	Key bookKey=KeyFactory.createKey("Book",isbn);
	try{
		inputData=datastore.get(bookKey);
		inputData.setProperty("offerNum",(long)inputData.getProperty("offerNum")+offerNum);
		inputData.setProperty("demandNum",(long)inputData.getProperty("demandNum")+demandNum);
	}
	catch(EntityNotFoundException e){
		inputData=new Entity("Book", isbn);
		inputData.setProperty("author",author);
		inputData.setProperty("title",title);
		inputData.setProperty("offerNum",offerNum);
		inputData.setProperty("demandNum",demandNum);
	}
	datastore.put(inputData);
	syncCache.put(isbn, inputData);
	
  }

  
  //The @PathParam annotation says that keyname can be inserted as parameter after this class's route /ds
  @Path("{isbn}")
  public BookResource getEntity(@PathParam("isbn") String isbn) {
    System.out.println("GETting Book Info for " +isbn);
    return new BookResource(uriInfo, request, isbn);
  }
  
  private List<Book> getBookList(){
	  List<Book> list=new ArrayList<Book>();
	  DatastoreService datastore= DatastoreServiceFactory.getDatastoreService();
	  Query display_all=new Query("Book");
	  List<Entity> results=datastore.prepare(display_all).asList(FetchOptions.Builder.withLimit(20));
	  for(Entity temp:results){
		  String isbn=temp.getKey().getName();
		  String author=(String)temp.getProperty("author");
		  String title=(String)temp.getProperty("title");
		  int offerNum=(int)temp.getProperty("offerNum");
		  int demandNum=(int)temp.getProperty("demandNum");
		  Book b=new Book(title,author,isbn,offerNum,demandNum);
		  list.add(b);
	  }
	  return list;
  }
  
}