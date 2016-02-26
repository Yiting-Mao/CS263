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
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js">
	</script>
</head>
<body>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
	String userID;
    pageContext.setAttribute("user", user);
	userID=user.getUserId();
	System.out.println(userID);
    String redirectURL = "/ds/owner/"+userID;
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	response.setHeader("Location", redirectURL);
	// servletResponse.sendRedirect(redirectURL);
%>


<script>
	
</script>
</body>
</html>