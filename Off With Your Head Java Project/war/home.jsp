<%@ page import="edu.uwm.owyh.model.WrapperObject" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Hello World" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="home.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

	<% WrapperObject user = (WrapperObject) Auth.getSessionVariable(request, "user");
		if(user == null) return; 
	%>	

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			
			<% if(user.getProperty("accesslevel").equals(WrapperObject.AccessLevel.TA))
			{%>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			<% } %>
			
			<li><a class="nav-link" href="queryPage.html">Queries</a></li>
		</ul>
	</div>
	  	
	<div id="body">
		<div id="welcome">
			<span class="large-title" style="margin-left: 10px;"> Welcome Back <%=user.getProperty("firstname") %>! </span>
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
						if (user != null)%>
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
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />