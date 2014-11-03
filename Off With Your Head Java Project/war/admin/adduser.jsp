<%@ page import="edu.uwm.owyh.model.Person" %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Hello World" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="/admin/addUser">Add User</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	
		<% if (request.getAttribute("addNewUser") != null) {
				boolean addNewUser = (Boolean) request.getAttribute("addNewUser");
				if (addNewUser) { %>
					<span style="color:red;">New User Was Added To The DataStore!</span>
				<% }
				else { %>
					<span style="color:red;">Add New User Failed! Check Email and Password!</span>
				<% }
			} %>
	
		<form action="/admin/addUser" method="post">
			<fieldset>
				<legend> Add Users</legend>
				<table>
				<tr>
					<td class="cell"><label class="field" for="email">Email: </label></td>
					<td class="cell"><input type = "text" name="email" id="email" required />
				</tr>
				<tr>
					<td class="cell"><label class="field" for="email">Password: </label></td>
					<td class="cell"><input type = "text" name="password" id="password" required />
				</tr>
				<tr>
					<td class="cell"><label class="field" for="accesslevel">AccessLevel: </label></td>
					<td class="cell"> 
						<select name="accesslevel">
						  <option value="<% out.print(Person.AccessLevel.TA.getVal()); %>">TA</option>
						  <option value="<% out.print(Person.AccessLevel.INSTRUCTOR.getVal()); %>">INSTRUCTOR</option>
						  <option value="<% out.print(Person.AccessLevel.ADMIN.getVal()); %>">ADMIN</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="cell" colspan="2"><input type="submit" value="Create New User" /></td>
				</tr>
				</table>
			</fieldset>
		</form>
	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />