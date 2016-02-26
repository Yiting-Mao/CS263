<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.taskqueue.Queue"%>
<%@ page import="com.google.appengine.api.taskqueue.QueueFactory"%>
<%@ page import="com.google.appengine.api.taskqueue.TaskOptions"%>
<%@ page import="java.util.List" %>
<%@ page import="java.io.*"%>
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
				    window.location = '/addmessage.html?receiver=' + receiver;
			   }
		 });
		 });
	 </script>
</head>
<body>
	 <h2>This is a heading</h2>
	 <p>This is a paragraph.</p>
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


	 <ul>
	 <li><a href="/index.jsp">Home</a></li>
	 <li><a href="/account.jsp">My Account</a></li>
	 <li><a href="/message.jsp">Messeging</a></li>
	 </ul>
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
		 

</body>

</html>

