<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Profile Manager" />
    <jsp:param name="stylesheet" value="main.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/genericnavagation.jsp">
	<jsp:param name="content" value="Admin Control Panel" />
</jsp:include>

<div id="body">

	<a href="#addnewuser">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/adduser.png');"></div>
			Add A New User
		</div>
	</a>
	
	<a href="#addcontactinformation">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/contactinfo.png');"></div>
			Add Contact <br />Information
		</div>
	</a>
	
	<a href="#reloadclassschedule">
		<div class="admin-tab">
			<div class="admin-tab-box"></div>
			Add A New User!
		</div>
	</a>
	
	<a href="#triggernewsemester">
		<div class="admin-tab">
			<div class="admin-tab-box"></div>
			Add A New User!
		</div>
	</a>
	
	<br class="clear" />
	
</div>

<!--
**************************
Start of CSS Pop Up
**************************
-->
<aside id="addnewuser" class="modal">
    <div>
        <p><strong>Add User Account</strong></p>
		
		<% 	List<String> addNewUserErrors = (List<String>) request.getAttribute("addnewusererrors");
		if (addNewUserErrors != null) { %>
			<ul class="message">
		<%	for (String error : addNewUserErrors) { %>
		<li class="error-message"><%=error %></li>
		<% } %>
			</ul>
		<% } %>
		
		<form action="/admin#addnewuser" method="post">
			<input type="hidden" name="addnewuser" value="addnewuser" />
			<table>
				<tr>
					<td class="user-label">Email:</td>
					<td class="user-data"><input type="text" name="email" value="" placeholder="someone@uwm.edu" required /></td>
				</tr>
				<tr>
					<td class="user-label">Password:</td>
					<td class="user-data"><input type="password" name="password" value="" required /></td>
				</tr>
				<tr>
					<td class="user-label">Verify Password:</td>
					<td class="user-data"><input type="password" name="verifypassword" value="" required /></td>
				</tr>
				<tr>
					<td class="user-label">User Role:</td>
					<td class="user-data">
						<select name="accesslevel" required>
						  <option value="">Please Select</option>
						  <option value="3" >TA</option>
						  <option value="2">INSTRUCTOR</option>
						  <option value="1">ADMIN</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="submitinfo" colspan="2" ><input type="submit" class="submit" value="Add User" /></td>
				</tr>
			</table>
		</form>
		<ul class="message"><li>Email must be an UWM email in the format "someone@uwm.edu"</li></ul>
        <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="addcontactinformation" class="modal">
    <div>
		<p><strong>Add Contact Information</strong></p>
		<% Map<String, Object> user = (Map<String, Object>) request.getAttribute("baduserinfo"); 
			String state = "";
			if (user != null) state = (String)user.get("state");
		
		%>
		<% 	List<String> addContatInfoErrors = (List<String>) request.getAttribute("addcontactinfoerrors");
		if (addContatInfoErrors != null) { %>
			<ul class="message">
		<%	for (String error : addContatInfoErrors) { %>
		<li class="error-message"><%=error %></li>
		<% } %>
			</ul>
		<% } %>
		<form action="/admin#addcontactinformation" method="post">
			<input type="hidden" name="addcontactinfo" value="addcontactinfo" />
			<table>
			<tr>
				<td class="cell">First Name:</td>
				<td class="cell" colspan="3"><input type = "text" name="firstname" id="firstname" value="<% if (user != null) out.print(user.get("firstname")); %>" required/></td>
			</tr>
			<tr>
				<td class="cell">Last Name:</td>
				<td class="cell" colspan="3"><input type = "text" name="lastname" id="lastname" value="<% if (user != null) out.print(user.get("lastname")); %>" required/></td>
			</tr>
			<tr>
				<td class="cell">Email:</td>
				<td class="cell" colspan="3"><input type = "email" name="email" id="email" placeholder="somename@uwm.edu" value="<% if (user != null) out.print(user.get("email")); %>" pattern="^\w+@uwm.edu$" required /></td>
			</tr>
			<tr>
				<td class="cell">Street Address:</td>
				<td class="cell" colspan="3"><input type = "text" name="streetaddress" id="streetaddress" value="<% if (user != null) out.print(user.get("streetaddress")); %>" /></td>
			</tr>
			<tr>
				<td class="cell">City:</td>
				<td class="cell"><input type = "text" name="city" id="city" value="<% if (user != null) out.print(user.get("city")); %>" /></td>
				<td class="cell" style="padding-left:10px;">State:</td>
				<td class="cell">
						<jsp:include page="/WEB-INF/templates/stateselect.jsp">
					    	<jsp:param name="selected" value="<%=state %>" />
						</jsp:include>	
				</td>	
			</tr>
			<tr>
				<td class="cell">Zip Code:</td>
				<td class="cell" colspan="3"><input type = "text" name="zip" id="zip" value="<% if (user != null) out.print(user.get("zip")); %>" /></td>	
			</tr>
			<tr>
				<td class="cell">Phone Number:</td>
				<td class="cell" colspan="3"><input type = "text" name="phone" id="phone" value="<% if (user != null) out.print(user.get("phone")); %>" /></td>
			</tr>
			<tr>
				<td class="cell">Role:</td>
				<td class="cell" colspan="3"> 
					<select name="accesslevel" required>
					  <option value="">Please Select</option>
					  <option value="3" >TA</option>
					  <option value="2">INSTRUCTOR</option>
					  <option value="1">ADMIN</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="submitinfo" colspan="2"><input type="submit" class="submit" value="Add User Contact Info" /></td>
				<td colspan="3"></td>
			</tr>
			
			</table>
		</form>
		<ul class="message">
			<li>First Name, Last Name, Email, and Role is required.</li>
			<li>Email must be an UWM email in the format "someone@uwm.edu"</li>
			<li>Phone Number must be in a correct format<br />
		Area Code Follow By 7 Digit Phone Number<br />
		Each Section can be separated by space, comma or dash<br />
		(414) 123 4567, 414.123.4567, 414-123-4567, 4141234567</li>
		</ul>
	 <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="reloadclassschedule" class="modal">
    <div>
        <p><strong>Reload Class Schedule</strong></p>
		<ul class="message">
			<li>You are about to load the class schedule from UWM's website.</li>
		</ul>
		<form action="admin.html" method="post">
			<p><input type="checkbox" name="agreetoreload" /> I Agree</p>
			<p><input type="submit" class="submit" name="reloadclassschedule" value="Reload Schedule" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="triggernewsemester" class="modal">
    <div>
        <p><strong>Trigger Brand New Semester </strong></p>
		<ul class="message">
			<li>You are about to advance to the next semester.</li>
			<li class="error-message">This will remove all Instructor and TA class assignment!</li>
			<li class="error-message">This will repopulate the class schedule!</li>
		</ul>
		<form action="admin.html" method="post">
			<p><input type="checkbox" name="agreetotrigger" /> I Agree</p>
			<p><input type="submit" class="submit" name="triggernewsemester" value="Start New Semester" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="addnewusercomplete" class="modal">
    <div>
        <p><strong>Add New User Complete</strong></p>
			<span class="good-message">Add new user successfully!</span>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="addcontactinfocomplete" class="modal">
    <div>
        <p><strong>Add New Contact Information</strong></p>
			<span class="good-message">Add new contact information was successfully!</span>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<!--
**************************
End of CSS Pop Up
**************************
-->

<jsp:include page="/WEB-INF/templates/footer.jsp" />