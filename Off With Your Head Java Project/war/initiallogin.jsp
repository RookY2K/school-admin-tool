<%@ page import="edu.uwm.owyh.model.Auth" %>
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Initial Login" />
    <jsp:param name="stylesheet" value="initiallogin.css" />
</jsp:include>
	<%
		Boolean isKey = (Boolean)request.getAttribute("isKey");
		Boolean noUsers = (Boolean)Auth.getSessionVariable(request, "noUsers");
		
		if(isKey == null && (noUsers == null || !noUsers.booleanValue())){
			response.sendRedirect(request.getContextPath() + "/");
		}else{
			Auth.removeSessionVariable(request, "noUsers");
		}
	%>
	<div id="body">
			
		<form id="login" method="post" action="initiallogin" >
			<table id=login_table>
				<tr>
					<td id="login-title" colspan="2">UWM - Computer Science Administration</td>
				</tr>
				<tr>
					<td id="app_key">Application Key</td>
					<td class="text_block"><input type="text" name="appkey"  required /></td>
				</tr>
				<%
					if(isKey != null && !isKey.booleanValue()){
				%>
				<tr>
					<td>&nbsp;</td>
					<td style="color:red;">That key was not valid!</td>
				</tr>
				<%
					}
				%>
				<tr>
					<td>&nbsp;</td>
					<td>
						<input type="submit" id="submit_button" value="Submit" />
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