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

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- This page serves to display current user's personal page/ view other user's home page -->

<!DOCTYPE html>
<html>
<head>
	 <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
	 <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
	 <script type="text/javascript" src="./js/otheraccount.js"> </script>
</head>
<body>
    <div id = "container">
	 <%
     UserService userService = UserServiceFactory.getUserService();
     User user = userService.getCurrentUser();
		 String targetID=request.getParameter("targetID");
	 	 String userID=null;
		 if(user!=null){
			 userID=user.getUserId();
       pageContext.setAttribute("userID", userID);
		 }
		 pageContext.setAttribute("targetID",targetID);
	 %>
   <input type="hidden" id="userID" value="${fn:escapeXml(userID)}">
     <input type="hidden" id="targetID" value="${fn:escapeXml(targetID)}">

	 <ul class="navi">
	 <li><a class="navi" href="/index.jsp">Home</a></li>
	 <li><a class="navi" href="/myaccount.jsp">Account</a></li>
	 <li><a class="navi" href="/message.jsp">Messeging</a></li>
	 </ul><br/>
   
   <div id = "personalinfo"></div>   
   <div id = "book"></div>
</div>
</body>

</html>

