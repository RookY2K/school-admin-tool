<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Login" />
    <jsp:param name="stylesheet" value="index.css" />
</jsp:include>
	<div id="body">
			
		<form id="login" method="post" action="login" >
			<table id=login_table>
				<tr>
					<td id="login-title" colspan="2">UWM - Computer Science Administration</td>
				</tr>
				<% if (request.getParameter("login") != null) { %>
				 <tr>
				 	<td colspan="2"><span style="color:red;">Login Failed, check Username/Password!</span></td>
				 </tr> 
				<% } %>
				<tr>
					<td id="user_name">User Name</td>
					<td class="text_block"><input type="text" name="username"  required /></td>
				</tr>
				<tr>
					<td id="password">Password</td>
					<td class="text_block"><input type="password" name="password"  required /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<input type="submit" id="login_button" value="Log In" />
						<!-- &nbsp;<a href="passwordReset.html">Forgot Password?</a> -->
					</td>
				</tr>			
				<tr>
					<td id="footer" colspan="2">
						For help, please contact help desk at <br />
						<a href="mailto:vamaiuri@uwm.edu?subject=UWMCSA%20Help">vamaiuri@uwm.edu</a><br />
					</td>
				</tr>
			</table>
		</form>
	</div>

	</body>
</html> 