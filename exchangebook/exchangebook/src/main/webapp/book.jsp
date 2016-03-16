<!-- display book's info, and people who offer/demand the book -->

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
	 <script type="text/javascript" src="./js/book.js"></script>
</head>
<body>
    <div id = "container">
	 <%
     String isbn = request.getParameter("isbn");
     pageContext.setAttribute("isbn", isbn);
     UserService userService = UserServiceFactory.getUserService();
     User user = userService.getCurrentUser();
	 	 String userID=null;
		 if(user!=null){
			 pageContext.setAttribute("user", user);
       %>
       <p>Hello, ${fn:escapeXml(user.nickname)}! (
           <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)
       </p>
       <%
		 } else {
       %>
       <p>Hello!
           <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
           to exchange books!
       </p>
       <%
		 }
	 %>
   <input type="hidden" id="isbn" value="${fn:escapeXml(isbn)}">

	 <ul class="navi">
	 <li><a class="navi" href="/index.jsp">Home</a></li>
	 <li><a class="navi" href="/myaccount.jsp">Account</a></li>
	 <li><a class="navi" href="/message.jsp">Messeging</a></li>
	 </ul><br/>
   
   <div id = "bookinfo"></div> 
     
   <div id = "owner"></div>
</div>
</body>

</html>

