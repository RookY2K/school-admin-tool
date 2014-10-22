<jsp:include page="header.jsp">
    <jsp:param name="title" value="Hello World" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="layout.jsp" />

<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="addUsers.html">Add Users</a></li>
	      <li><a class="nav-link" href="assignCourses.html">Assign Courses</a></li>
	      <li><a class="nav-link" href="index.html">Get Course Info</a></li>
	      <li><a class="nav-link" href="index.html">Trigger New Semester</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	

	
	</div>
</div>

<%@ include file="footer.jsp" %> 