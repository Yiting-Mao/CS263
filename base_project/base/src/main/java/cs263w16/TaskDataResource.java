package cs263w16;
import cs263w16.TaskData;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

import javax.xml.bind.JAXBElement;

public class TaskDataResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  String keyname;

  public TaskDataResource(UriInfo uriInfo, Request request, String kname) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.keyname = kname;
  }
  
  private TaskData getData(){
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	Key entKey=KeyFactory.createKey("TaskData",this.keyname);
  	Entity r_Data;
	if (syncCache.contains(this.keyname)) {
				Entity entity = (Entity) syncCache.get(this.keyname);
				return new TaskData(this.keyname, (String)entity.getProperty("value"), (Date)entity.getProperty("date"));
	} 
	else {
	  	try{	
	  		r_Data=datastore.get(entKey);  //Read form Datastore
	  		String r_value=(String)r_Data.getProperty("value");
	  		Date r_date=(Date)r_Data.getProperty("date");
	  		return new TaskData(this.keyname, r_value,r_date);
	  	}
	  	catch(EntityNotFoundException e){
	  		throw new RuntimeException("Get: TaskData with " + this.keyname +  " not found");
	  	}
	}
   
  }
  // for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public TaskData getTaskDataHTML() {
      //add your code here (get Entity from datastore using this.keyname)
      // throw new RuntimeException("Get: TaskData with " + keyname +  " not found");
      //if not found
	  return getData();
	
  }
  // for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public TaskData getTaskData() {
    //same code as above method
	return getData();
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public Response putTaskData(String val) {
    Response res = null;
    //add your code here
    //first check if the Entity exists in the datastore
    //if it is not, create it and 
    //signal that we created the entity in the datastore 
	//else signal that we updated the entity
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	Key entKey=KeyFactory.createKey("TaskData",this.keyname);
	Entity inputData;
	try{
		inputData=datastore.get(entKey);
		inputData.setProperty("value",val);
		//res.getWriter().println("Entity Updated");
		res = Response.noContent().build();
		
	}catch(EntityNotFoundException e){ 
		inputData=new Entity("TaskData",this.keyname);
		inputData.setProperty("value",val);
		Date createDate=new Date();
		inputData.setProperty("date", createDate);
		//res.getWriter().println("Entity Created");
		res = Response.created(uriInfo.getAbsolutePath()).build();	
	}
	datastore.put(inputData);
	syncCache.put(this.keyname, inputData);

    return res;
  }

  @DELETE
  public void deleteIt() {
    //delete an entity from the datastore
    //just print a message upon exception (don't throw)
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	Key entKey=KeyFactory.createKey("TaskData",keyname);
	try{
		datastore.delete(entKey);
	}catch(java.lang.IllegalArgumentException e){
		System.out.println("Invalid Key");
	}
	if(syncCache.contains(keyname)){
		syncCache.delete(keyname);
	}
   }
  
}