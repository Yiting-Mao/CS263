<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
</head>
<body>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
	String userID;
  
    pageContext.setAttribute("user", user);
	userID=user.getUserId();
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! </p>

	
<ul>
<li><a href="index.jsp">Home</a></li>
<li><a href="account.jsp">My Account</a></li>
<li><a href="message.jsp">Messeging</a></li>
</ul>

</body>
</html>