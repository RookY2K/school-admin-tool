<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Profile Manager" />
    <jsp:param name="stylesheet" value="main.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/adminnavation.jsp" />

<% 	Map<String, Object> goodAddUser = (Map<String, Object>) request.getAttribute("newuser");
	List<String> addNewUserErrors = (List<String>) request.getAttribute("addnewusererrors");
%>

<div id="body">

	<a href="#addnewuser">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/adduser.png');"></div>
			<p>Add A New User</p>
		</div>
	</a>
	
	<a href="#addcontactinformation">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/contactinfo.png');"></div>
			<p>Add Contact Information</p>
		</div>
	</a>
	
	<a href="#reloadclassschedule">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/reloadclasslist.png');"></div>
			<p>Reload Class List</p>
		</div>
	</a>
	
	<a href="#triggernewsemester">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/triggernewsemester.png');"></div>
			<p>Trigger New Semester</p>
		</div>
	</a>
	
	<br class="clear" />
	
	<a href="#emailupdateprofile">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/emailreminderprofile.png');"></div>
			<p>Email All User Reminder To Update Profile</p>
		</div>
	</a>
	
	<a href="#emailtalabs">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/emailreminderlabs.png');"></div>
			<p>Email Instructor Reminder to assign TA to Labs for courses they are instructing</p>
		</div>
	</a>
	
	<a href="#emailtaschedule">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/emailremindertaclasses.png');"></div>
			<p>Email All TA Reminder to Fill Out There Class Schedule</p>
		</div>
	</a>
	
	<a href="#emailofficehour">
		<div class="admin-tab">
			<div class="admin-tab-box" style="background-image:url('/images/emailreminderofficehours.png');"></div>
			<p>Email All User Reminder To Update Office Information</p>
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
		<form action="/admin#addnewuserconfirm" method="post">
			<input type="hidden" name="addnewuser" value="addnewuser" />
			<table>
				<tr>
					<td class="user-label">First Name:</td>
		      		<td class="cell" colspan="3"><input type = "text" name="firstname" value="" required/></td>
				</tr>
				<tr>
					<td class="user-label">Last Name:</td>
					<td class="user-data" colspan="3"><input type = "text" name="lastname" value="" required/></td>
				</tr>
				<tr>
					<td class="user-label">Email:</td>
					<td class="user-data"><input type="text" name="email" value="" placeholder="someone@uwm.edu" pattern="^\w+@uwm.edu$" required /></td>
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
		<ul class="message">
			<li class="list-message">All Fields are required to add a new user account.</li>
			<li class="list-message">Email must be an UWM email in the format "someone@uwm.edu"</li>
		</ul>
        <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="addcontactinformation" class="modal">
    <div>
    	<p><strong>Add Contact Information</strong></p>
		<form action="/admin#addcontactinformationconfirm" method="post">
			<input type="hidden" name="addcontactinfo" value="addcontactinfo" />
			<table>
			<tr>
				<td class="cell">First Name:</td>
	      		<td class="cell" colspan="3"><input type = "text" name="firstname" value="" required/></td>
			</tr>
			<tr>
				<td class="cell">Last Name:</td>
				<td class="cell" colspan="3"><input type = "text" name="lastname" value="" required/></td>
			</tr>
			<tr>
				<td class="cell">Email:</td>
				<td class="cell" colspan="3"><input type = "email" name="email" placeholder="somename@uwm.edu" value="" pattern="^\w+@uwm.edu$" required /></td>
			</tr>
			<tr>
				<td class="cell">Street Address:</td>
				<td class="cell" colspan="3"><input type = "text" name="streetaddress" value="" /></td>
			</tr>
			<tr>
				<td class="cell">City:</td>
				<td class="cell"><input type = "text" name="city" value="" /></td>
				<td class="cell" style="padding-left:10px;">State:</td>
				<td class="cell">
						<jsp:include page="/WEB-INF/templates/stateselect.jsp">
					    	<jsp:param name="selected" value="" />
						</jsp:include>	
				</td>	
			</tr>
			<tr>
				<td class="cell">Zip Code:</td>
				<td class="cell" colspan="3"><input type = "text" name="zip" value="" /></td>	
			</tr>
			<tr>
				<td class="cell">Phone Number:</td>
				<td class="cell" colspan="3"><input type = "text" name="phone" value="" /></td>
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
			<li class="list-message">First Name, Last Name, Email, and Role is required.</li>
			<li class="list-message">Email must be an UWM email in the format "someone@uwm.edu"</li>
			<li class="list-message">Phone Number must be in a correct format<br />
		Area Code Follow By 7 Digit Phone Number<br />
		Each Section can be separated by space, comma or dash<br />
		(414) 123 4567, 414.123.4567, 414-123-4567, 4141234567</li>
		</ul>
	 <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="addnewuserconfirm" class="modal">
    <div>
        <p><strong>Add User Account</strong></p>
		
		<% if (addNewUserErrors != null) { %>
			<ul class="message">
		<%	for (String error : addNewUserErrors) { %>
		<li class="error-message"><%=error %></li>
		<% } %>
			</ul>
		<% } %>
		
		<% if (goodAddUser != null && (addNewUserErrors == null || addNewUserErrors.isEmpty())) { %>
		<p><span class="good-message">The new user <%=goodAddUser.get("email") %> was successfully added!</span> 
		<form action="/userlist#edituserprofile" method="post" >
		<input type="hidden" name="modifyuser" value="<%=goodAddUser.get("email") %>" />
		<input type="submit" class="submit" value="Edit User's Profile" /></form> </p>
		<p><form action="/userlist" method="get" >
		<input type="submit" class="submit" value="Go To Userlist" /></form></p>
		<% } %>
		
		<form action="/admin#addnewuserconfirm" method="post">
			<input type="hidden" name="addnewuser" value="addnewuser" />
			<table>
				<tr>
					<td class="user-label">First Name:</td>
		      		<td class="cell" colspan="3"><input type = "text" name="firstname" value="" required/></td>
				</tr>
				<tr>
					<td class="user-label">Last Name:</td>
					<td class="user-data" colspan="3"><input type = "text" name="lastname" value="" required/></td>
				</tr>
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
		<ul class="message">
			<li class="list-message">All Fields are required to add a new user account.</li>
			<li class="list-message">Email must be an UWM email in the format "someone@uwm.edu"</li>
		</ul>
        <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="addcontactinformationconfirm" class="modal">
    <div>
		<p><strong>Add Contact Information</strong></p>
		<% Map<String, Object> user = (Map<String, Object>) request.getAttribute("userinfo");
			Map<String, Object> goodContactInfo = (Map<String, Object>) request.getAttribute("newuser");
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
		
		<% if (goodAddUser != null && (addNewUserErrors == null || addNewUserErrors.isEmpty())) { %>
		<p><span class="good-message">The new user <%=goodAddUser.get("email") %> was successfully added!</span> 
		<form action="/userlist#edituserprofile" method="post" >
		<input type="hidden" name="modifyuser" value="<%=goodAddUser.get("email") %>" />
		<input type="submit" class="submit" value="Edit User's Profile" /></form></p>
		<% } %>
		
		<form action="/admin#addcontactinformation" method="post">
			<input type="hidden" name="addcontactinfo" value="addcontactinfo" />
			<table>
			<tr>
				<td class="cell">First Name:</td>
				<td class="cell" colspan="3"><input type = "text" name="firstname" value="<% if (user != null) out.print(user.get("firstname")); %>" required/></td>
			</tr>
			<tr>
				<td class="cell">Last Name:</td>
				<td class="cell" colspan="3"><input type = "text" name="lastname"  value="<% if (user != null) out.print(user.get("lastname")); %>" required/></td>
			</tr>
			<tr>
				<td class="cell">Email:</td>
				<td class="cell" colspan="3"><input type = "email" name="email" placeholder="somename@uwm.edu" value="<% if (user != null) out.print(user.get("email")); %>" pattern="^\w+@uwm.edu$" required /></td>
			</tr>
			<tr>
				<td class="cell">Street Address:</td>
				<td class="cell" colspan="3"><input type = "text" name="streetaddress" value="<% if (user != null) out.print(user.get("streetaddress")); %>" /></td>
			</tr>
			<tr>
				<td class="cell">City:</td>
				<td class="cell"><input type = "text" name="city" value="<% if (user != null) out.print(user.get("city")); %>" /></td>
				<td class="cell" style="padding-left:10px;">State:</td>
				<td class="cell">
						<jsp:include page="/WEB-INF/templates/stateselect.jsp">
					    	<jsp:param name="selected" value="<%=state %>" />
						</jsp:include>	
				</td>	
			</tr>
			<tr>
				<td class="cell">Zip Code:</td>
				<td class="cell" colspan="3"><input type = "text" name="zip" value="<% if (user != null) out.print(user.get("zip")); %>" /></td>	
			</tr>
			<tr>
				<td class="cell">Phone Number:</td>
				<td class="cell" colspan="3"><input type = "text" name="phone" value="<% if (user != null) out.print(user.get("phone")); %>" /></td>
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
			<li class="list-message">First Name, Last Name, Email, and Role is required.</li>
			<li class="list-message">Email must be an UWM email in the format "someone@uwm.edu"</li>
			<li class="list-message">Phone Number must be in a correct format<br />
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
		<form action="/admin#reloadclassschedule" method="post">
			<p><input type="checkbox" name="agreetoreload" required /> I Agree</p>
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
		<form action="/admin#triggernewsemester" method="post">
			<p>
			<table>
				<tr>
					<td class="cell">Next Semester:</td>
					<td class="cell" colspan="3"> 
					<select name="semester" required>
					    <option value="">Please Select</option>
						<option value="Spring">Spring</option>
						<option value="Summer">Summer</option>
						<option value="Fall">Fall</option>
						<option value="UWinteriM ">UWinteriM</option>
					</select> 
					</td>
				</tr>				
		    </table>
		    </p>
			<p><input type="checkbox" name="agreetotrigger" required /> I Agree</p>
			<p><input type="submit" class="submit" name="triggernewsemester" value="Start New Semester" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>

<aside id="emailupdateprofile" class="modal">
    <div>
        <p><strong>Email All User Reminder To Update Profile</strong></p>
		<ul class="message">
			<li>This will mass email all users and ask them to update there Profile.</li>
		</ul>
		<form action="/admin#emailsent" method="post">
			<p><input type="checkbox" name="agreetotrigger" required /> I Agree</p>
			<p><input type="submit" class="submit" name="emailupdateprofile" value="Email Users" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>

<aside id="emailtalabs" class="modal">
    <div>
        <p><strong>Email Instructor Reminder to assign TA to Labs for courses they are instructing</strong></p>
		<ul class="message">
			<li>This will mass email all Instructor and ask them to add TA to the labs for the course they are instructing.</li>
		</ul>
		<form action="/admin#emailsent" method="post">
			<p><input type="checkbox" name="agreetotrigger" required /> I Agree</p>
			<p><input type="submit" class="submit" name="emailtalabs" value="Email Users" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>

<aside id="emailtaschedule" class="modal">
    <div>
        <p><strong>Email All TA Reminder to Fill Out Their Class Schedule</strong></p>
		<ul class="message">
			<li>This will mass email all TA and ask fill out their class schedule.</li>
		</ul>
		<form action="/admin#emailsent" method="post">
			<p><input type="checkbox" name="agreetotrigger" required /> I Agree</p>
			<p><input type="submit" class="submit" name="emailtaschedule" value="Email Users" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>

<aside id="emailofficehour" class="modal">
    <div>
        <p><strong>Email All User Reminder To Update Office Information</strong></p>
		<ul class="message">
			<li>This will mass email all users and ask them to update there office hours.</li>
		</ul>
		<form action="/admin#emailsent" method="post">
			<p><input type="checkbox" name="agreetotrigger" required /> I Agree</p>
			<p><input type="submit" class="submit" name="emailofficehour" value="Email Users" /></p>
		</form>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>
<aside id="emailsent" class="modal">
    <div>
        <p><strong>Email</strong></p>
		<ul class="message">
			<li class="good-message">Your Email has been sent.</li>
		</ul>
       <a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>

<!--
**************************
End of CSS Pop Up
**************************
-->

<jsp:include page="/WEB-INF/templates/footer.jsp" />