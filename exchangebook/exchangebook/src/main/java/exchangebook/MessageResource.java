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
    System.out.println("*******************");
    System.out.println("Adding Message");
    System.out.println("*******************");
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
    System.out.println("*******************");
    System.out.println("Getting Message Sent");
    System.out.println("*******************");
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
    System.out.println("*******************");
    System.out.println("Getting Message Received");
    System.out.println("*******************");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<MessageReceived> list = new ArrayList<>();
    Query.Filter f_receiver = new Query.FilterPredicate("receiver", Query.FilterOperator.EQUAL, this.userID);
    Query.Filter f_delete = new Query.FilterPredicate("receiverDelete", Query.FilterOperator.EQUAL, false);
    Query.Filter f_received = Query.CompositeFilterOperator.and(f_receiver, f_delete);
    Query q = new Query("Message").setFilter(f_received)
                                  .addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> receive_list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

    for(Entity temp : receive_list) {
      System.out.print(temp);
      list.add(new MessageReceived((String)temp.getProperty("sender"), (String)temp.getProperty("title"),
                                  (String)temp.getProperty("body"), (Date)temp.getProperty("date"),
                                  temp.getKey().getId(), (boolean)temp.getProperty("receiverRead")));
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
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateReadState(@FormParam("messageID") long messageID,
                              @FormParam("read") boolean read) {
    System.out.println("*******************");
    System.out.println("Updating Message Read/Unread");
    System.out.println("*******************");
    Response res = null;
	  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	  Key messageKey = KeyFactory.createKey("Message", messageID);
    Entity message;
    if (syncCache.contains(messageKey)) {
      message = (Entity)syncCache.get(messageKey);
    } else {
      try {
        message = datastore.get(messageKey);
      } catch (EntityNotFoundException e) {
        System.out.println("no such message with this messageID");
        res = Response.noContent().build();
        return res;
      }
    }
    //update read/unread if this user is the receiver
    if (!userID.equals((String)message.getProperty("receiver"))) {
      System.out.println("Wrong messageID");
      res = Response.noContent().build();
    } else {
      message.setProperty("receiverRead", read);
      datastore.put(message);
      syncCache.put(messageKey, message);
      res = Response.created(uriInfo.getAbsolutePath()).build();	 
    }
    return res;
  }
  
  @Path("{messageID}")
  @DELETE
  public void deleteMessage(@PathParam("messageID") long messageID) {
    System.out.println("*******************");
    System.out.println("Deleting Message");
    System.out.println("*******************");
	  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	  Key messageKey = KeyFactory.createKey("Message", messageID);
    System.out.print(messageID);
    System.out.print(messageKey);
    Entity message;
    if (syncCache.contains(messageKey)) {
      message = (Entity)syncCache.get(messageKey);
    } else {
      try {
        message = datastore.get(messageKey);
      } catch (EntityNotFoundException e) {
        throw new RuntimeException("no such message with this messageID");
      }
    }
    //mark as delete
    if (userID.equals((String)message.getProperty("receiver"))) {
      message.setProperty("receiverDelete", true);
    } else if (userID.equals((String)message.getProperty("sender"))) {
      message.setProperty("senderDelete", true); 
    } else {
      throw new RuntimeException("Wrong Path");
    }
    
    //if both sender and receiver have deleted the message, delete message from datastore
    if ((boolean)message.getProperty("senderDelete") == true && (boolean)message.getProperty("receiverDelete") == true) {
      datastore.delete(messageKey);
      if (syncCache.contains(messageKey)) {
        syncCache.delete(messageKey);
      }
    } else {
      datastore.put(message);
      syncCache.put(messageKey, message);
    }
  }
}