<%@ page import="edu.uwm.owyh.interfaces.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="edu.uwm.owyh.model.UserSchedule"%>
<%@ page import="edu.uwm.owyh.model.UserScheduleElement"%>
<%@ page import="edu.uwm.owyh.model.CellObject"%>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="home.css" />
</jsp:include>
<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/genericnavagation.jsp">
	<jsp:param name="content" value="Welcome Back!" />
</jsp:include>

<% Map<String, Object> self = (Map<String, Object>)request.getAttribute("self");

if (self == null) { out.print("No Correct Attribute Was Passed Into JSP!"); return; }
%>
	
	<div id="body">
		<div id="welcome">
			<div style="float:right;margin-right: 10px;">
				<a href="print.html" target="_blank">Print Schedule</a>
			</div>
		</div>
			<jsp:include page="/WEB-INF/templates/Calendar.jsp">
				<jsp:param name="role" value='<%=self.get("accesslevel") %>'/>
			</jsp:include>
	<br />

	</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />