package exchangebook;
import exchangebook.Book;
import exchangebook.Owner;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@Path("/enqueue")
public class EnqueueDispatcher {
  @Path("addbook")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void enqueueNewBook(@FormParam("isbn") String isbn,
	  @FormParam("author") String author,
  	@FormParam("title") String title,
		@FormParam("option") String option,
		@FormParam("quantity") int quantity, 
		@FormParam("user") String userID,
		@Context HttpServletResponse servletResponse)throws IOException {
    System.out.println("*******************");
    System.out.println("Enqueue: Adding Book");
    System.out.println("*******************");
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(TaskOptions.Builder.withUrl("/ds").param("isbn", isbn)
                .param("author", author).param("title", title).param("option", option)
                .param("quantity", Integer.toString(quantity)).param("user", userID));
  }
    
  @Path("addownerinfo")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void enqueueAddOwnerInfo(
    @FormParam("name") String name,
    @FormParam("location") String location,
    @FormParam("userID") String userID,
    @Context HttpServletResponse servletResponse)throws IOException {
    System.out.println("*******************");
    System.out.println("Enqueue: Adding Owner Info");
    System.out.println("*******************");
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(TaskOptions.Builder.withUrl("/ds/owner/" + userID + "/addinfo").param("name", name)
                .param("location", location));
  }
  //The following two functions can't work right now, because task queue sends POST request
  // @Path("deletebook")
 //  @DELETE
 //  public void enqueueDeleteBook(
 //    @FormParam("userID") String userID,
 //    @FormParam("isbn") String isbn,
 //    @FormParam("option") String option,
 //    @FormParam("num") long num,
 //    @Context HttpServletResponse servletResponse)throws IOException {
 //    System.out.println("*******************");
 //    System.out.println("Enqueue: Deleting Book");
 //    System.out.println("*******************");
 //    Queue queue = QueueFactory.getDefaultQueue();
 //    String url = "/ds/owner/" + userID + "/" + isbn;
 //    System.out.print(url);
 //    queue.add(TaskOptions.Builder.withUrl(url).param("option", option)
 //                .param("num", Long.toString(num)));
 //  }
 //
 //  @Path("deleteowner")
 //  @DELETE
 //  public void enqueueDeleteOwner(
 //    @FormParam("userID") String userID,
 //    @Context HttpServletResponse servletResponse)throws IOException {
 //    System.out.println("*******************");
 //    System.out.println("Enqueue: Deleting Owner");
 //    System.out.println("*******************");
 //    Queue queue = QueueFactory.getDefaultQueue();
 //    String url = "/ds/owner/" + userID;
 //    System.out.print(url);
 //    queue.add(TaskOptions.Builder.withUrl(url));
 //  }
 
 @Path("addmessage")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 public void enqueueAddMessage(
  @FormParam("userID") String userID,
  @FormParam("receiver") String receiver,
	@FormParam("title") String title,
	@FormParam("body") String body,
  @FormParam("delete") boolean delete,
	@Context HttpServletResponse servletResponse)throws IOException{
   System.out.println("*******************");
   System.out.println("Enqueue: Adding Message");
   System.out.println("*******************");
   Queue queue = QueueFactory.getDefaultQueue();
   String delete_str = delete ? "true" : "false";
   queue.add(TaskOptions.Builder.withUrl("/ds/message/" + userID).param("receiver", receiver)
               .param("title", title).param("body", body).param("delete", delete_str));
 }
 
}