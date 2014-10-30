<%@ page import="edu.uwm.owyh.model.User" %>

<jsp:include page="/header.jsp">
    <jsp:param name="title" value="Edit User" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/layout.jsp" />

<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="/admin/addUser">Add User</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	    <% User user =(User)(request.getAttribute("user"));
	       if(user != null) { %>
		       <form action="/admin/adminEditUser" method="post">
				   <fieldset>
					   <legend> <%= user.getUserName() %> </legend>
					   <table>
					   <tr>
						   <td class="cell"><label class="field" for="email">Password: </label></td>
						   <td class="cell"><input type = "text" name="password" id="password" value="<%=user.getPassword() %>" required />
					   </tr>
					   <tr>
						   <td class="cell"><label class="field" for="accesslevel">AccessLevel: </label></td>
						   <td class="cell"> 
							   <select name="accesslevel">
							     <option value="<% out.print(User.AccessLevel.TA.getVal()); %>" <% if(user.getAccessLevel() == User.AccessLevel.TA) {%> selected <% } %>>TA</option>
							     <option value="<% out.print(User.AccessLevel.INSTRUCTOR.getVal()); %>" <% if(user.getAccessLevel() == User.AccessLevel.INSTRUCTOR) {%> selected <% } %>>INSTRUCTOR</option>
							     <option value="<% out.print(User.AccessLevel.ADMIN.getVal()); %>" <% if(user.getAccessLevel() == User.AccessLevel.ADMIN) {%> selected <% } %>>ADMIN</option>
							   </select>
						   </td>
					   </tr>
					   <!--
					   <tr>
					       <td class="cell"><label class="field" for="email">Email: </label></td>
						   <td class="cell"><input type = "text" name="email" id="email" required />
					   </tr>
					   -->
					   <tr>
						   <td class="cell" colspan="2"><input type="submit" value="Edit this user"/></td>
					   </tr>
					   </table>
					   
					   <!-- This is hidden, but required to get the proper user once we post. -->
					   <input type="hidden" name="username" value="<%= user.getUserName() %>" />
				   </fieldset>
			   </form>
		<% } %>
	</div>
</div>

<jsp:include page="/footer.jsp" />