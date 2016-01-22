// The Worker servlet should be mapped to the "/worker" URL.
package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyname = request.getParameter("keyname");
		String value=request.getParameter("value");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	  	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	  	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		
  		Entity inputData=new Entity("TaskData",keyname);
  		inputData.setProperty("value",value);
  		Date createDate=new Date();
  		inputData.setProperty("date", createDate);
  		datastore.put(inputData);
     	syncCache.put(keyname,value);
    }
}