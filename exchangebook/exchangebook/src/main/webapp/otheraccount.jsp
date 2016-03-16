<!-- This page serves to display other user's home page -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.*"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
			 pageContext.setAttribute("user", user);
       %>
       <p>Hello, ${fn:escapeXml(user.nickname)}! (
           <a href="<%= userService.createLogoutURL("/otheraccount.jsp?targetID="+targetID+"") %>">sign out</a>.)
       </p>
       <%
		 } else {
       %>
       <p>Hello!
           <a href="<%= userService.createLoginURL("/otheraccount.jsp?targetID="+targetID+"") %>">Sign in</a>
           to exchange books!
       </p>
       <%
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

