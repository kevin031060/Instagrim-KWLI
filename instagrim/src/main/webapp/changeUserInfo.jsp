
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<title>Insert title here</title>
</head>
<body>
<% LoggedIn lg=(LoggedIn) session.getAttribute("LoggedIn");
  
%>
<center>
<p>Username:<%=lg.getUsername() %></p>
<p>Name:<%=lg.getFirstname() %> <%=lg.getLastname() %></p>
<p>E-mail:<%=lg.getEmail() %> </p>
<input type=button value="ChangePassword" onclick="changepass()"><br/>
<form id="pw" method="post" action="ChangePassword"></form>
<script type="text/javascript" src=checkuser.js></script>
<script>
function changepass(){
	document.getElementById("pw").innerHTML="<li><input type='password' name='password' id='pwd' onChange='pwLength()'>input your new password</li> \n"
	+ "<li><input type='password' name='password1' id='pwd1' onChange='showsubmit()'>input again(Tab to continue)</li> \n"
	+ "<input type='hidden' name='username' value='<%=lg.getUsername()%>'><br/> \n"
	+ "<div id='submitt'></div>";
}
function showsubmit(){
	if (document.getElementById("pwd").value==document.getElementById("pwd1").value && document.getElementById("pwd").value.length>2)
		document.getElementById("submitt").innerHTML="<input type='submit' value='submit'>";
		else
			document.getElementById("submitt").innerHTML="Please ensure your password input";
}

</script>

</center>

<footer>
            <ul>
                <li class="footer"><a href="/instagrimKWLI/TotheIndex.jsp">Home</a></li>
            </ul>
        </footer>
</body>
</html>