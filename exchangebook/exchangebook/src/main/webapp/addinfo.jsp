<!-- Used to add account info -->
<!-- Use jquery to post to the rest service, when it's done, redirects to the account/index page -->

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
</head>
<body>
    <div id = "container">
	<%
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		pageContext.setAttribute("userID",user.getUserId());
		pageContext.setAttribute("reqURI",request.getParameter("reqURI"));
	%>
	<p>Please fill in every blanks.</p>
	<script>
		$(document).ready(function(){
		    $("button").click(function(){
			if($('#name').val()==''){
				alert("Fill in name");
				$('#name').focus();
			}
			else if($('#location').val()==''){
				alert("Fill in your location");
				$('#location').focus();
			}
			else{
				var name=$('#name').val();
				var location=$('#location').val();
				var userID=$('#userID').val();
				var reqURI=$('#reqURI').val();
		        $.post("/ds/owner/"+userID+"/addinfo",
		        {
		          name: name,
		          location: location
		        },
				function(){
				     window.location =reqURI ;
				});
			}
		
		    });
		});

	</script>

	Your name: <input type="text" id="name"><br/>
	Your location: <input type="text" id="location"><br/>
	<input type="hidden" id="userID" value="${fn:escapeXml(userID)}">
	<input type="hidden" id="reqURI" value="${fn:escapeXml(reqURI)}">
	<button>Submit</button>
</body>
</html>