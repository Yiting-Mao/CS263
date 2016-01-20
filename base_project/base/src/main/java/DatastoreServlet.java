package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	  MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	  syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	  
      resp.setContentType("text/html");
      resp.getWriter().println("<html><body>");
	  
	  String keyname=req.getParameter("keyname");
	  String value=req.getParameter("value");
	  Enumeration params=req.getParameterNames();
	  boolean LegalParam=true;
	  while(params.hasMoreElements()){
		  String paramName=(String)params.nextElement();
		  if(!paramName.equals("keyname")&&!paramName.equals("value")){   //String compare in Java
			  resp.getWriter().println("<h2>Wrong input of parameters</h2>");
			  LegalParam=false;
			  break;
		  }
	  }

  	
	  if(LegalParam){
		
	  	if(keyname==null&&value==null){
	  		Query display_all=new Query("TaskData");
			//display_all.addSort("date", SortDirection.ASCENDING);
			List<String> k_names=new ArrayList<>();
			resp.getWriter().println(">>>>>>>>Displaying contents in Datastore<br>");
	  		List<Entity> results=datastore.prepare(display_all).asList(FetchOptions.Builder.withLimit(20));
			for(Entity temp: results){
				resp.getWriter().println(temp.getKey().getName()+" : "+(String)temp.getProperty("value") +"<br>");
				k_names.add((String)temp.getKey().getName());
			}  	
			
			resp.getWriter().println(">>>>>>>>Displaying contents in MemCache<br>");
			for(String k_name:k_names){
				if((String) syncCache.get(k_name)!=null){
					resp.getWriter().println(k_name+" : "+syncCache.get(k_name)+"<br>");
				}
			}
				
	  	
	  	}
	  	else if(value==null){
  		    //resp.getWriter().println("b : "+(String)syncCache.get("b")+ "+++++++++<br>"); //when rerun, keyname=a,c,d, output 																							null, keyname=b, output right.
	  		Key entKey=KeyFactory.createKey("TaskData",keyname);
	  		Entity r_Data;
	  		try{
				String r_value = (String)syncCache.get(keyname); // Read from cache.
				if (r_value == null){
		  			r_Data=datastore.get(entKey);  //Read form Datastore
		  			r_value=(String)r_Data.getProperty("value");
		  			//Date r_Date=(Date)r_Data.getProperty("date");
		  			resp.getWriter().println(keyname+ " : "+r_value+ "(Datastore)");
					syncCache.put(keyname,r_value); //Add to cache
				}
				else resp.getWriter().println(keyname+ " : "+r_value+ "(Both)");
	  			
	  		}
	  		catch(EntityNotFoundException e){
	  			resp.getWriter().println(keyname+"(Neither)");
	  		}
	  	}
	  	else{
	  		Entity inputData=new Entity("TaskData",keyname);
	  		inputData.setProperty("value",value);
	  		Date createDate=new Date();
	  		inputData.setProperty("date", createDate);
	  		datastore.put(inputData);
			resp.getWriter().println("Stored "+keyname+" and "+value+" in Datastore<br>");
			syncCache.put(keyname,value);
			resp.getWriter().println("Stored "+keyname+" and "+value+" in Memcache<br>");
	  	}
	  }
	  
      //Add your code here

      resp.getWriter().println("</body></html>");
  }
}