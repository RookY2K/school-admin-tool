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
	List<Map<String,Object>> officeHours = (List<Map<String,Object>>) request.getAttribute("officehours");
	String skills = (String)request.getAttribute("skills");
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
				<td class="cell-header">Role</td>
				<% if (isAdmin) { %>
				<td class="cell-header" colspan="3">Profile</td>
				<% } else {%>
				<td class="cell-header" colspan="1">Profile</td>
				<% } %>
				<% if (isAdmin) { %>
				<td class="cell-header"colspan="2">Office Hours</td>
				<% } else {%>
				<td class="cell-header"colspan="1">Office Hours</td>
				<% } %>
				<% if (isAdmin) { %>
				<td class="cell-header"colspan="2">Skills</td>
				<% } %>
			</tr>		
 		<% for (int i = 0; i < users.size(); i++) {
 				Map<String,Object> user = users.get(i);
		 		AccessLevel accesslevel = (AccessLevel) user.get("accesslevel");
		%>
			<tr class="line<% out.print(i % 2 + 1); %>">
				<td class="cell"><%=user.get("lastname") %></td>
				<td class="cell"><%=user.get("firstname") %></td>
				<td class="cell"><%=user.get("email") %></td>
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
						<input type="submit" class="submit" value="View" />
					</form>
				</td>
				<% if (isAdmin) { %>
				
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
				<td class="cell">
					<form action="/userlist#viewofficehours" method="post">
						<input type="hidden" name="modifyuser" value="<%=user.get("email") %>" />
						<input type="submit" class="submit" value="View" />
					</form>
				</td>
				
				<% if (isAdmin) { %>
				<td class="cell">
					<form action="/officehours" method="post">
					<input type="hidden" name="edituserofficehoursfromadmin" value="dituserofficehoursfromadmin" />
					<input type="hidden" name="email" value="<%=user.get("email") %>" />
					<input type="submit" class="submit" name="gotoprofile" value="Edit"/>
					</form>
				</td>
				<% } %>
				<% if (isAdmin) { %>
				<td class="cell">
					<form action="/userlist#viewuserskills" method="post">
						<input type="hidden" name="modifyuser" value="<%=user.get("email") %>" />
						<input type="submit" class="submit" value="View" />
					</form>
				</td>
				
				<td class="cell">
					<form action="/userlist#edituserskills" method="post">
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
    	<p><strong>Viewing Profile of <%=modifyUser.get("email") %></strong></p>
  		<form action="/userlist#edituserprofile" method="post">
  			<input type="hidden" id="modifyuser" name="modifyuser" value="<%=modifyUser.get("email") %>" />
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
				   <td class="user-label">Role:</td>
				   <td class="user-label"><%=modifyUser.get("accesslevel") %></td>
				</tr>
				<% if (isAdmin) { %>
			   <tr>
				   <td class="user-label">Phone:</td>
				   <td class="user-label"><%=modifyUser.get("phone") %></td>
			   </tr>
			   <tr>
				   <td class="user-label">Address:</td>
				   <td class="user-label"><%=modifyUser.get("streetaddress") %> <br /> <%=modifyUser.get("city") %>
				   <% if (modifyUser.get("city") != null && modifyUser.get("state") != null && 
				   		!modifyUser.get("state").equals("") && !modifyUser.get("city").equals("")) { out.print(", "); } %>
				   <%=modifyUser.get("state") %>  <%=modifyUser.get("zip") %>
				   </td>
			   </tr>
			   <% } %>
				<tr>
				   <td class="submitinfo" colspan="2">
				   <% if (isAdmin) { %>
				   <input type="submit" class="submit" name="editprofilesubmit" value="Edit Information"/>
				   </form>
				   <form action="/userlist#changeuserpassword" method="post">
				   		<input type="hidden" id="modifyuser" name="modifyuser" value="<%=modifyUser.get("email") %>" />
				   		<input type="submit" class="submit" name="editprofilesubmit" value="ChangePassword" style="margin-top:8px;" />
				   </form>
				   </td>
				   <% } %>
				</tr>
			</table>	
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
		<ul class="message"><li class="good-message">This Contact Information was successfully edited!</li></ul>
		<% } %>
		
		<% 	if (modifyUser != null) {
			String state ="";
			state = (String) modifyUser.get("state");
		%>
		<form action="/userlist#edituserprofile" method="post">
			<input type="hidden" name="edituserprofile" id="edituserprofile" value="edituserprofile" />
			<input type="hidden" name="email" id="email" value="<%=modifyUser.get("email") %>" />
			<table>
				<tr>
				   <td class="user-label">Email:</td>
				   <td class="user-label"><%=modifyUser.get("email") %></td>
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
				   <td class="submitinfo" colspan="2"><input type="submit" class="submit" name="edituserprofilesubmit" value="Edit Information"/>
				   </form>
				   	<form action="/userlist#changeuserpassword" method="post">
				   		<input type="hidden" id="modifyuser" name="modifyuser" value="<%=modifyUser.get("email") %>" />
				   		<input type="submit" class="submit" name="editprofilesubmit" value="ChangePassword" style="margin-top:8px;" />
				   </form>
				   </td>
				</tr>
			</table>
		
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
		<form action="/userlist#deleteuser" method="post">
			<input type="hidden" name="deleteuserconfirm" value="deleteuserconfirm" />
			<input type="hidden" name="email" value="<%=modifyUser.get("email") %>" />
			<input type="submit" class="submit" name="gotoprofile" value="Delete"/>
		</form>
		<% } %>
    </div>
</aside>
<aside id="viewofficehours" class="modal">
    <div>
		<a href="#close" title="Close" class="unselectable">Close</a>
		<p><strong>Office Hours</strong></p>
		<p> 
		<% if (modifyUser == null || modifyUser.get("officeroom") == null || modifyUser.get("officeroom").equals("")) { %>
		This user did not set an Office Location.
		<% } else { %>
		Office Location: <%=modifyUser.get("officeroom") %>
		<% } %>
		</p>
		<% if (officeHours == null || officeHours.isEmpty()) { %>
			This user has no Office Hours.
		<% }
		else {
		%>
			<table class="officehour-table">
			<tr>
				<td class="underline">Days</td>
				<td class="underline">Time</td>
			</tr>
		<% for (Map<String, Object> hour : officeHours) { %>
			<tr>
				<td><%=hour.get("days") %></td>
				<td><%=hour.get("starttime") %> - <%=hour.get("endtime") %></td>
				<td></td>
			</tr>
			<tr>
		<% } %>
			</table>
		<% } %>
		<% if (isAdmin && modifyUser != null) { %>
		<br /><br />
			<form action="/officehours" method="post">
			<input type="hidden" name="edituserofficehoursfromadmin" value="dituserofficehoursfromadmin" />
			<input type="hidden" name="email" value="<%=modifyUser.get("email") %>" />
			<input type="submit" class="submit" name="gotoprofile" value="Edit Office Hours"/>
			</form>
		<% } %>
    </div>
</aside>
<aside id="changeuserpassword" class="modal">
    <div>
		<a href="#close" title="Close" class="unselectable">Close</a>
		<% if (modifyUser != null) { %>
		<p><strong>Change Pasword of <%=modifyUser.get("email") %></strong></p>
		<% 	List<String> changePasswordErrors = (List<String>) request.getAttribute("changepassworderrors");
			if (changePasswordErrors != null) { %>
				<ul class="message">
		<%	for (String error : changePasswordErrors) { %>
			<li class="error-message"><%=error %></li>
			<% } %>
				</ul>
		<% } %>
		<% if (request.getAttribute("goodchangepassword") != null) { %>
			<ul class="message"><li class="good-message">User's password was changed successfully.</li></ul>
		<% } %>
		
			<form action="/userlist#changeuserpassword" method="post">
				<input type="hidden" name="changeuserpassword" value="changeuserpassword" />
				<input type="hidden" name="email" value="<%=modifyUser.get("email") %>" />
				<table>
					<tr>
						<td>Change Password:</td>
						<td><input type="password" name="newpassword" required /></td>
					</tr>
					<tr>
						<td>Verify Password:</td>
						<td><input type="password" name="verifynewpassword" required /></td>
					</tr>
					<tr>
						<td  class="submitinfo" colspan="2"><input type="submit" class="submit" value="Change User Password"/>
						</td>
					</tr>
				</table>
			</form>
		<% } %>
    </div>
</aside>

<aside id="viewuserskills" class="modal">
    <div>
    	<% 	if (modifyUser != null) {	%>
    	<p><strong>Viewing Skills of <%=modifyUser.get("email") %></strong></p>
  		<form action="/userlist#edituserskills" method="post">
  			<input type="hidden" id="modifyuser" name="modifyuser" value="<%=modifyUser.get("email") %>" />
			<table>
			    <tr>
				   <td class="user-label"><%=skills%></td>
				</tr>
				<tr>
				   <td class="submitinfo" colspan="2">
				   <% if (isAdmin) { %>
				   <input type="submit" class="submit" name="editskillssubmit" value="Edit Skills"/>
				   </form>
				   </td>
				   <% } %>
				</tr>
			</table>	
		<a href="#close" title="Close"  class="unselectable">Close</a>
		<% } %>
    </div>
</aside>
<aside id="edituserskills" class="modal">
    <div>
		<p><strong>Skills</strong></p>
		<% 	List<String> editSkillsErrors = (List<String>) request.getAttribute("edituserskillserrors");
			String goodEditUser = (String) request.getAttribute("goodedituser");
			if (editSkillsErrors != null) { %>
				<ul class="message">
		<%	for (String error : editSkillsErrors) { %>
			<li class="error-message"><%=error %></li>
			<% } %>
				</ul>
		<% } %>
		
	<% if (goodEditUser != null) { %>
		<ul class="message"><li class="good-message">The User's skills were successfully edited!</li></ul>
		<% } %>
		
		<% 	if (modifyUser != null) {
			String state ="";
			state = (String) modifyUser.get("state");
		%>
		<form action="/userlist#edituserskills" method="post" id="editskillsform">
			<input type="hidden" name="edituserskills" id="edituserskills" value="edituserskills" />
			<input type="hidden" name="email" id="email" value="<%=modifyUser.get("email") %>" />
			
			<textarea rows="4" cols="40" name="skilllist" form="editskillsform"><%=skills%></textarea>
			
			<table>
				<tr>
				   <td class="submitinfo" colspan="2"><input type="submit" class="submit" name="edituserskillssubmit" value="Edit Skills"/>
				   </form>
				   </td>
				</tr>
			</table>
		
		<ul class="message">
			<li class="list-message">Skills must be a comma separated list.</li>
		</ul>
		<% } %>
		<a href="#close" title="Close"  class="unselectable">Close</a>
    </div>
</aside>


<jsp:include page="/WEB-INF/templates/footer.jsp" />