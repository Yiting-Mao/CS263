package exchangebook;
import exchangebook.Owner;
import exchangebook.Book;
import exchangebook.BookResource;

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

public class OwnerResource{
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String userID;
	
	public OwnerResource(UriInfo uriInfo, Request request, String userID){
		this.uriInfo=uriInfo;
		this.request=request;
		this.userID=userID;
	}
	
	private Owner getOwner(){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		Key ownerKey=KeyFactory.createKey("Owner",this.userID);
		Entity result;
		
		try{
			result=datastore.get(ownerKey);
			return new Owner(this.userID,(String)result.getProperty("name"),(String)result.getProperty("location"),
				(List<String>)result.getProperty("bookOffer"),
					(List<String>)result.getProperty("bookDemand"));
		}
		catch(EntityNotFoundException e){
			throw new RuntimeException("Get: Owner with userID" + this.userID+" not found");
		}
		
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
	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getOwnerBrowser( 
		@Context HttpServletRequest req, @Context HttpServletResponse resp) throws IOException,ServletException{
		    req.setAttribute(this.getClass().getName(), this);
		    req.getRequestDispatcher("/account.jsp?targetID="+userID).forward(req, resp);
		}
		// Owner owner=getOwner();
	// 	String res="<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"/stylesheets/index.css\"/></head>";
	// 	res+="<body>";
	// 	res+="<ul><li><a href=\"/index.jsp\">Home</a></li><li><a href=\"/account.jsp\">Account</a></li><li><a href=\"/message.jsp\">Messeging</a></li></ul>";
	// 	res+="<p>Personal Info<p>";
	// 	res=res+"<p>Name:"+owner.getName()+"<br/>Location:"+owner.getLocation()+"</p>";
	// 	res=res+"<p>Books Offer<p>";
	// 	List<String> bookOffer=owner.getBookOffer();
	// 	List<String> bookDemand=owner.getBookDemand();
	// 	for(int i=0;i<bookOffer.size();i++){
	// 		BookResource bookResource=new BookResource(uriInfo, request,bookOffer.get(i));
	// 		Book book=bookResource.getBookData();
	// 		res=res+"<a href=\"/ds/book/"+bookOffer.get(i)+"\">"+book.getTitle()+"</a>";
	// 		res=res+"&nbspAuthor:"+book.getAuthor()+"&nbspIsbn:"+bookOffer.get(i)+"<br/>";
	// 		i++;
	//
	// 	}
	// 	res=res+"<p>Books Demand<p>";
	// 	for(int i=0;i<bookDemand.size();i++){
	// 		BookResource bookResource=new BookResource(uriInfo, request,bookDemand.get(i));
	// 		Book book=bookResource.getBookData();
	// 		res=res+"<a href=\"/ds/book/"+bookDemand.get(i)+"\">"+book.getTitle()+"</a>";
	// 		res=res+"&nbspAuthor:"+book.getAuthor()+"&nbspIsbn:"+bookDemand.get(i)+"<br/>";
	// 		i++;
	//
	// 	}
	// 	res+="</body>";
	// 	res+="</html";
	// 	return res;
	 // }
	
}





