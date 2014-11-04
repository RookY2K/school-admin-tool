<%@ page import="edu.uwm.owyh.model.Auth" %>


<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Hello World" />
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
	

	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />