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
		if(syncCache.contains(this.isbn)){
			result=(Entity)syncCache.get(this.isbn);
			return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
				this.isbn,(int)result.getProperty("offerNum"),
					(int)result.getProperty("demandNum"));
		}
		else{
			try{
				result=datastore.get(bookKey);
				return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
					this.isbn,(int)result.getProperty("offerNum"),
						(int)result.getProperty("demandNum"));
			}
			catch(EntityNotFoundException e){
				throw new RuntimeException("Get: Book with isbn" + this.isbn+" not found");
			}
		}
	}
	
	//for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Book getBookHtml(){
		return getBook();
	}
	
	//for the application
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Book getBookApp(){
		return getBook();
	}
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
	public Response putBook(String title, String author, int offerNum, int demandNum){
		Response res=null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		Key bookKey=KeyFactory.createKey("Book",this.isbn);
		Entity inputData;
		try{
			inputData=datastore.get(bookKey);
			inputData.setProperty("offerNum",(int)inputData.getProperty("offerNum")+offerNum);
			inputData.serProperty("demandNum",(int)inputData.getProperty("demandNum")+demandNum);
			res=Response.noContent().build();
		}
		catch(EntityNotFoundException e){
			inputData=new Entity("Book", this.isbn);
			inputData.setProperty("author",author);
			inputData.setProperty("title",title);
			inputData.setProperty("offerNum",offerNum);
			inputData.setProperty("demandNum",demandNum);
			res = Response.created(uriInfo.getAbsolutePath()).build();	
		}
		datastore.put(inputData);
		syncCache.put(this.isbn, inputData);
		return res;
	}
	
	@DELETE
	public void deleteBook(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		Key bookKey=KeyFactory.createKey("Book",this.isbn);
	}
}





