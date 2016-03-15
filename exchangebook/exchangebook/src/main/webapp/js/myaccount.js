$(document).ready(function(){  
  $.getJSON('/ds/owner/'+$('#userID').val(), function(data) {
     $('#personalinfo').append('<p> Name: ' + data.name + '</p>');
     $('#personalinfo').append('<p>Location: ' + data.location+ '</p>');
    $('#personalinfo').append('<button id = "updateinfo">updateinfo</button>');
     var book=[]
     book.push("<tr><th>Books Offer:</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
     book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th><th>Quantity</th><th>&nbsp</th></tr>");  
     $.each(data.bookOffer, function(index, element) {
       book.push('<tr class="'+element.isbn+'">'
       + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
       + "<td>" + element.author + "</td>"
       + "<td>" + element.isbn + "</td>"
       + '<td><input id="' + element.isbn + '" type="number" min="0" max="' + element.num + '"value="'+element.num+'"></td>'
       + '<td><button type="button" class="updatebook" data-option="offer" data-isbn="' + element.isbn
       + '">Update</button></td>'
       + "</tr>");
     });
     book.push("<tr><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
     book.push("<tr><th>Books Demand:</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
     book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th><th>Quantity</th><th>&nbsp</th></tr>");
     $.each(data.bookDemand, function(index, element) {
       book.push('<tr class="'+element.isbn+'">'
       + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
       + "<td>" + element.author + "</td>"
       + "<td>" + element.isbn + "</td>"
       + '<td><input id="' + element.isbn + '" type="number" min="0" max="' + element.num + '"value="'+element.num+'"></td>'
       + '<td><button type="button" class="updatebook" data-option="demand" data-isbn="' + element.isbn
       + '">Update</button></td>'
       + "</tr>");
     });
     $("<table/>", {
       html: book.join("")
     }).appendTo('#book');  
  });
  
  
  $('#book').on('click', '.updatebook', function() {      
     var isbn=$(this).data("isbn");
     var option=$(this).data("option");
     var src='#'+isbn;
     var max=$(src).attr('max');
     var quantity=$(src).val();
     if (quantity < max) {
       var userID=$('#userID').val();
       $.ajax({
           url: '/ds/owner/'+userID+'/'+isbn,
           type: 'DELETE',
           data: {
             num: max-quantity,
             option: option
           },
           success: function(result) {
             if(quantity == '0')$('tr.'+isbn).hide();
             alert("Your change has been updated");
           }
       });
     } else {
       alert("You didn't make any change");
     }   
  });
  $('#personalinfo').on('click', '#updateinfo', function() {
     window.location = '/addinfo.jsp?reqURI=/myaccount.jsp';
  });  
  
});