<%@ page import="edu.uwm.owyh.model.Person" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="java.util.List" %>
<%! @SuppressWarnings("unchecked") %>
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Edit User" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="editprofile.css" />
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
		<%
			List<String> errors = (List<String>)request.getAttribute("errors");
			if(errors != null){
				for(String error: errors){
		%>
		<span style="color:red;"><%=error%></span>
		<%			
				}
			}
		%>
	    <% Person user =(Person)Auth.getSessionVariable(request, "user");
	       if(user != null) { %>
		       <form action="/editprofile" method="post">
				   <fieldset>
					   <legend> <%= user.getProperty("name") %> </legend>
					   <table>
					   <tr>
						   <td class="cell"><label class="field" for="name">Name: </label></td>
						   <td class="cell"><input type = "text" name="name" id="name" value="<%=user.getProperty("name") %>" required />
					   </tr>
					   <tr>
						   <td class="cell"><label class="field" for="email">Email: </label></td>
						   <td class="cell"><input type = "text" name="email" id="email" value="<%=user.getProperty("email") %>" required />
					   </tr>
					   <tr>
						   <td class="cell"><label class="field" for="phone">Phone: </label></td>
						   <td class="cell"><input type = "text" name="phone" id="phone" value="<%=user.getProperty("phone") %>" required />
					   </tr>
					   <tr>
						   <td class="cell"><label class="field" for="address">Address: </label></td>
						   <td class="cell"><input type = "text" name="address" id="address" value="<%=user.getProperty("address") %>" required />
					   </tr>
					   <tr>
						   <td class="cell" colspan="2"><input type="submit" value="Submit"/></td>
					   </tr>
					   </table>
				   </fieldset>
			   </form>
		<% } %>
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />
