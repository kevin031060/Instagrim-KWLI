<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<% LoggedIn lg=(LoggedIn) session.getAttribute("LoggedIn");
%>
<meta http-equiv="refresh" content="0;url=/instagrimKWLI/Portraits/<%=lg.getUsername()%>" >
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html>