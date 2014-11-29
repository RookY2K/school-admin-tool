<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="edu.uwm.owyh.jdo.OfficeHours" %>
<%@ page import="edu.uwm.owyh.factories.WrapperObjectFactory" %>
<%@ page import="edu.uwm.owyh.library.Library"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Profile" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="profile.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<%
	Map<String,Object> user = (Map<String,Object>) request.getAttribute("user");
	Map<String,Object> self = (Map<String,Object>) request.getAttribute("self");
	
	if (user == null || self == null) return;
	
	AccessLevel accesslevel = (AccessLevel) self.get("accesslevel");
%>

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
			<li><a class="nav-link" href="/profile">View My Profile</a></li>
			<li><a class="nav-link" href="/editprofile">Edit My Profile</a></li>
			<li><a class="nav-link" href="/editprofile#changepassword">Change Password</a></li>
			<li><a class="nav-link" href="/editofficehours">Add/Edit Office Hour</a></li>
			<!--
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			-->
		</ul>
	</div>

	<div id="body">
			<table id="profile-table">
				<tr>
					<td class="user-label">Name:</td><td class="user-data"><%=user.get("firstname") + " " + user.get("lastname") %></td>
				</tr>
				<tr>
					<td class="user-label">Email Address:</td><td class="user-data"><%=user.get("email") %></td>
				</tr>
				<%  if (self.get("email").equals(user.get("email")) || accesslevel == AccessLevel.ADMIN) {  %>
				<tr>
					<td class="user-label">Phone Number:</td><td class="user-data"><%=user.get("phone") %></td>
				</tr>
				<tr>
					<td class="user-label">Address:</td><td class="user-data"><%=user.get("streetaddress")%> <br/>
					                                                          <%=user.get("city") + ", " + user.get("state") + " " + user.get("zip")%></td>
				</tr>
				<% } %>
				
				<tr>
					<td class="user-label"> Office Hours: </td><td class="user-data">
					
				 <% List<WrapperObject<OfficeHours>> officeHoursWrapped = (List<WrapperObject<OfficeHours>>) user.get("officehours");

				 	for(WrapperObject<OfficeHours> hours : officeHoursWrapped) {
		 			  	String startTime = (String) hours.getProperty("starttime");
		 			  	String endTime = (String) hours.getProperty("endtime");
		 			  	String days = (String) hours.getProperty("days");
				 		
				 		out.print("" + days +" " + startTime + " - " + endTime + "<br />");
				 	}
				 %>
				</td>
				</tr>
				<tr>
			  <%  if (self.get("email").equals(user.get("email"))) {  %>
					<td id="edit-link-cell">
						<form action="/editprofile" method="get">
							<input type="submit" value="Edit My Profile"/>
						</form>
					</td>
				</tr>
				<tr>
					<td id="edit-link-cell">
						<form action="/editofficehours" method="get">
							<input type="submit" value="Edit Office Hours"/>
						</form>
					</td>
				</tr>	
				<% } else if (accesslevel == AccessLevel.ADMIN) { %>
				<tr>	
					<td id="edit-link-cell">
						<form action="/editprofile" method="get">
							<input type="hidden" name="username" value="<% out.print(user.get("email")); %>" />
							<input type="submit" value="Edit User Profile" />
						</form>
					</td>
					
				<% } %>
				</tr>
			</table>
	</div>                    
</div>
<jsp:include page="/WEB-INF/templates/footer.jsp" />
