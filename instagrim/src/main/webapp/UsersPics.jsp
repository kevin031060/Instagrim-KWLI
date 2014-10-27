<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>instagrimKWLI</title>
        <link rel="stylesheet" type="text/css" href="/instagrimKWLI/Styles.css" />
    </head>
    <body>
        <header>
       
        <h1>instagrimKWLI ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        
        <nav>
            <ul>
                <li class="nav"><a href="/instagrimKWLI/upload.jsp">Upload</a></li>
                <li class="nav"><a href="/instagrimKWLI/Images/majed">Sample Images</a></li>
                
            </ul>
        </nav>
 
        <article>
        
            <h1>Your Pics</h1>
        <%
            
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:MM");
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();
                if(p.getSUUID()!=null){
                    
        %>
       
        
     
        <div STYLE="border-style:Double;border-width:2pt;Width:50%; border-color: black">
        <font color="#00dddd" size="1"  face="Comic Sans MS">Uploaded on:</font><br/>
        <font color="#00dddd" size="1"  face="Comic Sans MS"><%=sdf.format(p.getPicadded()) %></font><br/><br>
        <a href="/instagrimKWLI/Image/<%=p.getSUUID()%>" ><img src="/instagrimKWLI/Thumb/<%=p.getSUUID()%>" align=top></a><br/>
        
        <a href="/instagrimKWLI/comment?id=<%=p.getSUUID()%>">Show Comment:</a><br/>
        
        <a href="/instagrimKWLI/addComment.jsp?picid=<%=p.getSUUID()%>">Comment this Picture</a><br/>
        
        <a href="/instagrimKWLI/deleteServlet?id=<%=p.getSUUID()%>">Delete</a><br/>
        </div>
        <%
                    
                }                
            }
            }
        %>
        
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/instagrimKWLI/TotheIndex.jsp">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
