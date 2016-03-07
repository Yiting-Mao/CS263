package exchangebook;
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

@Path("/message")

public class MessageResource{
	@Context
	UriInfo uriInfo;
	@Context 
	Request request;
	
	@POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newMessage(@FormParam("sender") String sender,
		@FormParam("receiver") String receiver,
		@FormParam("title") String title,
		@FormParam("body") String body,
		@Context HttpServletResponse servletResponse)throws IOException{
    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Entity message = new Entity("Message");
    	message.setProperty("title", title);
      message.setProperty("receiver", receiver);
      message.setProperty("body", body);
      message.setProperty("sender", sender);
      Date date = new Date();
      message.setProperty("date", date);
    	datastore.put(message);
		}
    
}