<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.*"%>
<%@ page import="com.google.appengine.api.memcache.*"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js">
	</script>
</head>
<body>
 <div id = "container">

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
	  String userID;
    pageContext.setAttribute("user", user);
	  userID=user.getUserId();
	  System.out.println(userID);
    if (user != null){
      %>
          <p>Hello, ${fn:escapeXml(user.nickname)}! (
              <a href="<%= userService.createLogoutURL("/index.jsp") %>">sign out</a>.)</p>
      <%
    }
  
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    %>
    <ul class="navi">
    <li><a class="navi" href="/index.jsp">Home</a></li>
    <li><a class="navi" href="/account.jsp">Account</a></li>
    <li><a class="navi" href="/message.jsp" style="background-color: #DEB887">Messeging</a></li>
    </ul><br/>
    <h2> Message Received: </h2>
    <%
    
    Query.Filter f =new Query.FilterPredicate("receiver",Query.FilterOperator.EQUAL,userID);
    Query q = new Query("Message").setFilter(f).
              addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> receive=datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: receive){
			String sender = (String)result.getProperty("sender");
      String title = (String)result.getProperty("title");
      String body = (String)result.getProperty("body");
      Date date = (Date) result.getProperty("date");
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateString = dateFormat.format(date);
      pageContext.setAttribute("sender", sender);
      pageContext.setAttribute("title", title);
      pageContext.setAttribute("body", body);
      pageContext.setAttribute("date", dateString);
      %>
        <div>
            <p> 
              Title: ${fn:escapeXml(title)} <br/>
              From: <a href = "/ds/owner/${fn:escapeXml(sender)}">${fn:escapeXml(sender)}</a> &nbsp
              Time:${fn:escapeXml(date)} <br/>
              Body:<br/><pre>${body}</pre> 
              <hr />
            </p>
        </div>
      <%
		}    
    %>

      <h2> Message Sent: </h2>
   <%
   
   f =new Query.FilterPredicate("sender",Query.FilterOperator.EQUAL,userID);
   q = new Query("Message").setFilter(f).
             addSort("date", Query.SortDirection.DESCENDING);
	 List<Entity> send=datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
	 for(Entity result: send){
		String receiver = (String)result.getProperty("receiver");
     String title = (String)result.getProperty("title");
     String body = (String)result.getProperty("body");
     Date date = (Date) result.getProperty("date");
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String dateString = dateFormat.format(date);
     pageContext.setAttribute("receiver", receiver);
     pageContext.setAttribute("title", title);
     pageContext.setAttribute("body", body);
     pageContext.setAttribute("date", dateString);
     %>
       <div>
           <p> 
             Title: ${fn:escapeXml(title)} <br/> 
             To: <a href = "/ds/owner/${fn:escapeXml(receiver)}">${fn:escapeXml(sender)}</a> 
             &nbsp Time:${date} <br/>
             Body:<br/><pre>${body}</pre> 
             <hr />
           </p>
       </div>
     <%
	}    
   %>
</div>
</body>
</html>