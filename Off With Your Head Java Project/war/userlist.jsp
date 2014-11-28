<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="userlist.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/genericnavagation.jsp">
	<jsp:param name="content" value="User List" />
</jsp:include>

<%
	Map<String,Object> self = (Map<String,Object>) request.getAttribute("self");
	List<Map<String,Object>> users = (List<Map<String,Object>>) request.getAttribute("users");
	
	if (self == null || users == null) { out.print("No Correct Attribute Was Passed Into JSP!"); return; }

	AccessLevel userAccess = (AccessLevel) self.get("accesslevel");
	boolean isAdmin = (userAccess == AccessLevel.ADMIN);
	
	Map<String,Object> modifyUser = (Map<String,Object>) request.getAttribute("modifyuser");
%>

<div id="body" style="clear:both;">
	
		<!-- User List  -->
		
		<% if (request.getParameter("deleted") != null) { %>
			<br /><span class="good-message">A user was successfully deleted!</span>
		<% } %>
		
		<table id="user-search">
			<tr>
				<td>User Name: <input type="text" value="" /></td>
				<td>Email: <input type="text" value="" /></td>
				<td><input type="checkbox" /> Admin <input type="checkbox" /> Instructor <input type="checkbox" /> TA</td>
				<td><input type="submit" class="submit" value="Filter Users" /></td>
			</tr>
		</table>

		<br /><br />
		<table id="users">
			<tr>
				<td class="cell-header">Last Name</td>
				<td class="cell-header">First Name</td>
				<td class="cell-header">Email</td>
				<td class="hidden"></td><td class="hidden"></td><td class="hidden"></td>
				<td class="hidden"></td><td class="hidden"></td>
				<td class="cell-header">Role</td>
				<% if (userAccess == AccessLevel.ADMIN) { %>
				<td class="cell-header" colspan="3">Profile</td>
				<% } else {%>
				<td class="cell-header" colspan="1">Profile</td>
				<% } %>
			</tr>		
 		<% for (int i = 0; i < users.size(); i++) {
 				Map<String,Object> user = users.get(i);
		 		AccessLevel accesslevel = (AccessLevel) user.get("accesslevel");
		 		
		 		String address = user.get("streetaddress") + "<br/>" + user.get("city");
				if (user.get("city") != null && !user.get("city").equals("") && user.get("state") != null && !user.get("state").equals("")) address += ", ";
				address += user.get("state") + " " + user.get("zip");
		%>
			<tr>
				<td class="cell"><%=user.get("lastname") %></td>
				<td class="cell"><%=user.get("firstname") %></td>
				<td class="cell"><%=user.get("email") %></td>
				<td class="hidden"><%=user.get("phone") %></td>
				<td class="hidden"><%=address %></td>
				<td class="hidden"><%=user.get("streetaddress") %></td>
				<td class="hidden"><%=user.get("city") %></td>
				<td class="hidden"><%=user.get("state") %></td>
				<td class="hidden"><%=user.get("zip") %></td>
				<td class="cell">
				<% if (accesslevel == AccessLevel.TA) { %>
					TA
				<% } if (accesslevel == AccessLevel.INSTRUCTOR) { %>
					INSTRUCTOR
				<% } if (accesslevel == AccessLevel.ADMIN) {  %>
					ADMIN
				<% } %>
				</td>
				
				<td class="cell">
					<form action="/userlist#viewuserprofile" method="post">
						<input type="hidden" name="modifyuser" value="<%=user.get("email") %>" />
						<input type="submit" class="submit" value="View" onclick="viewProfile(<%=i + 1 %>);" />
					</form>
				</td>
				<% if (userAccess == AccessLevel.ADMIN) { %>
				
				<% String myUsername = (String)self.get("email");
					String username = (String)user.get("email");
					
					if(myUsername != null && !myUsername.equals(username)){%>
				<td class="cell">
					<form action="/userlist#deleteuser" method="post">
						<input type="hidden" name="modifyuser" value="<%=user.get("email") %>" />
						<input type="submit" class="submit" value="Delete"/>
					</form>
				</td>
				<%}else{%>
				<td class="cell"></td>
				<%}%>
				
				<td class="cell">
					<form action="/userlist#edituserprofile" method="post">
						<input type="hidden" name="modifyuser" value="<%=user.get("email") %>" />
						<input type="submit" class="submit" value="Edit" />
					</form>
				</td>
						
				<% } %>
			</tr>
		<% } %>
		</table>
		
	<br class="clear" />
</div>



<!-- CSS Modal Start Here -->

<aside id="viewuserprofile" class="modal">
    <div>
    	<% 	if (modifyUser != null) {	%>
  		<form action="/userlist#edituserprofile" method="post">
  			<input type="hidden" id="modifyuser" name="modifyuse" value="<%=modifyUser.get("email") %>" />
			<table>
				<tr>
				   <td class="user-label">First Name:</td>
				   <td class="user-label"><%=modifyUser.get("firstname") %></td>
				</tr>
				<tr>
					<td class="user-label">Last Name:</td>
					<td class="user-label"><%=modifyUser.get("lastname") %></td>
				</tr>
				<tr>
				   <td class="user-label">Email:</td>
				   <td class="user-label"><%=modifyUser.get("email") %></td>
				</tr>
			   <tr>
				   <td class="user-label">Phone:</td>
				   <td class="user-label"><%=modifyUser.get("phone") %></td>
			   </tr>
			   <tr>
				   <td class="user-label">Address:</td>
				   <td class="user-label"><%=modifyUser.get("streetaddress") %> <br /> <%=modifyUser.get("city") %>
				   <% if (modifyUser.get("city") != null && modifyUser.get("") != null) { out.print(" , "); } %>
				   <%=modifyUser.get("zip") %>
				   </td>
			   </tr>
				<tr>
				   <td class="submitinfo" colspan="2"><input type="submit" class="submit" name="editprofilesubmit" value="Edit Information"/></td>
				</tr>
			</table>	
		</form>
		<a href="#close" title="Close"  class="unselectable">Close</a>
    </div>
</aside>

<aside id="edituserprofile" class="modal">
    <div>
		<p><strong>Contact Information</strong></p>
		<% 	List<String> editProfileErrors = (List<String>) request.getAttribute("edituserprofileerrors");
			String goodEditUser = (String) request.getAttribute("goodedituser");
			if (editProfileErrors != null) { %>
				<ul class="message">
		<%	for (String error : editProfileErrors) { %>
			<li class="error-message"><%=error %></li>
			<% } %>
				</ul>
		<% } %>
		
	<% if (goodEditUser != null) { %>
		<p><span class="good-message">This Contact Information was successfully edited!</span> </p>
		<% } %>
		
		<% 	if (modifyUser != null) {
			String state ="";
			state = (String) modifyUser.get("state");
		%>
		<form action="/userlist#edituserprofile" method="post">
			<input type="hidden" name="edituserprofile" id="edituserprofile" value="edituserprofile" />
			<input type="hidden" name="email" id="email" value="<%=modifyUser.get("email") %>" />
			<input type="hidden" name="username" id="username" value="<%=modifyUser.get("email") %>" />
			<table>
				<tr>
				   <td class="user-label">Email:</td>
				   <td class="user-label"><input type="text" value="<%=modifyUser.get("email") %>" disabled /></td>
				</tr>
				<tr>
				   <td class="user-label">First Name:</td>
				   <td class="user-label"><input type = "text" name="firstname" id="firstname" value="<%=modifyUser.get("firstname") %>" required /></td>
				</tr>
				<tr>
					<td class="user-label">Last Name:</td>
					<td class="user-label"><input type = "text" name="lastname" id="lastname" value="<%=modifyUser.get("lastname") %>" required /></td>
				</tr>
				<tr>
				   <td class="user-label">Phone:</td>
				   <td class="user-label"><input type = "text" name="phone" id="phone" value="<%=modifyUser.get("phone") %>" /></td>
				</tr>
				<tr>
					<td class="user-label">Address:</td>
					<td class="user-label"><input type = "text" name="streetaddress" id="streetaddress" value="<%=modifyUser.get("streetaddress") %>"/></td>
				</tr>
				<tr>
					<td class="user-label">City & State:</td>
					<td class="user-label"><input type = "text" name="city" id="city" value="<%=modifyUser.get("city") %>"/>
						<jsp:include page="/WEB-INF/templates/stateselect.jsp">
					    	<jsp:param name="selected" value='<%=state %>' />
						</jsp:include>		
					</td>
				</tr>
				<tr>
				   <td class="user-label">Zip Code:</td>
				   <td class="user-label"><input type = "text" name="zip" id="zip" value="<%=modifyUser.get("zip") %>"/></td>
				</tr>
				<tr>
				   <td class="submitinfo" colspan="2"><input type="submit" class="submit" name="edituserprofilesubmit" value="Edit Information"/></td>
				</tr>
			</table>
		</form>
		<ul class="message">
			<li class="list-message">First Name, Last Name required.</li>
			<li class="list-message">Phone Number must be in a correct format<br />
		Area Code Follow By 7 Digit Phone Number<br />
		Each Section can be separated by space, comma or dash<br />
		(414) 123 4567, 414.123.4567, 414-123-4567, 4141234567
		<% } %>
		</ul>
		<% } %>
		<a href="#close" title="Close"  class="unselectable">Close</a>
    </div>
</aside>
<aside id="deleteuser" class="modal">
    <div>
		<a href="#close" title="Close" class="unselectable">Close</a>
		<p><strong>Delete User</strong></p>
		<% if (modifyUser == null) { %>
		<ul class="message" style="margin-top:0px;">
			<li class="good-message">User has been successfully deleted.</li>
		</ul>
		<form action="#close" method="post">
			<input type="submit" class="submit" name="gotoprofile" value="Confirm"/>
		</form>
		<% }
		else {
		%>
		<ul class="message" style="margin-top:0px;">
			<li class="warning-message">Are you sure you want to delete <%=modifyUser.get("email") %>?</li>
		</ul>
		<form action="/userlist#userdeleted" method="post">
			<input type="hidden" name="deleteuserconfirm" value="deleteuserconfirm" />
			<input type="hidden" name="username" value="<%=modifyUser.get("email") %>" />
			<input type="submit" class="submit" name="gotoprofile" value="Delete"/>
		</form>
		<% } %>
    </div>
</aside>

<jsp:include page="/WEB-INF/templates/footer.jsp" />