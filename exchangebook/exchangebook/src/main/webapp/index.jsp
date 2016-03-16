<!-- index -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.*"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    <script type="text/javascript" src="./js/index.js"> </script>
</head>

<body>
<div id = "container">
<%
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  UserService userService = UserServiceFactory.getUserService();
  User user = userService.getCurrentUser();
  String userID;
  if (user != null) {
    pageContext.setAttribute("user", user);
    userID=user.getUserId();
    Key ownerKey=KeyFactory.createKey("Owner",userID);
    try{
    	Entity result=datastore.get(ownerKey);
    }
    //direct to fill in user info if it's the first time visit
    catch(EntityNotFoundException e){
          String redirectURL = "/addinfo.jsp?reqURI=/index.jsp";
    	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    	response.setHeader("Location", redirectURL);
    }
   
    %>
    <p>Hello, ${fn:escapeXml(user.nickname)}! (
        <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
    <%
  } else {
  	userID="";
    %>
    <p>Hello!
        <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
        to exchange books!</p>
    <%
  }
%>
<div>
<ul class="navi">
<li><a class="navi" href="/index.jsp" style="background-color: #DEB887">Home</a></li>
<li><a class="navi" href="/myaccount.jsp">Account</a></li>
<li><a class="navi" href="/message.jsp">Messeging</a></li>
</ul><br/>
</div>

<div id="match">
<p>Search a Book</p>
<form id ="searchform">
	title<input type="text" id="title" name="title"><br/>
	author<input type="text" id="author" name="author"><br/>
	isbn<input type="text" id="isbn" name="isbn"><br/>
	<button id ="searchbook"> Search </button>
</form>

<p>Add a Book</p>
<form id="addform">
	title<input type="text" name="title"><br/>
	author<input type="text" name="author"><br/>
	isbn<input type="text" name="isbn"><br/>
	offer<input type="radio" name="option" value="offer"><br/>
	demand<input type="radio" name="option" value="demand"><br/>
	quantity<input type="number" name="quantity" min="1" max="10"><br/>
	<input type="hidden" id="user" name="user" value="<%=userID%>">
	<button id ="addbook"> Add </button>
</form>
</div>
</div>
</body>
</html>