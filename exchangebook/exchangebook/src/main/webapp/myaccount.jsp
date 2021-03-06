<!-- This page serves to display current user's personal page -->

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
	 <script type="text/javascript" src="./js/myaccount.js"></script>
</head>
<body>
    <div id = "container">
	 <%
     UserService userService = UserServiceFactory.getUserService();
     User user = userService.getCurrentUser();
	 	 String userID=null;
		 if(user!=null){
			 pageContext.setAttribute("user", user);
			 userID=user.getUserId();
       pageContext.setAttribute("userID", userID);
       %>
       <p>Hello, ${fn:escapeXml(user.nickname)}! (
           <a href="<%= userService.createLogoutURL("/index.jsp") %>">sign out</a>.)
       </p>
       <%
		 }
	 %>
   <input type="hidden" id="userID" value="${fn:escapeXml(userID)}">

	 <ul class="navi">
	 <li><a class="navi" href="/index.jsp">Home</a></li>
	 <li><a class="navi" href="/myaccount.jsp" style="background-color: #DEB887">Account</a></li>
	 <li><a class="navi" href="/message.jsp">Messeging</a></li>
	 </ul><br/>
   
   <div id = "personalinfo"></div> 
     
   <div id = "book"></div>
</div>
</body>

</html>

