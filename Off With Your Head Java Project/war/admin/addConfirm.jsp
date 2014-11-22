<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Confirm Add User" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<%
	Map<String,Object> properties = (Map<String,Object>) request.getAttribute("properties");
	if (properties == null) return;
%>

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="/admin/addContactInfo">Add New User</a></li>
	      <li><a class="nav-link" href="/admin/addContactInfo#addcontactinfo">Add Contact Info</a></li>
		</ul>
	</div>

	<div id="body">
			<table id="profile-table">
			<tr>
				<td><br /><span class="good-message">New User Has Been Added!</span></td>
			</tr>
						
			<% if (properties.get("firstname") != null && properties.get("lastname") != null && !properties.get("firstname").equals("") && !properties.get("lastname").equals("")) { %>
				<tr>
					<td class="user-label">Name:</td><td class="user-data"><%=properties.get("firstname") + " " + properties.get("lastname") %></td>
				</tr>
			<% } %>
			
			<% if (properties.get("email") != null && !properties.get("email").equals("")) { %>
				<tr>
					<td class="user-label">Email Address:</td><td class="user-data"><%=properties.get("email") %></td>
				</tr>
			<% } %>
			
			<% if (properties.get("phone") != null && !properties.get("phone").equals("")) { %>
				<tr>
					<td class="user-label">Phone Number:</td><td class="user-data"><%=properties.get("phone") %></td>
				</tr>
			<% } %>
			
			<% if (properties.get("streetaddress") != null && properties.get("city") != null && properties.get("state") != null && properties.get("zip") != null) {
				if (!properties.get("streetaddress").equals("") && !properties.get("state").equals("") && !properties.get("city").equals("") && !properties.get("zip").equals("")) {
			%>
				<tr>
					<td class="user-label">Address:</td><td class="user-data"><%=properties.get("streetaddress")%> <br/>
					                                                          <%=properties.get("city") + ", " + properties.get("state") + " " + properties.get("zip")%></td>
				</tr>
			<% } } %>
			
				<tr>
					<td id="edit-link-cell">
					<br /><br />
						<form action="/editprofile" method="get">
							<input type="hidden" name="username" value="<%=properties.get("email") %>" />
							<input type="submit" value="Edit User's Profile"/>
						</form>
					</td>
				</tr>
				<tr>
					<td id="edit-link-cell">
						<form action="/admin/addContactInfo" method="get">
							<input type="submit" value="Add More Users" />
						</form>
						</td>
				</tr>
				<tr>
					<td id="edit-link-cell">
						<form action="/userlist" method="get">
							<input type="submit" value="Goto User List" />
						</form>
					</td>
					
				</tr>
			</table>
	</div>                    
</div>
<jsp:include page="/WEB-INF/templates/footer.jsp" />
