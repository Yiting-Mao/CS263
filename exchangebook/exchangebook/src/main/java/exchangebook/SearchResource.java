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


@Path("/search")
public class SearchResource {
  
  //search books 
  @Path("book")
  @GET
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<BookInfo> searchBook(@QueryParam("isbn") String isbn,
	  @QueryParam("author") String author,
  	@QueryParam("title") String title,
		@Context HttpServletResponse servletResponse)throws IOException {
    System.out.println("*******************");
    System.out.println("Searching Book");
    System.out.println("*******************");
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    List<BookInfo> book = new ArrayList<BookInfo>();
    Query.Filter f_isbn = new Query.FilterPredicate("isbn", Query.FilterOperator.EQUAL, isbn);
    Query.Filter f_author = new Query.FilterPredicate("author", Query.FilterOperator.EQUAL, author);
    Query.Filter f_title = new Query.FilterPredicate("title", Query.FilterOperator.EQUAL, title);
    Query.Filter f= Query.CompositeFilterOperator.or(f_isbn, f_author, f_title);
    Query q = new Query("Book").setFilter(f);
     //Though defined as list, should return no more than one entity
    List<Entity> list = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
    for(Entity temp : list) {
      book.add(new BookInfo((String)temp.getProperty("title"), (String)temp.getProperty("author"),(String)temp.getKey().getName()));
    }
    return book;
  }
}