<%@ page import="edu.uwm.owyh.model.User" %>

<jsp:include page="/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="users.css" />
</jsp:include>

<jsp:include page="/layout.jsp" />

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
		</ul>
	</div>
	  	
	<div id="body">
	
		User List
	
		<table id="users">
			<tr>
				<td class="cell-header">Email</td>
				<td class="cell-header">Access Level</td>
				<td class="cell-header" colspan="2">Modify</td>
			</tr>		
 	<% String[] username = (String[]) request.getAttribute("username");
	   int[] accesslevel = (int[]) request.getAttribute("accesslevel");
	
		for (int i = 0; i < username.length; i++) { %>
			<tr>
				<td class="cell"><%=username[i] %></td>
				<td class="cell">
				<% if (accesslevel[i] == User.AccessLevel.TA.getVal()) { %>
					TA
				<% } if (accesslevel[i] == User.AccessLevel.INSTRUCTOR.getVal()) { %>
					INSTRUCTOR
				<% } if (accesslevel[i] == User.AccessLevel.ADMIN.getVal()) {  %>
					ADMIN
				<% } %>
				</td>
				<form action="/userlist" method="post">
				<td class="cell"><input type="submit" value="Delete User" name="<%=username[i] %>"/></td>
				</form>
				<form action="/userlisteditbutton" method="post">
				<td class="cell"><input type="submit" value="Edit User" name="<%=username[i] %>"/></td>
				</form>
			</tr>
		<%
	} %>
		</table>	
	
	
	</div>
</div>

<jsp:include page="/footer.jsp" />