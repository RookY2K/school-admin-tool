<jsp:include page="/header.jsp">
    <jsp:param name="title" value="Hello World" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/layout.jsp" />

<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="admin/addUser">Add User</a></li>
	      <li><a class="nav-link" href="admin/addCourses">Assign Courses</a></li>
	      <li><a class="nav-link" href="admin/getCourseInfo">Get Course Info</a></li>
	      <li><a class="nav-link" href="admin/triggerNewSemester">Trigger New Semester</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	
		<form>
			<fieldset>
				<legend> Add Users</legend>
				<table>
				<tr>
					<td class="cell"><label class="field" for="firstName">First Name:</label></td>
					<td class="cell"><input type = "text" name="firstName" id="firstName"/>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="lastName">Last Name:</label></td>
					<td class="cell"><input type = "text" name="lastName" id="lastName"/>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="email">Email:</label></td>
					<td class="cell"><input type = "text" name="email" id="email"/>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="phoneNumber">Phone Number:</label></td>
					<td class="cell"><input type = "text" name="phoneNumber" id="phoneNumber"/>
				</tr>
				<tr>
					<td class="cell" colspan="2"><input type="submit" value="Create New User" /></td>
				</tr>
				</table>
			</fieldset>
		</form>
	
	</div>
</div>

<jsp:include page="/footer.jsp" />