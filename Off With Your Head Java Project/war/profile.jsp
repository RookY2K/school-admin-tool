<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.library.Library"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>


<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Profile" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="profile.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<%
	WrapperObject user = (WrapperObject)request.getAttribute("user");
	WrapperObject self = (WrapperObject) Auth.getSessionVariable(request, "user");
	if(user == null) user = self;
	Map<String,Object> properties = null;
	
	if(user != null){
		properties = Library.propertySetBuilder("firstname", user.getProperty("firstname")
		                               ,"lastname",user.getProperty("lastname")
		                               ,"phone",user.getProperty("phone")
		                               ,"email",user.getProperty("email")
		                               ,"streetaddress",user.getProperty("streetaddress")
		                               ,"city",user.getProperty("city")
		                               ,"state",user.getProperty("state")
		                               ,"zip",user.getProperty("zip")
		                               );
		
		Set<String> keySet = properties.keySet();
		String val = "";
		for(String key : keySet){
			if(properties.get(key) == null) properties.put(key, val);
		}
	}
	
	if (user == null || properties == null || self == null) return;
	WrapperObject.AccessLevel accesslevel = (WrapperObject.AccessLevel) self.getProperty("accesslevel");
%>

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
			<li><a class="nav-link" href="/profile">View My Profile</a></li>
			<li><a class="nav-link" href="/editprofile">Edit My Profile</a></li>
			<li><a class="nav-link" href="/editprofile#changepassword">Change Password</a></li>
			<!--
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			-->
		</ul>
	</div>

	<div id="body">
			<table id="profile-table">
				<tr>
					<td class="user-label">Name:</td><td class="user-data"><%=properties.get("firstname") + " " + properties.get("lastname") %></td>
				</tr>
				<tr>
					<td class="user-label">Email Address:</td><td class="user-data"><%=properties.get("email") %></td>
				</tr>
				<%  if (self.getId().equals(user.getId()) || accesslevel == WrapperObject.AccessLevel.ADMIN) {  %>
				<tr>
					<td class="user-label">Phone Number:</td><td class="user-data"><%=properties.get("phone") %></td>
				</tr>
				<tr>
					<td class="user-label">Address:</td><td class="user-data"><%=properties.get("streetaddress")%> <br/>
					                                                          <%=properties.get("city") + ", " + properties.get("state") + " " + properties.get("zip")%></td>
				</tr>
				<% } %>
				<tr>
			  <%  if (self.getId().equals(user.getId())) {  %>
					<td id="edit-link-cell">
						<form action="/editprofile" method="get">
							<input type="submit" value="Edit My Profile"/>
						</form>
					</td>
				<% } else if (accesslevel == WrapperObject.AccessLevel.ADMIN) { %>
					
					<td id="edit-link-cell">
						<form action="/editprofile" method="get">
							<input type="hidden" name="username" value="<% out.print(user.getProperty("username")); %>" />
							<input type="submit" value="Edit User Profile" />
						</form>
					</td>
					
				<% } %>
				</tr>
			</table>
	</div>                    
</div>
<jsp:include page="/WEB-INF/templates/footer.jsp" />
