
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Layout Title" />
    <jsp:param name="stylesheet" value="main.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/genericnavagation.jsp">
	<jsp:param name="content" value="Layout Name" />
</jsp:include>

<div id="body">

Your Stuff goes between body DIV. 
	
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />
