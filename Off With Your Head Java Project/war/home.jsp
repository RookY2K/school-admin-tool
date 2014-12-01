<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
		<div id="calender">
			<table id="schedule">
				<tr>
					<td class="time">Time</td>
					<td class="days">Monday</td>
					<td class="days">Tuesday</td>
					<td class="days">Wednesday</td>
					<td class="days">Thursday</td>
					<td class="days">Friday</td>
				</tr>
				<tr>
					<td class="time">8:00 AM</td>
					<% for(int i = 0; i < 5; i++)
					{
						if (self != null)%>
						<td class="hours"></td><%
					}%>	

				</tr>
				<tr>
					<td class="time">9:00 AM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">10:00 AM</td>	
					<td class="hours"><!--<span class="class-hour">COMPSCI 361 Section 801</span>--></td>
					<td class="hours"></td>
					<td class="hours"><!--<span class="class-hour">COMPSCI 361 Section 801</span>--></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">11:00 AM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">12:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">1:00 PM</td>	
					<td class="hours"><!--<span class="office-hour">COMPSCI 361 EMS W301</span>--></td>
					<td class="hours"></td>
					<td class="hours"><!--<span class="office-hour">COMPSCI 361 EMS W301</span>--></td>
					<td class="hours"></td>
					<td class="hours"><!--<span class="office-hour">COMPSCI 361 EMS W301</span>--></td>
				</tr>
				<tr>
					<td class="time">2:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">3:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">4:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">5:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">6:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">7:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">8:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">9:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
				<tr>
					<td class="time">10:00 PM</td>	
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
					<td class="hours"></td>
				</tr>
			</table>
			<p>
				<span class="class-hour">*Classes</span> <br />
				<span class="office-hour">*Office Hours</span> 
			</p>
		</div>
	
	<br />

	</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />