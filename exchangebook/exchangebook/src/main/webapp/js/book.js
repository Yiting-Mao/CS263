$(document).ready(function(){  
  $.getJSON('/ds/book/'+$('#isbn').val(), function(data) {
    $('#bookinfo').append('<p>Title: ' + data.title + '</p>');
    $('#bookinfo').append('<p>Author: ' + data.author+ '</p>');
    $('#bookinfo').append('<p>ISBN: ' + data.isbn+ '</p>');
    $('#bookinfo').append('<hr/>');
     //add people offering the book
    var owner=[]
    if (data.peopleOffer.length) {
      owner.push("<tr><th>People Offer:</th><th>&nbsp</th><th>&nbsp</th></tr>");
      owner.push("<tr><th>Name</th><th>Location</th><th>Quantity</th></tr>");  
      $.each(data.peopleOffer, function(index, element) {
        owner.push('<tr>'
        + '<td><a href="/otheraccount.jsp?targetID=' + element.userID + '"> ' + element.name + "</a></td>"
        + "<td>" + element.location + "</td>"
        + "<td>" + element.num +"</td>"
        + "</tr>");
      });
    } else {
      $('#owner').append("<p>No one offers</p>");
    }
    $("<table/>", {
     html: owner.join("")
    }).appendTo('#owner');  
    $('#owner').append('<hr/>');
    
    //add people demanding the book
    owner=[];
    if(data.peopleDemand.length) {
      owner.push("<tr><th>People Demand:</th><th>&nbsp</th><th>&nbsp</th></tr>");
      owner.push("<tr><th>Name</th><th>Location</th><th>Quantity</th></tr>");  
      $.each(data.peopleDemand, function(index, element) {
        owner.push('<tr>'
        + '<td><a href="/otheraccount.jsp?targetID=' + element.userID + '"> ' + element.name + "</a></td>"
        + "<td>" + element.location + "</td>"
        + "<td>" + element.num +"</td>"
        + "</tr>");
      });
    } else {
      $('#owner').append("<p>No one demands</p>");
    }
    $("<table/>", {
     html: owner.join("")
    }).appendTo('#owner');  
  });   
});