package exchangebook;
import exchangebook.Book;
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
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		Key bookKey=KeyFactory.createKey("Book",this.isbn);
		Entity result;
		
		try{
			result=datastore.get(bookKey);
			return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
				this.isbn,(long)result.getProperty("offerNum"),
					(long)result.getProperty("demandNum"));
		}
		catch(EntityNotFoundException e){
			throw new RuntimeException("Get: Book with isbn" + this.isbn+" not found");
		}
		
		// if(syncCache.contains(this.isbn)){
	// 		result=(Entity)syncCache.get(this.isbn);
	// 		return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
	// 			this.isbn,(long)result.getProperty("offerNum"),
	// 				(long)result.getProperty("demandNum"));
	// 	}
	// 	else{
	// 		try{
	// 			result=datastore.get(bookKey);
	// 			return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
	// 				this.isbn,(long)result.getProperty("offerNum"),
	// 					(long)result.getProperty("demandNum"));
	// 		}
	// 		catch(EntityNotFoundException e){
	// 			throw new RuntimeException("Get: Book with isbn" + this.isbn+" not found");
	// 		}
	// 	}
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
	
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response putTaskData(String title) {
      Response res = null;
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
      syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	Key bookKey=KeyFactory.createKey("Book",this.isbn);
  	Entity inputData;
  	try{
  		inputData=datastore.get(bookKey);
  		inputData.setProperty("title",title);
  		//res.getWriter().println("Entity Updated");
  		res = Response.noContent().build();
		
  	}catch(EntityNotFoundException e){ 
  		inputData=new Entity("Book",this.isbn);
  		inputData.setProperty("title",title);
  		//res.getWriter().println("Entity Created");
  		res = Response.created(uriInfo.getAbsolutePath()).build();	
  	}
  	datastore.put(inputData);
  	syncCache.put(this.isbn, inputData);

      return res;
    }
	
	//     @PUT
	//     @Consumes(MediaType.APPLICATION_XML)
	// public Response putBook(String title, String author, long offerNum, long demandNum){
	// 	System.out.println("3 GETting Book Info for " +isbn);
	// 	Response res=null;
	// 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	//     MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	// 	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	// 	Key bookKey=KeyFactory.createKey("Book",this.isbn);
	// 	Entity inputData;
	// 	try{
	// 		inputData=datastore.get(bookKey);
	// 		inputData.setProperty("offerNum",(long)inputData.getProperty("offerNum")+offerNum);
	// 		inputData.setProperty("demandNum",(long)inputData.getProperty("demandNum")+demandNum);
	// 		res=Response.noContent().build();
	// 	}
	// 	catch(EntityNotFoundException e){
	// 		inputData=new Entity("Book", this.isbn);
	// 		inputData.setProperty("author",author);
	// 		inputData.setProperty("title",title);
	// 		inputData.setProperty("offerNum",offerNum);
	// 		inputData.setProperty("demandNum",demandNum);
	// 		res = Response.created(uriInfo.getAbsolutePath()).build();
	// 	}
	// 	datastore.put(inputData);
	// 	syncCache.put(this.isbn, inputData);
	// 	return res;
	// }
	
	// @DELETE
	// public void deleteBook(long offerNum, long demandNum){
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
	// 	long offer=(long)target.getProperty("offerNum")-offerNum;
	// 	long demand=(long)target.getProperty("demandNum")-demandNum;
	// 	if(offer<=0&&demand<=0){
	// 		datastore.delete(bookKey);
	// 		if(syncCache.contains(bookKey)){
	// 			syncCache.delete(bookKey);
	// 		}
	// 	}
	// 	else{
	// 		target.setProperty("offerNum",offer);
	// 		target.setProperty("demandNum",demand);
	// 		datastore.put(target);
	// 		syncCache.put(this.isbn, target);
	// 	}
	//
	// }
}





