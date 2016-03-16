<!-- add messages -->
<!-- Use jquery to post to the rest enqueue dispatcher, when it's done, redirects to the page which accesses this -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.io.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head> 
  <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
 <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
 <script  type="text/javascript" src="./js/addmessage.js"></script>
</head>
<body>
  <div id = "container">
	<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
		pageContext.setAttribute("sender",user.getUserId());
		pageContext.setAttribute("receiver",request.getParameter("receiver"));
    pageContext.setAttribute("reqURI",request.getParameter("reqURI"));
    pageContext.setAttribute("title", request.getParameter("title"));
	%>
    
 
  <input type="hidden" id="receiver" value="${fn:escapeXml(receiver)}">
	Title: <input type="text" id="title" size="100" value="${fn:escapeXml(title)}"><br/>
  Body: <br/>
  <textarea id="body" rows="30" cols="100"></textarea><br/>
	<input type="hidden" id="sender" value="${fn:escapeXml(sender)}">
	<input type="hidden" id="reqURI" value="${fn:escapeXml(reqURI)}">
	<button>Send</button>
</div>
</body>
</html>