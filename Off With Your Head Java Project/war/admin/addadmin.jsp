<%@ page import="edu.uwm.owyh.model.Person" %>
<%@ page import="java.util.List" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Add Admin" />
    <jsp:param name="stylesheet" value="addAdmin.css" />
</jsp:include>
	<div id="body">
		<%
			Boolean isAddAdmin = (Boolean)session.getAttribute("isAddAdmin");
			if(isAddAdmin == null){
				response.sendRedirect(request.getContextPath() + "/");
			}
		%>

		<form id="addAdmin" action="/admin/addAdmin" method="post">
			<table id="addAdmin-table">
			<tr>
				<td id="addAdmin-title" colspan="2">UWM - Computer Science Administration</td>
			</tr>
			<% 
			if (request.getAttribute("addNewUser") != null) {
				boolean addNewUser = (Boolean) request.getAttribute("addNewUser");
				if (!addNewUser) {
					List<String> errors = (List<String>)request.getAttribute("errors");
					for(String error:errors){
			%>
			<tr>
				<td colspan="2" style="color:red;"><%=error%></td>		
			</tr>
			<% 
					}
				}
			} 
			%>			
			<tr>
				<td class="cell">Email:</td>
				<td class="cell"><input type ="text" name="email" id="email" required />
			</tr>
			<tr>
				<td class="cell">Password: </td>
				<td class="cell"><input type = "text" name="password" id="password" required />
			</tr>
			<tr>
				<td class="cell">AccessLevel:</td>
				<td class="cell"> 
					<select name="accesslevel">
					  <option value="<% out.print(Person.AccessLevel.ADMIN.getVal()); %>" selected>ADMIN</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="cell" colspan="2"><input type="submit" value="Create New User" /></td>
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