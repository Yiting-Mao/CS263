window.onload = function(){
  document.getElementById('receive').click();
}

$(document).ready(function(){  

  //get all messages received
  $('#receive').click(function(){
    $(this).css('background-color','#DEB887');
    $('#sent').css('background-color','#bebebe');
    $('#messagereceived').empty();
    $('#messagesent').empty();
    $.getJSON('/ds/message/' + $('#userID').val() + '/received', function(data) {
      if(!data.length){
        $('#messagereceived').append('<p>No messages</p>');
      }
       $.each(data, function(index, element) {
        $('#messagereceived').append('<div class="'+ element.messageID+ '">'
         +'<p>'
         + 'Title:' + element.title 
         + '<button class = "delete" data-message="'+element.messageID+'">Delete</button>'
         + '<button class = "reply" data-sender="'+element.senderID+'" data-title="'+element.title+'">Reply</button> <br/>'
         + 'From: <a href = "/otheraccount.jsp?targetID=' + element.senderID + '">' + element.senderID + '</a> &nbsp'
         + 'Time:' +  element.date +'<br/>'
         + 'Body:<br/><pre>'+ element.body + '</pre>'
         + '<hr/></p></div>' );
       });    
    });
  });
  
  //get all messages sent
  $('#sent').click(function(){
    $(this).css('background-color','#DEB887');
    $('#receive').css('background-color','#bebebe');
    $('#messagereceived').empty();
    $('#messagesent').empty();
    $.getJSON('/ds/message/' + $('#userID').val() + '/sent', function(data) {
      if(!data.length){
        $('#messagesent').append('<p>No messages</p>');
      } 
       $.each(data, function(index, element) {
        
        $('#messagesent').append('<div class="'+ element.messageID+ '">'
         +'<p>'
         + 'Title:' + element.title 
         + '<button class = "delete" data-message="'+element.messageID+'">Delete</button><br/>'
         + 'To: <a href = "/otheraccount.jsp?targetID=' + element.receiverID + '">' + element.receiverID + '</a> &nbsp'
         + 'Time:' +  element.date +'<br/>'
         + 'Body:<br/><pre>'+ element.body + '</pre>'
         + '<hr/></p></div>' );
       });    
    });
  });
  
  //delete received message
  $('#messagereceived').on('click', '.delete', function() {      
     var messageID=$(this).data("message");
     var userID=$('#userID').val();
     $.ajax({
         url: '/ds/message/'+userID+'/'+messageID,
         type: 'DELETE',
         success: function(result) {
           $('div.'+messageID).hide();
         }
     });
     
  });
  
  //delete sent message
  $('#messagesent').on('click', '.delete', function() {      
     var messageID=$(this).data("message");
     var userID=$('#userID').val();
     $.ajax({
         url: '/ds/message/'+userID+'/'+messageID,
         type: 'DELETE',
         success: function(result) {
           $('div.'+messageID).hide();
         }
     });
  });
  
  //reply a message
  $('#messagereceived').on('click', '.reply', function() {
     var receiver=$(this).data("sender");
     var title='Re:'+ $(this).data("title");
     var reqURI='/message.jsp';
     window.location='/addmessage.jsp?receiver='+receiver+'&reqURI='+reqURI+'&title='+title;
  });  
  
});