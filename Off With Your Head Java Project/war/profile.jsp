<%@ page import="edu.uwm.owyh.model.Person" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Profile" />
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
	  <% Person user = (Person) request.getAttribute("user");
	       if(user != null) { %>
			<table id="profile-table">
				<tr>
					<td class="user-label">Name:</td><td class="user-data"><%=user.getProperty("name") %></td>
				</tr>
				<tr>
					<td class="user-label">Phone Number:</td><td class="user-data"><%=user.getProperty("phone") %></td>
				</tr>
				<tr>
					<td class="user-label">Email Address:</td><td class="user-data"><%=user.getEmail() %></td>
				</tr>
				<tr>
					<td class="user-label">Street Address:</td><td class="user-data"><%=user.getProperty("address") %></td>
				</tr>
				<tr>
				<% Person me = (Person) Auth.getSessionVariable(request, "user");
					if (me != null && me.getUserName().equals(user.getUserName())) {
				%>
					<td id="edit-link-cell">
						<form action="/editprofile" method="get">
							<input type="submit" value="Edit My Profile"/></a>
						</form>
					</td>
				<% } else { %>
					
					<td id="edit-link-cell">
						<form action="/userlisteditbutton" method="post">
							<input type="submit" value="Edit User Profile" name="<% out.print(user.getUserName()); %>" />
						</form>
					</td>
					
				<% } %>
				</tr>
			</table>
		  <% } %>
	</div>                    
</div>
<jsp:include page="/WEB-INF/templates/footer.jsp" />
