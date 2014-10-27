<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form method="post" action="comment">
<input type=text name="commen">
<input type=hidden name="id" value="<%=request.getParameter("picid")%>">
<input type=submit value="Comment">
</form>
<img src="/instagrimKWLI/Thumb/<%=request.getParameter("picid")%>">
</body>
</html>