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
UserSchedule schedule = (UserSchedule) request.getAttribute("userschedule");
CellObject[][] array = (CellObject[][]) request.getAttribute("array");
CellObject cell;
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
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][0];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">9:00 AM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][2];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">10:00 AM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][4];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">11:00 AM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][6];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">12:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][8];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>	
				</tr>
				<tr>
					<td class="time">1:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][10];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">2:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][12];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">3:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][14];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">4:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][16];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">5:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][18];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">6:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][20];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">7:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][22];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">8:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][24];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">9:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][26];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
				<tr>
					<td class="time">10:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][28];%>							
							<td class = "<%=cell.getType()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
						<%}%>
				</tr>
			</table>
			<p>
				<span class="class-key">*Classes</span> <br />
				<span class="office-key">*Office Hours</span> 
			</p>
		</div>
	
	<br />

	</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />