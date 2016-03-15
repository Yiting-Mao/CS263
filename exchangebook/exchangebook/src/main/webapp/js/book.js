$(document).ready(function(){  
  $.getJSON('/ds/book/'+$('#isbn').val(), function(data) {
     $('#bookinfo').append('<p>Title: ' + data.title + '</p>');
     $('#bookinfo').append('<p>Author: ' + data.author+ '</p>');
     $('#bookinfo').append('<p>ISBN: ' + data.isbn+ '</p>');
     var owner=[]
     owner.push("<tr><th>People Offer:</th><th>&nbsp</th><th>&nbsp</th></tr>");
     owner.push("<tr><th>Name</th><th>Location</th><th>Quantity</th></tr>");  
     $.each(data.peopleOffer, function(index, element) {
       owner.push('<tr>'
       + '<td><a href="/otheraccount.jsp?targetID=' + element.userID + '"> ' + element.name + "</a></td>"
       + "<td>" + element.location + "</td>"
       + "<td>" + element.num +"</td>"
       + "</tr>");
     });
     owner.push("<tr><th>&nbsp</th><th>&nbsp</th><th>&nbsp</th></tr>");
     owner.push("<tr><th>People Demand:</th><th>&nbsp</th><th>&nbsp</th></tr>");
    owner.push("<tr><th>Name</th><th>Location</th><th>Quantity</th></tr>");  
     $.each(data.peopleDemand, function(index, element) {
       owner.push('<tr>'
       + '<td><a href="/otheraccount.jsp?targetID=' + element.userID + '"> ' + element.name + "</a></td>"
       + "<td>" + element.location + "</td>"
       + "<td>" + element.num +"</td>"
       + "</tr>");
     });
     $("<table/>", {
       html: owner.join("")
     }).appendTo('#owner');  
  });
   
});