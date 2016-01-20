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
	  		List<Entity> results=datastore.prepare(display_all).asList(
				FetchOptions.Builder.withLimit(20));
			for(Entity temp: results){
				String r_value=(String)temp.getProperty("value");
				Date r_Date=(Date)temp.getProperty("date");
				resp.getWriter().println("Value="+r_value+ ". Created at " +r_Date +"<br>");
			}  		
	  	
	  	}
	  	else if(value==null){
	  		Key entKey=KeyFactory.createKey("TaskData",keyname);
	  		Entity r_Data;
	  		try{
	  			r_Data=datastore.get(entKey);
	  			String r_value=(String)r_Data.getProperty("value");
	  			Date r_Date=(Date)r_Data.getProperty("date");
	  			resp.getWriter().println("The value of key "+keyname+ " is "+r_value+ ". Created at " +r_Date +"\n");	  			
	  		}
	  		catch(EntityNotFoundException e){
	  			resp.getWriter().println("There is no such entity in TaskData\n");
	  		}
	  	}
	  	else{
	  		Entity inputData=new Entity("TaskData",keyname);
	  		inputData.setProperty("value",value);
	  		Date createDate=new Date();
	  		inputData.setProperty("date", createDate);
	  		datastore.put(inputData);
			resp.getWriter().println("Stored "+keyname+" and "+value+" in Datastore");
	  	}
	  }
	  
      //Add your code here

      resp.getWriter().println("</body></html>");
  }
}