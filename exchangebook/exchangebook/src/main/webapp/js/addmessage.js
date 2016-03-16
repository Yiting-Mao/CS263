$(document).ready(function(){
  $("button").click(function(){
  	if($('#title').val()==''){
  		alert("Fill in title");
  		$('#title').focus();
  	} else {
  		var title=$('#title').val();
  		var body=$('#body').val();
  		var sender=$('#sender').val();
      var receiver = $('#receiver').val();
      var deleted=false;
  		var reqURI=$('#reqURI').val();
      
      //add message to task queue
      $.post("/ds/enqueue/addmessage",
      {
        userID: sender,
        receiver: receiver,
	      title: title,
        body: body,
        delete: deleted
      }, function(){
  		     window.location =reqURI ;
  		});
  	}
  });
});
