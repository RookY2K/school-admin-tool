<%@ page import="edu.uwm.owyh.model.Person" %>
<%@ page import="java.util.List" %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Edit User" />
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
		<% 
		if (request.getAttribute("isEdited") != null) {
			boolean isEdited = (Boolean) request.getAttribute("isEdited");
			if (isEdited) { 
		%>
		<span style="color:red;">Edit Was Successfully!</span>
		<% 
			}else { 
				List<String> errors = (List<String>)request.getAttribute("errors");
				for(String error:errors){
		%>
		<span style="color:red;"><%=error%></span>
		<% 
				}
			}
		}
		%>
	
	    <% Person user =(Person)(request.getAttribute("user"));
	       if(user != null) { %>
		       <form action="/admin/adminEditUser" method="post">
				   <fieldset>
					   <legend> <%= user.getProperty("name") %> </legend>
					   <table>
					   <tr>
						   <td class="cell"><label class="field" for="name">Name: </label></td>
						   <td class="cell"><input type = "text" name="name" id="name" value="<%=user.getProperty("name") %>" required />
					   </tr>
					   <tr>
						   <td class="cell"><label class="field" for="address">Address: </label></td>
						   <td class="cell"><input type = "text" name="address" id="address" value="<%=user.getProperty("address") %>" />
					   </tr>
					   <tr>
						   <td class="cell"><label class="field" for="phone">Phone Number: </label></td>
						   <td class="cell"><input type = "text" name="phone" id="phone" value="<%=user.getProperty("phone") %>" />
					   </tr>	
					   <tr>
						   <td class="cell"><label class="field" for="accesslevel">Role: </label></td>
						   <td class="cell"> 
							   <select name="accesslevel">
							     <option value="<% out.print(Person.AccessLevel.TA.getVal()); %>" <% if(user.getProperty("accesslevel") == Person.AccessLevel.TA) {%> selected <% } %>>TA</option>
							     <option value="<% out.print(Person.AccessLevel.INSTRUCTOR.getVal()); %>" <% if(user.getProperty("accesslevel") == Person.AccessLevel.INSTRUCTOR) {%> selected <% } %>>INSTRUCTOR</option>
							     <option value="<% out.print(Person.AccessLevel.ADMIN.getVal()); %>" <% if(user.getProperty("accesslevel") == Person.AccessLevel.ADMIN) {%> selected <% } %>>ADMIN</option>
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

<jsp:include page="/WEB-INF/templates/footer.jsp" />