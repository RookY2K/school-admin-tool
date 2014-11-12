<%@ page import="edu.uwm.owyh.model.Person" %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="users.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />
<%
	String userName = "";
	Person user = (Person)request.getAttribute("user");
	if(user != null){
		userName = user.getUserName();
	}
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
		<form>
			<fieldset>
				<legend>User List</legend>
			
		<table id="users">
			<tr>
				<td class="cell-header">Last Name</td>
				<td class="cell-header">First Name</td>
				<td class="cell-header">Email</td>
				<td class="cell-header">Role</td>
				<td class="cell-header" colspan="3">Modify</td>
			</tr>		
 	<% String[] firstname = (String[]) request.getAttribute("firstname");
 	   String[] lastname = (String[]) request.getAttribute("lastname");
 	   String[] username = (String[]) request.getAttribute("username");
	   int[] accesslevel = (int[]) request.getAttribute("accesslevel");
	
		for (int i = 0; i < username.length; i++) { %>
			<tr>
				<td class="cell"><%=lastname[i] %></td>
				<td class="cell"><%=firstname[i] %></td>
				<td class="cell"><%=username[i] %></td>
				<td class="cell">
				<% if (accesslevel[i] == Person.AccessLevel.TA.getVal()) { %>
					TA
				<% } if (accesslevel[i] == Person.AccessLevel.INSTRUCTOR.getVal()) { %>
					INSTRUCTOR
				<% } if (accesslevel[i] == Person.AccessLevel.ADMIN.getVal()) {  %>
					ADMIN
				<% } %>
				</td>
				<form action="/profile" method="post">
				<td class="cell"><input type="submit" value="View Profile" name="<%=username[i] %>"/></td>
				</form>
				<%if(!userName.equalsIgnoreCase(username[i])){%>
				<form action="/userlist" method="post">
				<td class="cell"><input type="submit" value="Delete User" name="<%=username[i] %>"/></td>
				</form>
				<%}else{%>
				<td class="cell"></td>
				<%}%>
				<form action="/userlisteditbutton" method="post">
				<td class="cell"><input type="submit" value="Edit User" name="<%=username[i] %>"/></td>
				</form>
				
			</tr>
		<%
	} %>
		</table>
		</fieldset>
		</form>	
	
	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />