<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List"%>
<%! @SuppressWarnings("unchecked")%>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Add User" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="/admin/addClient">Add New User</a></li>
	      <li><a class="nav-link" href="/admin/addContactInfo">Add User Contact</a></li>
		</ul>
	</div>

	<div id="body">
			
			<% 
		String taAccess = Integer.toString(AccessLevel.TA.getVal());
		String instructorAccess = Integer.toString(AccessLevel.INSTRUCTOR.getVal());
		String adminAccess = Integer.toString(AccessLevel.ADMIN.getVal());
		
		String username = "";
		if (request.getAttribute("badUserName") != null)
		username = (String) request.getAttribute("badUserName");
		String accesslevel = "";
		if (request.getAttribute("badAcesslevel") != null)
		accesslevel = (String) request.getAttribute("badAcesslevel");
%>
		
		<% List<String> errors = (List<String>)request.getAttribute("errors");
		 if (errors != null) {
			for(String error : errors) { %>
		
			<span class="error-message"><%=error %><br /></span>
		<% } }%>

		<form action="/admin/addClient" method="post">
			<fieldset>
				<legend> Add New User Account</legend>
				<table>
					<tr>
						<td class="cell"><label class="field" for="email">Email: </label></td>
						<td class="cell"><input type = "email" name="email" id="email" placeholder="somename@uwm.edu" value="<%=username %>" pattern="^\w+@uwm.edu$" required /></td>
					</tr>
					<tr>
						<td class="cell"><label class="field" for="email">Password: </label></td>
						<td class="cell"><input type = "password" name="password" id="password" required /></td>
					</tr>
					<tr>
						<td class="cell"><label class="field" for="email">Verify Password: </label></td>
						<td class="cell"><input type = "password" name="verifypassword" id="password" required /></td>
					</tr>

					<tr>
						<td class="cell"><label class="field" for="accesslevel">Role: </label></td>
						<td class="cell"> 
							<select name="accesslevel" required>
							  <option value="">Please Select</option>
							  <option value="<%=taAccess%>" <%if(taAccess.equals(accesslevel)){%>selected<%}%>>TA</option>
							  <option value="<%=instructorAccess%>"<%if(instructorAccess.equals(accesslevel)){%>selected<%}%>>INSTRUCTOR</option>
							  <option value="<%=adminAccess%>"<%if(adminAccess.equals(accesslevel)){%>selected<%}%>>ADMIN</option>
							</select>
						</td>
					</tr>

					<tr>
					<td class="cell" colspan="2"><input type="submit" value="Add New User" /></td>
					</tr>

				</table>
			</fieldset>
		</form>
		
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />