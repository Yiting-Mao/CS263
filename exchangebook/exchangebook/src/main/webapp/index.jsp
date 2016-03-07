<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.*"%>
<%@ page import="com.google.appengine.api.taskqueue.Queue"%>
<%@ page import="com.google.appengine.api.taskqueue.QueueFactory"%>
<%@ page import="com.google.appengine.api.taskqueue.TaskOptions"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/index.css"/>
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
<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
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
<script>

function validate_required(field)
{
	with (field)
	{
		if (value==null||value=="")
			{return false}
		else {return true}
	}
}
function validate_form_search(thisform)
{
	with (thisform)
	{
		if (validate_required(title)==false&&validate_required(author)==false&&validate_required(isbn)==false)
		{
			alert("Fill in at least one area");
			title.focus();return false
		}
	}
}
function validate_form_add(thisform)
{
	with (thisform)
	{
		with(user){
			if(value==""){
			alert("Please sign in to add books");
			return false
			}
		}
		if(validate_required(title)==false){
			alert("Fill in title");
			title.focus();return false
		}
		if(validate_required(author)==false){
			alert("Fill in author");
			author.focus();return false
		}
		if(validate_required(isbn)==false){
			alert("Fill in isbn");
			isbn.focus();return false
		}
		if(!option[0].checked&&!option[1].checked){
			alert("Check one option");
			return false
		}
		if(validate_required(quantity)==false){
			alert("Choose the quantity");
			quantity.focus();return false
		}

	}
}

</script>

<ul class="navi">
<li><a href="/index.jsp">Home</a></li>
<li><a href="/account.jsp">Account</a></li>
<li><a href="/message.jsp">Messeging</a></li>
</ul><br/>
	
<p>Search a Book</p>
<form action="/search" onsubmit="return validate_form_search(this)" method="get">
	title<input type="text" name="title"><br/>
	author<input type="text" name="author"><br/>
	isbn<input type="text" name="isbn"><br/>
	<input type="submit" value="begin search">
</form>

<p>Add a Book</p>
<form action="/ds" onsubmit="return validate_form_add(this)" method="post">
	title<input type="text" name="title"><br/>
	author<input type="text" name="author"><br/>
	isbn<input type="text" name="isbn"><br/>
	<input type="radio" name="option" value="offer">offer<br/>
	<input type="radio" name="option" value="demand">demand<br/>
	quantity<input type="number" name="quantity" min="1" max="10"><br/>
	<input type="hidden" name="user" value="<%=userID%>">
	<input type="submit" value="Add">
</form>
</div>
</body>
</html>