$(document).ready(function(){  
  $.getJSON('/ds/owner/'+$('#userID').val(), function(data) {
    $('#personalinfo').append('<br/><t3>Personal Info:</t3>');
    $('#personalinfo').append('<br/>Name:' + data.name + '&nbsp&nbsp Location:' + data.location);
    $('#personalinfo').append('<button id = "updateinfo"> updateinfo </button> <hr/>');
    
    var book=[]
    if(data.bookOffer.length) {
      book.push("<tr><th>Books Offer:</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
      book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th><th>Quantity</th><th>&nbsp</th></tr>");  
      $.each(data.bookOffer, function(index, element) {
       book.push('<tr class="'+element.isbn+'">'
       + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
       + "<td>" + element.author + "</td>"
       + "<td>" + element.isbn + "</td>"
       + '<td><input id="' + element.isbn + '" type="number" min="0" value="'+element.num+'"></td>'
       + '<td><button type="button" class="updatebook" data-option="offer" data-isbn="' + element.isbn
       + '">Update</button></td>'
       + "</tr>");
      });
    } else {
      $('#book').append('<p>No books offer</p>');
    }
    
    $("<table/>", {
     html: book.join("")
    }).appendTo('#book');  
    book=[]
    $('#book').append('<hr/>');
    
    if(data.bookDemand.length){
      book.push("<tr><th>Books Demand:</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
      book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th><th>Quantity</th><th>&nbsp</th></tr>");
      $.each(data.bookDemand, function(index, element) {
       book.push('<tr class="'+element.isbn+'">'
       + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
       + "<td>" + element.author + "</td>"
       + "<td>" + element.isbn + "</td>"
       + '<td><input id="' + element.isbn + '" type="number" min="0" value="'+element.num+'"></td>'
       + '<td><button type="button" class="updatebook" data-option="demand" data-isbn="' + element.isbn
       + '">Update</button></td>'
       + "</tr>");
      });
    } else {
       $('#book').append('<p>No books Demand</p>');
    }
    $("<table/>", {
     html: book.join("")
    }).appendTo('#book');  
   
  });
  
  
  $('#book').on('click', '.updatebook', function() {      
     var isbn=$(this).data("isbn");
     var option=$(this).data("option");
     var src='#'+isbn;
     var quantity=$(src).val();
     
     //delete or update the book
     if (quantity == '0') {
       var userID=$('#userID').val();
       $.ajax({
           url: '/ds/owner/'+userID+'/'+option+'/'+isbn,
           type: 'DELETE',
           success: function(result) {
             $('tr.'+isbn).hide();
           }
       });
     } else {
       var userID=$('#userID').val();
       $.ajax({
           url: '/ds/owner/'+userID+'/'+option+'/'+isbn,
           type: 'PUT',
           data: {
             num: quantity
           },
           success: function(result) {
             alert("Your change has been updated");
           }
       });
     }   
  });
  $('#personalinfo').on('click', '#updateinfo', function() {
     window.location = '/addinfo.jsp?reqURI=/myaccount.jsp';
  });  
  
});