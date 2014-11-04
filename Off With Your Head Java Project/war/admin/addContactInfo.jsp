<%@ page import="edu.uwm.owyh.model.Person" %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Add Contact" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <!-- <li><a class="nav-link" href="/admin/addClient">Add User</a></li> -->
	      <li><a class="nav-link" href="/admin/addContactInfo">Add User Contact Info</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	
		<% if (request.getAttribute("addNewUser") != null) {
				boolean addNewUser = (Boolean) request.getAttribute("addNewUser");
				if (addNewUser) { %>
					<span class="good-message">Contact Info Was Added To The DataStore!</span>
				<% }
				else { %>
					<span class="error-message">Add Contact Info Failed! You Must Use A Valid UWM Email and a Phone Number With Area Code!</span>
				<% }
			} %>
			
		<%
			String name = "";
			String email = "";
			String address = "";
			String phonenumber = "";
			if(request.getAttribute("badUserInfo") != null) {
				Person user = (Person) request.getAttribute("badUserInfo");
				name = user.getName();
				email = user.getEmail();
				address = user.getAddress();
				phonenumber = user.getPhone();
			}
		
		%>
	
		<form action="/admin/addContactInfo" method="post">
			<fieldset>
				<legend> Add Users</legend>
				<table>
				<tr>
					<td class="cell"><label class="field" for="name">Name: </label></td>
					<td class="cell"><input type = "text" name="name" id="name" value="<%=name %>" required/>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="email">Email: </label></td>
					<td class="cell"><input type = "text" name="email" id="email" value="<%=email %>" required />
				</tr>
				<tr>
					<td class="cell"><label class="field" for="accesslevel">Role: </label></td>
					<td class="cell"> 
						<select name="accesslevel">
						  <option value="<% out.print(Person.AccessLevel.TA.getVal()); %>">TA</option>
						  <option value="<% out.print(Person.AccessLevel.INSTRUCTOR.getVal()); %>">INSTRUCTOR</option>
						  <option value="<% out.print(Person.AccessLevel.ADMIN.getVal()); %>">ADMIN</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="address">Address: </label></td>
					<td class="cell"><input type = "text" name="address" id="address" value="<%=address %>" />
				</tr>
				<tr>
					<td class="cell"><label class="field" for="phone">Phone number: </label></td>
					<td class="cell"><input type = "text" name="phone" id="phone" value="<%=phonenumber %>" />
				</tr>
				<tr>
					<td class="cell" colspan="2"><input type="submit" value="Submit Contact Info" /></td>
				</tr>
				</table>
			</fieldset>
		</form>
	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />