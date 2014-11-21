<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@page import="java.util.List"%>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="users.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />
<%
	String userName = "";
	WrapperObject self = (WrapperObject)Auth.getSessionVariable(request, "user");
	if(self != null ){
		userName = (String)self.getProperty("username");
	}
	
	List<WrapperObject> users = (List<WrapperObject>) request.getAttribute("users");
	
	WrapperObject.AccessLevel userAccess = (WrapperObject.AccessLevel) self.getProperty("accesslevel");
%>
<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
		</ul>
	</div>
	  	
	<div id="body">
	
		<!-- User List  -->
		
		<% if (request.getParameter("deleted") != null) { %>
			<br /><span class="good-message">A user was successfully deleted!</span>
		<% } %>
		<fieldset>
			<legend>User List</legend>
			
		<table id="users">
			<tr>
				<td class="cell-header">Last Name</td>
				<td class="cell-header">First Name</td>
				<td class="cell-header">Email</td>
				<td class="cell-header">Role</td>
				<% if (userAccess == WrapperObject.AccessLevel.ADMIN) { %>
				<td class="cell-header" colspan="3">Modify</td>
				<% } else {%>
				<td class="cell-header" colspan="1">Profile</td>
				<% } %>
			</tr>		
 		<% for (WrapperObject user : users) {
			
			String firstname = (String) user.getProperty("firstname");
		 	String lastname = (String) user.getProperty("lastname");
		 	String username = (String) user.getProperty("username");
		 	WrapperObject.AccessLevel accesslevel = (WrapperObject.AccessLevel) user.getProperty("accesslevel");
			
			%>
			<tr>
				<td class="cell"><%=lastname %></td>
				<td class="cell"><%=firstname %></td>
				<td class="cell"><%=username%></td>
				<td class="cell">
				<% if (accesslevel == WrapperObject.AccessLevel.TA) { %>
					TA
				<% } if (accesslevel == WrapperObject.AccessLevel.INSTRUCTOR) { %>
					INSTRUCTOR
				<% } if (accesslevel == WrapperObject.AccessLevel.ADMIN) {  %>
					ADMIN
				<% } %>
				</td>
				
				
				
				<td class="cell">
					<form action="/profile" method="post">
						<input type="hidden" name="username" value="<%=username %>" />
						<input type="submit" value="View Profile" />
					</form>
				</td>
				<% if (userAccess == WrapperObject.AccessLevel.ADMIN) { %>
				
				<%if(!userName.equalsIgnoreCase(username)){%>
				<td class="cell">
					<form action="/userlist" method="post">
						<input type="hidden" name="username" value="<%=username %>" />
						<input type="submit" value="Delete User"/>
					</form>
				</td>
				<%}else{%>
				<td class="cell"></td>
				<%}%>
				
				<td class="cell">
					<form action="/editprofile" method="post">
						<input type="hidden" name="username" value="<%=username %>" />
						<input type="submit" value="Edit User" />
					</form>
				</td>
						
				<% } %>
				
			</tr>
		<% } %>
		</table>
		</fieldset>	
	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />