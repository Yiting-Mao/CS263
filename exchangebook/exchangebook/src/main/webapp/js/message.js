$(document).ready(function(){  
  $.getJSON('/ds/message/' + $('#userID').val() + '/received', function(data) {
     $('#messagereceived').append('<p> Message Received: </p>');

     $.each(data, function(index, element) {
      $('#messagereceived').append('<p class="'+ element.messageID+ '">'
       + 'Title:' + element.title 
       + '<button class = "delete" data-messageID="'+ element.messageID+ '">Delete</button>'
       + '<button class = "reply" data-sender="'+element.senderID+'">Reply</button> <br/>'
       + 'From: <a href = "/otheraccount?targetID=' + element.sender + '">' + element.sender + '</a> &nbsp'
       + 'Time:' +  element.date +'<br/>'
       + 'Body:<br/><pre>'+ element.body + '</pre>'
       + '<hr/></p>' );
     });    
  });
  
  
  $('#messagereceived').on('click', '.delete', function() {      
     var messageID=$(this).data("messageID");
     var userID=$('#userID').val();
     $.ajax({
         url: '/ds/message/'+userID+'/'+messageID,
         type: 'DELETE',
         success: function(result) {
           $('p.'+messageID).hide();
         }
     });
     
  });
  $('#messagereceived').on('click', '.reply', function() {
     var receiver=$(this).data("sender");
     var reqURI='/message.jsp';
     window.location='/addmessage.jsp?receiver='+receiver+'reqURI='+reqURI;
  });  
  
});