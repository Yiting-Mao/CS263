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
				resp.getWriter().println(temp.getKey().getName()+" : "+(String)temp.getProperty("value") +" : "+(Date)temp.getProperty("date")+"<br>");
				k_names.add((String)temp.getKey().getName());
			}  	
			
			resp.getWriter().println(">>>>>>>>Displaying contents in MemCache<br>");
			for(String k_name:k_names){
				if(syncCache.contains(k_name)){
					Entity taskdata=(Entity)syncCache.get(k_name);
					resp.getWriter().println(k_name+" : "+(String)taskdata.getProperty("value")+" : "+(Date)taskdata.getProperty("date")+"<br>");
				}
			}
				
	  	
	  	}
	  	else if(value==null){
  		    //resp.getWriter().println("b : "+((Entity)syncCache.get("b")).getProperty("value")+ "+++++++++<br>"); //when rerun, keyname=a,c,d, output 																							null, keyname=b, output right.
	  		Key entKey=KeyFactory.createKey("TaskData",keyname);
	  		Entity r_Data;
			if(syncCache.contains(keyname)){
				r_Data=(Entity)syncCache.get(keyname);
				String r_value=(String)r_Data.getProperty("value");
				resp.getWriter().println(keyname+ " : "+r_value+ "(Both)"+" : "+(Date)r_Data.getProperty("date"));				
			}
			else{
		  		try{
		  			r_Data=datastore.get(entKey);  //Read form Datastore
		  			String r_value=(String)r_Data.getProperty("value");
		  			//Date r_Date=(Date)r_Data.getProperty("date");
		  			resp.getWriter().println(keyname+ " : "+r_value+ "(Datastore)"+" : "+(Date)r_Data.getProperty("date"));
					syncCache.put(keyname,r_Data); //Add to cache 			
		  		}
		  		catch(EntityNotFoundException e){
		  			resp.getWriter().println(keyname+"(Neither)");
		  		}
			}		
	  	}
	  	else{
	  		Entity inputData=new Entity("TaskData",keyname);
	  		inputData.setProperty("value",value);
	  		Date createDate=new Date();
	  		inputData.setProperty("date", createDate);
	  		datastore.put(inputData);
			resp.getWriter().println("Stored "+keyname+" and "+value+" in Datastore<br>");
			syncCache.put(keyname,inputData);
			resp.getWriter().println("Stored "+keyname+" and "+value+" in Memcache<br>");
	  	}
	  }
	  
      //Add your code here

      resp.getWriter().println("</body></html>");
  }
}