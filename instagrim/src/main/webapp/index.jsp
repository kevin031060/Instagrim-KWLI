<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>instagrimKWLI</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1>instagrimKWLI ! </h1>
            <h2>Your world in Black and White</h2>
        </header>
     
     
     <%  
     LoggedIn lg=(LoggedIn) session.getAttribute("LoggedIn");    
     if (lg!=null && lg.getlogedin()){
    	   
            java.util.LinkedList<Pic> lsPors = (java.util.LinkedList<Pic>) request.getAttribute("Pors");
            if (lsPors == null) {
        %>
        <p>Haven't uploaded your portrait yet! <a href="/instagrimKWLI/uploadPortrait.jsp?id='upload'">Upload</a></p>
        
        <%
        } else {
        
            Iterator<Pic> iterator;
            iterator = lsPors.iterator();
         
                Pic p = (Pic) iterator.next();
                if(p.getSUUID()!=null){
                	%>
                	<a href="instagrimKWLI/uploadPortrait.jsp?id='change'" ><img src="/instagrimKWLI/PThumb/<%=p.getSUUID()%>"></a><br/>
 
                    <%     
                            }                
                        }
                    %>
             
     <a href="/instagrimKWLI/changeUserInfo.jsp">Your information</a>
     <%
     } 
     %>
        <nav>
            <ul>       
                    <%
                    if (lg != null) {
                        String UserName = lg.getUsername();
                        if (lg.getlogedin()) {
                %>

            <li><a href="/instagrimKWLI/Images/<%=lg.getUsername()%>">Your Images</a></li>
            <li><a href="/instagrimKWLI/Logout">Log Out</a></li>
                <%}else{
                	%>
                	<li><a href="register.jsp">Register</a></li>
            <li><a href="login.jsp">Login</a></li>
                <%
                }
                        }else{
                            %>
             <li><a href="register.jsp">Register</a></li>
            <li><a href="login.jsp">Login</a></li>
            <%
                                    
                               
                }%>
            </ul>
        </nav>
        <footer>
            <ul>
                <li class="footer"><a href="/instagrimKWLI/TotheIndex.jsp">Home</a></li>    
                <li>&COPY; Andy C</li>
                
            </ul>
        </footer>
    </body>
</html>
