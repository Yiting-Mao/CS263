package exchangebook;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");
		String isbn=req.getParameter("isbn");
		String author=req.getParameter("author");
		String title=req.getParameter("title");
		Query bookResult=new Query("Book");
		resp.getWriter().println(">>>>>>>Book Founded with the same isbn<br>");
		Key bookKey=KeyFactory.createKey("Book",isbn);
		Entity book;
		if(syncCache.contains(isbn)){
			book=(Entity)syncCache.get(isbn);
			resp.getWriter().println("isbn:"+isbn+" title:"+(String)book.getProperty("title")+" author:"+
				(String)book.getProperty("author")+" offerNum:"+(long)book.getProperty("offerNum")+" demandNum:"+
					(long)book.getProperty("demandNum")+"<br>");
		}
		else{
			try{
				book=datastore.get(bookKey);
				resp.getWriter().println("isbn:"+isbn+" title:"+(String)book.getProperty("title")+" author:"+
					(String)book.getProperty("author")+" offerNum:"+(long)book.getProperty("offerNum")+" demandNum:"+
						(long)book.getProperty("demandNum")+"<br>");
				syncCache.put(bookKey,book);
			}
			catch(EntityNotFoundException e){
				resp.getWriter().println("Not Found <br>");
			}
		}
		resp.getWriter().println(">>>>>>>>People who offer this book<br>");
		
		Query.Filter ownerFilter=new Query.FilterPredicate("bookOffer",Query.FilterOperator.EQUAL,isbn);
		Query ownerQuery=new Query("BookOwner").setFilter(ownerFilter);
		List<Entity> ownerList=datastore.prepare(ownerQuery).asList(FetchOptions.Builder.withDefaults());
		for(Entity temp: ownerList){
			resp.getWriter().println(temp.getKey().getName()+"<br>");
		} 
	}
}