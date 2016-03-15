$(document).ready(function(){  
  if($('#userID').val() == $('#targetID').val()) {
     window.location = '/myaccount.jsp';
  }
  $.getJSON('/ds/owner/'+$('#targetID').val(), function(data) {
     $('#personalinfo').append('<p> Name: ' + data.name + '</p>');
     $('#personalinfo').append('<p>Location: ' + data.location+ '</p>');
     $('#personalinfo').append('<button id = "sendmessage">send message</button>');
     var book=[]
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
     book.push("<tr><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
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
     $("<table/>", {
       html: book.join("")
     }).appendTo('#book');  
  });
   
  $('#personalinfo').on('click', '#sendmessage', function() {
    var receiver = $('#targetID');
    window.location = '/addmessage.jsp?receiver=' + receiver + '&reqURI=/otheraccount.jsp?targetID='+ receiver;
  });  
});