package exchangebook;
import exchangebook.Book;
import exchangebook.Owner;
import exchangebook.OwnerResource;
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
				this.isbn,(List<String>)result.getProperty("peopleOffer"),
					(List<String>)result.getProperty("peopleDemand"));
		}
		catch(EntityNotFoundException e){
			throw new RuntimeException("Get: Book with isbn" + this.isbn+" not found");
		}
		
		// if(syncCache.contains(this.isbn)){
	// 		result=(Entity)syncCache.get(this.isbn);
	// 		return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
	// 			this.isbn,(long)result.getProperty("peopleOffer"),
	// 				(long)result.getProperty("peopleDemand"));
	// 	}
	// 	else{
	// 		try{
	// 			result=datastore.get(bookKey);
	// 			return new Book((String)result.getProperty("title"),(String)result.getProperty("author"),
	// 				this.isbn,(long)result.getProperty("peopleOffer"),
	// 					(long)result.getProperty("peopleDemand"));
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

	@GET
	@Produces("text/html")
	public String getBookBrowser(){
		Book book=getBook();
		String res="<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"/stylesheets/index.css\"/></head>";
		res+="<body>";
		res+="<ul><li><a href=\"/index.jsp\">Home</a></li><li><a href=\"/account.jsp\">Account</a></li><li><a href=\"/message.jsp\">Messeging</a></li></ul>";
		res+="<p>Book Info<p>";
		res=res+"<p>Title:"+book.getTitle()+"&nbspAuthor:"+book.getAuthor()+"&nbspISBN:"+book.getIsbn()+"</p>";
		res=res+"<p>People Offer</p>";
		List<String> peopleOffer=book.getPeopleOffer();
		List<String> peopleDemand=book.getPeopleDemand();
		for(int i=0;i<peopleOffer.size();i++){
			OwnerResource ownerResource=new OwnerResource(uriInfo, request,peopleOffer.get(i));
			Owner owner=ownerResource.getOwnerData();
			res=res+"<a href=\"/ds/owner/"+peopleOffer.get(i)+"\">Name:"+owner.getName()+"</a>";
			res=res+"&nbspLocation:"+owner.getLocation()+"<br/>";
			i++;
		}
		res=res+"<p>Books Demand</p>";
		for(int i=0;i<peopleDemand.size();i++){
			OwnerResource ownerResource=new OwnerResource(uriInfo, request,peopleDemand.get(i));
			Owner owner=ownerResource.getOwnerData();
			res=res+"<a href=\"/ds/owner/"+peopleDemand.get(i)+"\">Name:"+owner.getName()+"</a>";
			res=res+"&nbspLocation:"+owner.getLocation()+"<br/>";
			i++;

		}
		res+="</body>";
		res+="</html";

		return res;
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





