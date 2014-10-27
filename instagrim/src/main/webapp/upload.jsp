<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>instagrimKWLI</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
        <h1>instagrimKWLI ! </h1>
        <h2>Your world in Black and White</h2>
        <nav>
            <ul>
                <li class="nav"><a href="upload.jsp">Upload</a></li>
                <li class="nav"><a href="/instagrimKWLI/Images/majed">Sample Images</a></li>
                <li class="nav"><a href="/instagrimKWLI/TotheUserPics.jsp">Your Images</a></li>
            </ul>
        </nav>
 
        <article>
        
            <h3>File Upload</h3>
            <form method="POST" name="myform" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile" id="imgFile" onChange='Preview()'><br/>
                
                <br/>
                <input type="submit" value="Press"> to upload the file!
            </form>
        <script> 
   function Preview() 
   { 
	var imgFile = document.getElementById("imgFile");
    var pattern = /(\.*.jpg$)|(\.*.png$)|(\.*.jpeg$)|(\.*.gif$)|(\.*.bmp$)/;      
    if(!pattern.test(imgFile.value)) 
    { 
     alert("jpg/jpeg/png/gif/bmp！");  
     imgFile.focus(); 
    } 
    else 
    { 
     
     var path; 
     if(document.all)//IE 
     { 
      imgFile.select(); 
      path = document.selection.createRange().text; 
      document.getElementById("imgPreview").innerHTML=""; 
      document.getElementById("imgPreview").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + path + "\")";//filter 
     } 
     else//FireFox
     { 
      path = URL.createObjectURL(imgFile.files[0]);
      document.getElementById("imgPreview").innerHTML = "<img src='"+path+"'/>"; 
     } 
    } 
   } 
  </script> 

<div id="imgPreview" style='width:200px; height:200px;'> 
                <img src=""/> 
                </div> 
                <br/>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/instagrimKWLI/TotheIndex.jsp">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
