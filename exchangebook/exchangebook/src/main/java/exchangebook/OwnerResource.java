package exchangebook;
import exchangebook.Owner;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
	
}





