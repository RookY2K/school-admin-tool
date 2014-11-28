<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Profile Manager" />
    <jsp:param name="stylesheet" value="main.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/homenavagation.jsp" />

<%
	Map<String,Object> self = (Map<String,Object>) request.getAttribute("self");
	List<Map<String,Object>> officeHours = (List<Map<String,Object>>) request.getAttribute("officehours");
	if (self == null) { out.print("No Correct Attribute Was Passed Into JSP!"); return; }
%>

<div id="body">

	<div style="float:left;width:60%;">
		
		<ul class="message" style="margin-top:0px;">		
			<% if (self.get("firstname") == null || self.get("firstname").equals("")) { %>
			<li class="warning-message">You should edit your name so other users know who you are!</li>
			<% } %>
			<% if (self.get("phone") == null || self.get("phone").equals("")) { %>
			<li class="warning-message">You should edit your phone number so you can be contacted!</li>
			<% } %>
			<% if (self.get("streetaddress") == null || self.get("streetaddress").equals("")) { %>
			<li class="warning-message">You should edit your mailing address number so you can be contacted!</li>
			<% } %>
		</ul>

		<p><strong><img src="/images/message_info.png" style="margin-right:5px;"/>User Information</strong></p>
		
		<table>
			<tr>
				<td class="user-label">Name:</td><td class="user-data"><%=self.get("firstname") + " " + self.get("lastname") %></td>
			</tr>
			<tr>
				<td class="user-label">Email:</td><td class="user-data"><%=self.get("email") %></td>
			</tr>
			<tr>
				<td class="user-label">Role:</td><td class="user-data">Admin</td>
			</tr>
			<tr>
				<td class="user-label">Password:</td>
				<td class="user-label"><a href="#changepassword"><img src="/images/build_exec.png" style="margin-right:5px;"/>Change Password</a></td>				   
			</tr>
			<tr>
				<td class="user-label"></td>
				<td class="user-data"><a href="#editprofile"><img src="/images/readwrite_obj.png" style="margin-right:5px;"/>Edit Profile</a></td>
			</tr>
		</table> 
		
		<br /><p><img src="/images/file_obj.png" style="margin-right:5px;"/><strong>Contact Information</strong></p>
		
		<table>
			<tr>
				<td class="user-label">Secondary Email:</td><td class="user-data"><%=self.get("email") %></td>
			</tr>
			<tr>
				<td class="user-label">Phone Number:</td><td class="user-data"><%=self.get("phone") %></td>
			</tr>
			<tr>
				<td class="user-label">Mailing Address:</td>
				<td class="user-data"><%=self.get("streetaddress")%> <br/>  <%=self.get("city") %> 
					<% if (self.get("city") != null && !self.get("city").equals("") && self.get("state") != null && !self.get("state").equals("")) { out.print(", "); } %>
					<%=self.get("state") + " " + self.get("zip")%>
				</td>
			</tr>
		</table>
		<br /><ul class="message"><li>Contact information is private and only viewable by you and the Administrators.</li></ul>
	</div>

	<div style="float:right;width:38%;">
		<div class="officehour-tab"  style="min-height:300px; height:100%">
			<p><strong>Office Hours</strong></p>
			<table style="width: 100%; text-align: center; font-family: Tahoma;">
				<tr>
					<td class="underline">Days</td><td class="underline">Time</td><td class="underline">Room</td>
				</tr>
				<% if (officeHours != null) { %>
				<% for (Map<String,Object> hour : officeHours) { %>
				<tr>
					<td><%=hour.get("days") %></td><td> <%=hour.get("starttime") %> - <%=hour.get("endtime") %></td><td></td>
				</tr>
				<% } } %>
			</table>
		</div>
	</div>
	
	<br class="clear" />
</div>      

<!-- CSS Modal Start Here -->

<aside id="changepassword" class="modal">
    <div>
		<a href="#close" title="Close" class="unselectable">Close</a>
		<p><strong>Change Password</strong></p>
		<% 	List<String> changePasswordErrors = (List<String>) request.getAttribute("changepassworderrors");
			if (changePasswordErrors != null) { %>
				<ul class="message">
		<%	for (String error : changePasswordErrors) { %>
			<li class="error-message"><%=error %></li>
			<% } %>
				</ul>
		<% } %>
		
        <form action="/profile#changepassword" method="post">
			<input type="hidden" name="changepassword" value="changepassword" />
			<input type="hidden" name="email" value="admin@uwm.edu" />
			<table>
				<tr>
				   <td class="user-label">Original Password:</td>
				   <td class="user-label"><input type = "password" name="orginalpassword" id="orginalpassword" value="" required />
				</tr>
				<tr>
				   <td class="user-label">New Password:</td>
				   <td class="user-label"><input type = "password" name="newpassword" id="newpassword" value="" required />
				</tr>
				<tr>
				   <td class="user-label">Verify New Password:</td>
				   <td class="user-label"><input type = "password" name="verifynewpassword" id="verifynewpassword" value="" required />
				</tr>
				<tr>
				   <td class="submitinfo" colspan="2"><input type="submit" class="submit" name="changepasswordsubmit" value="Change Password"/></td>
				</tr>
			</table>
	   </form>
    </div>
</aside>

<aside id="passwordchanged" class="modal">
    <div>
		<a href="#close" title="Close" class="unselectable">Close</a>
		<p><strong>Change Password</strong></p>
		<ul class="message" style="margin-top:0px;">
			<li class="good-message">Your password was successfully changed.</li>
		</ul>
		<form action="#close" method="post">
			<input type="submit" class="submit" name="gotoprofile" value="Confirm"/>
		</form>
    </div>
</aside>

<aside id="editprofile" class="modal">
    <div>
		<p><strong>Contact Information</strong></p>
		<% 	List<String> editProfileErrors = (List<String>) request.getAttribute("editprofileerrors");
			if (editProfileErrors != null) { %>
				<ul class="message">
		<%	for (String error : editProfileErrors) { %>
			<li class="error-message"><%=error %></li>
			<% } %>
				</ul>
		<% } %>
		<form action="/profile#editprofile" method="post">
			<input type="hidden" name="editprofile" value="editprofile" />
			<input type="hidden" name="email" value="<%=self.get("email") %>" />
			<table>
				<tr>
				   <td class="user-label">First Name:</td>
				   <td class="user-label"><input type = "text" name="firstname" value="<%=self.get("firstname") %>" required /></td>
				</tr>
				<tr>
					<td class="user-label">Last Name:</td>
					<td class="user-label"><input type = "text" name="lastname" value="<%=self.get("lastname") %>" required /></td>
				</tr>
				<tr>
				   <td class="user-label">Phone:</td>
				   <td class="user-label"><input type = "text" name="phone" id="phone" value="<%=self.get("phone") %>" /></td>
				</tr>
				<tr>
					<td class="user-label">Address:</td>
					<td class="user-label"><input type = "text" name="streetaddress" id="streetaddress" value="<%=self.get("streetaddress") %>"/></td>
				</tr>
				<tr>
					<td class="user-label">City & State:</td>
					<td class="user-label"><input type = "text" name="city" id="city" value="<%=self.get("city") %>"/>
						<jsp:include page="/WEB-INF/templates/stateselect.jsp">
					    	<jsp:param name="selected" value='<%=self.get("state") %>' />
						</jsp:include>		
					</td>
				</tr>
				<tr>
				   <td class="user-label">Zip Code:</td>
				   <td class="user-label"><input type = "text" name="zip" id="zip" value="<%=self.get("zip") %>"/></td>
				</tr>
				<tr>
				   <td class="submitinfo" colspan="2"><input type="submit" class="submit" name="editprofilesubmit" value="Edit Information"/></td>
				</tr>
			</table>
		</form>
		<ul class="message"><li class="list-message">Phone Number must be in a correct format<br />
		Area Code Follow By 7 Digit Phone Number<br />
		Each Section can be separated by space, comma or dash<br />
		(414) 123 4567, 414.123.4567, 414-123-4567, 4141234567</li></ul>
		
		<a href="#close" title="Close"  class="unselectable">Close</a>
    </div>
</aside>

<aside id="profilechanged" class="modal">
    <div>
		<a href="#close" title="Close" class="unselectable">Close</a>
		<p><strong>Contact Information</strong></p>
		<ul class="message" style="margin-top:0px;">
			<li class="good-message">Your contact information was successfully changed.</li>
		</ul>
		<form action="#close" method="post">
			<input type="submit" class="submit" name="gotoprofile" value="Confirm"/>
		</form>
    </div>
</aside>

        
<jsp:include page="/WEB-INF/templates/footer.jsp" />
