$(document).ready(function(){  
  $('#searchbook').click(function(){
    if($('#title').val()==''&&$('#author').val()==''&&$('#isbn').val()==''){
      alert("You must fill in at least one area");
      $('#title').focus();
    } else {
      // if don't use validate, can't return data, don't know why
      // refresh the page, display potential matches
      $('#searchform').validate({ 
        submitHandler: function() {
          $.getJSON('/ds/search/book?isbn='+$('#isbn').val()+'&title='+$('#title').val()+'&author='+$('#author').val(), function(data) {
            $('#match').empty();
            $('#match').append('<br/>');
            if(data.length) {
              var book = [];
              book.push("<tr><th>Potential Matches:</th><th>&nbsp</th><th>&nbsp</th></tr>");
              book.push("<tr><th>Title</th><th>Author</th><th>ISBN</th></tr>");  
              $.each(data, function(index, element) {
                book.push('<tr>'
                + '<td><a href="/book.jsp?isbn=' + element.isbn + '"> ' + element.title + "</a></td>"
                + "<td>" + element.author + "</td>"
                + "<td>" + element.isbn + "</td>"
                + "</tr>");
              });
            } else {
              $('#match').append('<p>No books found</p>');
            }
            $("<table/>", {
             html: book.join("")
            }).appendTo('#match'); 
          });         
        }
      });
    } 
  });

  $('#addbook').click(function(){
    if($('#user').val()=='') {
      alert('You need to login first');
    } else {
      $('#addform').validate({
        rules: {
          title:"required",
          author:"required",
          isbn:"required",
          option:"required",
          quantity:"required"
        },
        
        messages: {
          title:"required",
          author:"required",
          isbn:"required",
          option:"select one",
          quantity:"choose quantity"
        },
        
        //enqueue a book
        submitHandler: function() {
          $.ajax({
              url: '/ds/enqueue/addbook',
              type: 'POST',
              data: $('#addform').serialize(),
              success: function(result) {
                window.location='/book.jsp?isbn='+ $('#addform input[name=isbn]').val();
              }
          });         
        }
      });    
    }
  }); 
});