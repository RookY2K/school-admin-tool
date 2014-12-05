<%@ page import="java.util.List" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Forgot Password" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="index.css" />
</jsp:include>
<% List<String> errors = (List<String>) request.getAttribute("errors");
List<String> messages = (List<String>) request.getAttribute("messages");
%>
		
<form id="login" method="post" action="/forgotpassword" >
	<table id=login_table>
		<tr>
			<td id="login-title" colspan="2"><span id="site-title">UWM - Computer Science Administration</span></td>
		</tr>
		<tr><td><div style="margin: 10px;"></div></td></tr>
		<% if (errors != null) { 
			for (String error : errors) {
		%>
		 <tr>
		 	<td colspan="2"><div style="margin: 0 10px 10px 10px;"><span class="error-message"><%=error %></div></span>
		 </td>
		 </tr> 
		<% } } %>
		<% if (messages != null) { 
			for (String message : messages) {
		%>
		 <tr>
		 	<td colspan="2"><div style="margin: 0 10px 10px 10px;"><span class="good-message"><%=message %></div></span>
		 </td>
		 </tr> 
		<% } } %>
		<tr>
			<td id="user_name">User Name</td>
			<td class="text_block"><input type="text" name="email" placeholder="somename@uwm.edu" required /></td>
		</tr>
		<tr>
			<td id="footer" colspan="2">
			<input type="submit" class="submit" id="login_button" value="Request New Password" /> <br /><br />
			<a href="/">Go Back to Login</a><br /><br />
			For help, please contact help desk at <br />
			<a href="mailto:vamaiuri@uwm.edu?subject=UWMCSA%20Help">vamaiuri@uwm.edu</a><br />
			</td>
		</tr>
	</table>
</form>

<br class="clear" />
	
<jsp:include page="/WEB-INF/templates/footer.jsp" />