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
 <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
</head>
<body>
	<%
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		pageContext.setAttribute("sender",user.getUserId());
		pageContext.setAttribute("receiver",request.getParameter("receiver"));
    pageContext.setAttribute("reqURI",request.getParameter("reqURI"));
	%>
	<script>
		$(document).ready(function(){
		    $("button").click(function(){
			if($('#title').val()==''){
				alert("Fill in title");
				$('#title').focus();
			}
			else{
				var title=$('#title').val();
				var body=$('#body').val();
				var sender=$('#sender').val();
        var receiver = $('#receiver').val();
				var reqURI=$('#reqURI').val();
		        $.post("/ds/message",
		        {
		          sender: sender,
		          receiver: receiver,
				      title: title,
              body: body
		        },
				function(){
				     window.location =reqURI ;
				});
			}
		
		    });
		});

	</script>
  <input type="hidden" id="receiver" value="${fn:escapeXml(receiver)}">
	Title: <input type="text" id="title" size="100"></br>
  Body: </br>
  <textarea id="body" rows="30" cols="100"></textarea></br>
	<input type="hidden" id="sender" value="${fn:escapeXml(sender)}">
	<input type="hidden" id="reqURI" value="${fn:escapeXml(reqURI)}">
	<button>Send</button>
</body>
</html>