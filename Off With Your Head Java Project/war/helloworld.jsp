<jsp:include page="header.jsp">
    <jsp:param name="title" value="Hello World" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="home.css" />
</jsp:include>

<jsp:include page="layout.jsp" />

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			<li><a class="nav-link" href="queryPage.html">Queries</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	
	${body}
	
	<br />
	
	<% for (String item : (String[]) request.getAttribute("var")) {
			out.println(item);  %> <br /> <% } %>
	
	</div>
</div>

<jsp:include page="footer.jsp" />