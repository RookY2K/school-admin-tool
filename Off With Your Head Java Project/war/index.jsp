<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Login" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="index.css" />
</jsp:include>
		
<form id="login" method="post" action="login" >
	<table id="login_table">
		<tr>
			<td id="login-title" colspan="2"><span id="site-title">UWM - Computer Science Administration</span></td>
		</tr>
		<tr><td><div style="margin: 10px;"></div></td></tr>
		<% if (request.getParameter("login") != null) { %>
		 <tr>
		 	<td colspan="2"><div style="margin: 0 10px 10px 10px;"><span class="error-message" style="text-align:center; width:100%;margin-bottom: 10px;">
		 	Login Failed, check Username/Password!</div></span>
		 </td>
		 </tr> 
		<% } %>
		<tr>
			<td class="text_info">User Name</td>
			<td class="text_block"><input type="text" name="username" id="email" placeholder="somename@uwm.edu" required /></td>
		</tr>
		<tr>
			<td class="text_info">Password</td>
			<td class="text_block"><input type="password" name="password" id="password" required /></td>
		</tr>
		<tr>
			<td id="footer" colspan="2">
			<input type="submit" class="submit" id="login_button" value="Log In" /> <br /><br />
			<a href="/forgotpassword">Forgot Password?</a><br /><br />
			For help, please contact help desk at <br />
			<a href="mailto:vamaiuri@uwm.edu?subject=UWMCSA%20Help">vamaiuri@uwm.edu</a><br />
			</td>
		</tr>
	</table>
</form>

<br class="clear" />
	
<jsp:include page="/WEB-INF/templates/footer.jsp" />