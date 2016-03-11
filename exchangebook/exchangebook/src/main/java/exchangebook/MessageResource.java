package exchangebook;
import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import com.google.gson.Gson;
import exchangebook.Message;

public class MessageResource{
	@Context
	UriInfo uriInfo;
	@Context 
	Request request;
	String userID;
  
	public MessageResource(UriInfo uriInfo, Request request, String userID) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.userID = userID;
	}
  
	@POST
  @Produces(MediaType.TEXT_HTML)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newMessage(@FormParam("receiver") String receiver,
	@FormParam("title") String title,
	@FormParam("body") String body,
  @FormParam("delete") boolean delete,
	@Context HttpServletResponse servletResponse)throws IOException{
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	Entity message = new Entity("Message");
  	message.setProperty("title", title);
    message.setProperty("receiver", receiver);
    message.setProperty("body", body);
    message.setProperty("sender", this.userID);
    message.setProperty("senderDelete", delete);
    message.setProperty("receiverDelete", false);
    message.setProperty("receiverRead", false);
    Date date = new Date();
    message.setProperty("date", date);
  	datastore.put(message);
	}
  
  private List<MessageSent> getMessageSent() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<MessageSent> list = new ArrayList<>();
    Query.Filter f_sender = new Query.FilterPredicate("sender", Query.FilterOperator.EQUAL, this.userID);
    Query.Filter f_delete = new Query.FilterPredicate("senderDelete", Query.FilterOperator.EQUAL, false);
    Query.Filter f_sent = Query.CompositeFilterOperator.and(f_sender, f_delete);
    
    Query q = new Query("Message").setFilter(f_sent)
                                  .addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> sent_list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    System.out.println("sent");
    System.out.print(sent_list);
    for(Entity temp : sent_list) {
      list.add(new MessageSent((String)temp.getProperty("receiver"), (String)temp.getProperty("title"), 
                              (String)temp.getProperty("body"), (Date)temp.getProperty("date"),
                              temp.getKey().getId()));
    }
    return list;
  }
  
  private List<MessageReceived> getMessageReceived() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<MessageReceived> list = new ArrayList<>();
    Query.Filter f_receiver = new Query.FilterPredicate("receiver", Query.FilterOperator.EQUAL, this.userID);
    Query.Filter f_delete = new Query.FilterPredicate("receiverDelete", Query.FilterOperator.EQUAL, false);
    Query.Filter f_received = Query.CompositeFilterOperator.and(f_receiver, f_delete);
    Query q = new Query("Message").setFilter(f_received)
                                  .addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> receive_list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    System.out.print(receive_list);
    if (receive_list != null) {
      System.out.println(" not empty");
      for(Entity temp : receive_list) {
        list.add(new MessageReceived((String)temp.getProperty("sender"), (String)temp.getProperty("title"), 
                                (String)temp.getProperty("body"), (Date)temp.getProperty("date"),
                                temp.getKey().getId(), (boolean)temp.getProperty("receiveRead")));
      }
    }  
    return list;
  }
  
  
  //for the browser
  @Path("sent")
  @GET
  @Produces(MediaType.TEXT_XML)
  public List<MessageSent> getMessagerSentHTML() {
    return getMessageSent();
  }

  //
  //for the application
  @Path("sent")
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<MessageSent> getMessageSentData() {
    //same code as above method
    return getMessageSent();
  }
 
  //for the browser
  @Path("received")
  @GET
  @Produces(MediaType.TEXT_XML)
  public List<MessageReceived> getMessagerReceivedHTML() {
    return getMessageReceived();
  }


  //for the application
  @Path("received")
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<MessageReceived> getMessageReceivedData() {
    //same code as above method
    return getMessageReceived();
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response putTaskData(long messageID) {
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
}