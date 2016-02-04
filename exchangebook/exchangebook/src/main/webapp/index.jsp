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
    if (user != null) {
        pageContext.setAttribute("user", user);
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
} else {
%>
<p>Hello!
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to exchange books!</p>
<%
    }
%>
	
<p>Search a Book</p>
<form action="/search" method="get">
	title<input type="text" name="title"></br>
	author<input type="text" name="author"></br>
	isbn<input type="text" name="isbn"></br>
	<input type="submit" value="begin search">
</form>

<p>Add a Book</p>
<form action="/rest/ds" method="post">
	title<input type="text" name="title"></br>
	author<input type="text" name="author"></br>
	isbn<input type="text" name="isbn"></br>
	<input type="radio" name="action" value="offer">offer</br>
	<input type="radio" name="action" value="demand">demand</br>
	quantity<input type="number" name="quantity" min="1" max="10"></br>
	<input type="submit" value="Add">
</form>
</body>
</html>