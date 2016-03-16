$(document).ready(function(){  
  if($('#userID').val() == $('#targetID').val()) {
     window.location = '/myaccount.jsp';
  }
  $.getJSON('/ds/owner/'+$('#targetID').val(), function(data) {
    if(data.userID) {
      $('#personalinfo').append('<br/><t3>Personal Info:</t3>');
      $('#personalinfo').append('<br/>Name:' + data.name + '&nbsp&nbsp Location:' + data.location);
      $('#personalinfo').append('<button id = "sendmessage">Messaging</button><hr/>');
      var book=[]
      if(data.bookOffer.length) {
        book.push("<tr><th>Books Offer:</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
        book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th><th>Quantity</th></tr>");  
        $.each(data.bookOffer, function(index, element) {
          book.push('<tr>'
          + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
          + "<td>" + element.author + "</td>"
          + "<td>" + element.isbn + "</td>"
          + "<td>" + element.num +"</td>"
          + "</tr>");
        });
      } else {
        $('#book').append('<p>No books offer</p>');
      }
      $("<table/>", {
       html: book.join("")
      }).appendTo('#book');  
      book = [];
      $('#book').append('<hr/>');
      
      if(data.bookDemand.length) {
        book.push("<tr><th>Books Demand:</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
        book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th><th>Quantity</th></tr>");
        $.each(data.bookDemand, function(index, element) {
          book.push('<tr>'
          + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
          + "<td>" + element.author + "</td>"
          + "<td>" + element.isbn + "</td>"
          + "<td>" + element.num +"</td>"
          + "</tr>");
        });
      } else {
         $('#book').append('<p>No books demand</p>');
      }
      $("<table/>", {
       html: book.join("")
      }).appendTo('#book');  
    } else {
      $('#book').append('<p>No such user</p>');
    }    
  });
   
  $('#personalinfo').on('click', '#sendmessage', function() {
    var receiver = $('#targetID').val();
    window.location = '/addmessage.jsp?receiver=' + receiver + '&reqURI=/otheraccount.jsp?targetID='+ receiver;
  });  
});