<%@ page import="edu.uwm.owyh.model.Person" %>
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
	Person user = (Person)request.getAttribute("user");
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
%>

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
	  <% 
	       if(user != null && properties != null) { %>
			<table id="profile-table">
				<tr>
					<td class="user-label">Name:</td><td class="user-data"><%=properties.get("firstname") + " " + properties.get("lastname") %></td>
				</tr>
				<tr>
					<td class="user-label">Phone Number:</td><td class="user-data"><%=properties.get("phone") %></td>
				</tr>
				<tr>
					<td class="user-label">Email Address:</td><td class="user-data"><%=properties.get("email") %></td>
				</tr>
				<tr>
					<td class="user-label">Address:</td><td class="user-data"><%=properties.get("streetaddress")%> <br/>
					                                                          <%=properties.get("city") + ", " + properties.get("state") + " " + properties.get("zip")%></td>
				</tr>
				<tr>
				<% Person me = (Person) Auth.getSessionVariable(request, "user");
					if (me != null && me.getUserName().equals(user.getUserName())) {
				%>
					<td id="edit-link-cell">
						<form action="/editprofile" method="get">
							<input type="submit" value="Edit My Profile"/>
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
