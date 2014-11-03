<%@ page import="edu.uwm.owyh.model.User" %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="profile.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
			<li><a class="nav-link" href="/profile">User Profile</a></li>
			<!--
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			-->
		</ul>
	</div>

	<div id="body">
	  <% User user =(User)(request.getAttribute("user"));
	       if(user != null) { %>
			<table id="profile-table">
				<tr>
					<td class="user-label">Name:</td><td class="user-data"><%=user.getName() %></td>
				</tr>
				<tr>
					<td class="user-label">Phone Number:</td><td class="user-data"><%=user.getPhone() %></td>
				</tr>
				<tr>
					<td class="user-label">Email Address:</td><td class="user-data"><%=user.getEmail() %></td>
				</tr>
				<tr>
					<td class="user-label">Street Address:</td><td class="user-data"><%=user.getAddress() %></td>
				</tr>
				<tr>
					<td id="edit-link-cell"><a id="edit-link" href="/editprofile">Edit Profile</a></td>
				</tr>
			</table>
		  <% } %>
	</div>                    
</div>
<jsp:include page="/WEB-INF/templates/footer.jsp" />