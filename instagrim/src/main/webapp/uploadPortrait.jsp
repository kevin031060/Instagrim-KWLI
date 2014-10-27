<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
String state=request.getParameter("id");

if (state=="upload"){	
%>
Please upload your head portrait:
<form method="POST" name="myform" enctype="multipart/form-data" action="Portrait">
                Portrait to upload: <input type="file" name="upfile" ><br/>
                 <input type="submit" value="Press"> to upload your portrait!
</form>
<%
}else{
%>
Please change your head portrait:
<form method="POST" name="myform" enctype="multipart/form-data" action="Portrait">
                Portrait to change: <input type="file" name="upfile" ><br/>
                 <input type="submit" value="Press"> to change your portrait!
</form>
<%
}
%>
</body>
</html>