<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.taskqueue.Queue"%>
<%@ page import="com.google.appengine.api.taskqueue.QueueFactory"%>
<%@ page import="com.google.appengine.api.taskqueue.TaskOptions"%>
<%@ page import="com.google.appengine.api.datastore.*"%>
<%@ page import="com.google.appengine.api.memcache.*"%>
<%@ page import="java.util.List" %>
<%@ page import="java.io.*"%>

<%@ page import="exchangebook.Book" %>
<%@ page import="exchangebook.BookResource" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- This page serves to display current user's personal page/ view other user's home page -->

<!DOCTYPE html>
<html>
<head>
	 <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
	 <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
	 <script type="text/javascript">
		 $(document).ready(function(){
		   $("button").click(function(){
			   if($(this).attr('id')=="updateinfo")
				     window.location = '/addinfo.jsp?reqURI=/account.jsp';
			   if($(this).attr('id')=="sendmessage"){
				   var receiver=$(this).data("receiver");
				    window.location = '/addmessage.jsp?receiver=' + receiver + '&reqURI=/account.jsp';
			   }
		 });
		 });
	 </script>
</head>
<body>
    <div id = "container">
	 <%
	     UserService userService = UserServiceFactory.getUserService();
	     User user = userService.getCurrentUser();
		 String targetID=request.getParameter("targetID");
	 	 String name=request.getParameter("name");
	 	 String location=request.getParameter("location");
	 	 String userID=null;
		 if(user!=null){
			 pageContext.setAttribute("user", user);
			 userID=user.getUserId();
			 // handle request from addinfo.jsp
			 if(targetID==null)targetID=userID;
		 }
		 pageContext.setAttribute("targetID",targetID);
 	// servletResponse.sendRedirect(redirectURL);
	 %>


	 <ul class="navi">
	 <li><a href="/index.jsp">Home</a></li>
	 <li><a href="/account.jsp">Account</a></li>
	 <li><a href="/message.jsp">Messeging</a></li>
	 </ul><br/>
	 <% DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		Key ownerKey=KeyFactory.createKey("Owner",targetID);
		Entity result;
		try{
			result=datastore.get(ownerKey);
        }
	  	catch(EntityNotFoundException e){
	  		throw new RuntimeException("UserID with " + targetID +  " not found");
	  	}
		pageContext.setAttribute("name", result.getProperty("name"));
		pageContext.setAttribute("location", result.getProperty("location"));
	%>
        <h2> Personal Info: </h2>
        <p> Name: ${fn:escapeXml(name)} Location: ${fn:escapeXml(location)} </p>
	 <% if(targetID==userID){
	 %>
		 <button type="button" id="updateinfo">Update Info</button>
	<%	
	 }else{
	%>
		<button type="button" id="sendmessage" data-receiver="${fn:escapeXml(targetID)}">Send Message</button>
	<%
	 }
	 %>
         <h2> Books Offer: </h2>
 	<%
         List<String> bookOffer=(List<String>)result.getProperty("bookOffer");
         if(bookOffer != null){
             for(int i=0; i<bookOffer.size(); i++){
                 String isbn =bookOffer.get(i);
                 Key bookKey = KeyFactory.createKey("Book", bookOffer.get(i));
                 // May have a little problem here
                 Entity book = datastore.get(bookKey);

                 pageContext.setAttribute("title", book.getProperty("title"));
                 pageContext.setAttribute("author",book.getProperty("author"));
                 pageContext.setAttribute("isbn", bookOffer.get(i));
                 %>
                     <div id = ${fn:escapeXml(isbn)} >
                         <p> <a href = "/ds/book/${fn:escapeXml(isbn)}">Title:${fn:escapeXml(title)}</a>
                             &nbsp Author:${fn:escapeXml(author)}&nbsp ISBN: ${fn:escapeXml(isbn)}
                         </p>
                     </div>
                <%
             }     	 
         }
         
         %>
         
             <h2> Books Demand: </h2>
     	<%
             List<String> bookDemand=(List<String>)result.getProperty("bookDemand");
             if(bookDemand != null){
                 for(int i=0; i<bookDemand.size(); i++){
                     String isbn =bookDemand.get(i);
                     Key bookKey = KeyFactory.createKey("Book", bookDemand.get(i));
                     // May have a little problem here
                     Entity book = datastore.get(bookKey);

                     pageContext.setAttribute("title", book.getProperty("title"));
                     pageContext.setAttribute("author",book.getProperty("author"));
                     pageContext.setAttribute("isbn", bookOffer.get(i));
                     %>
                         <div id = ${fn:escapeXml(isbn)} >
                             <p> <a href = "/ds/book/${fn:escapeXml(isbn)}">Title:${fn:escapeXml(title)}</a>
                                 &nbsp Author:${fn:escapeXml(author)}&nbsp ISBN: ${fn:escapeXml(isbn)}
                             </p>
                         </div>
                    <%
                 }     	 
             }
         
             %>
</div>
</body>

</html>

