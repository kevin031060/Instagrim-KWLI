
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="refresh" content="1;url=/instagrimKWLI/TotheIndex.jsp" >
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<title>Insert title here</title>
</head>
<body>
<% 
LoggedIn lg=(LoggedIn) session.getAttribute("LoggedIn");
%>
<center>
<h1> Welcom to instagrimKWLI,<%=lg.getFirstname() %> </h1>

</center>
</body>
</html>