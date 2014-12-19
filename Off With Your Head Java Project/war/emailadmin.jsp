
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Contact the Administrator" />
    <jsp:param name="stylesheet" value="main.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/genericnavagation.jsp">
	<jsp:param name="content" value="Contact the Administrator" />
</jsp:include>

<div id="body">

    	<form action="/emailadmin" method="post">
    	<input type="hidden" name="returnURI" value="<%=request.getRequestURI() %>" />
    	<input type="hidden" name="targetEmail" value="<%=request.getAttribute("myemail") %>" />
    	<textarea name="message" rows="6" cols="40"  placeholder="Your Message" required ></textarea><br /><br />
    	<input type="submit" class="submit" name="submitEmail" value="Send Email" />
    	</form>
	
</div>

<!-- CSS Modal Start Here -->
<jsp:include page="/WEB-INF/templates/emailadminmodal.jsp" />

<jsp:include page="/WEB-INF/templates/footer.jsp" />
