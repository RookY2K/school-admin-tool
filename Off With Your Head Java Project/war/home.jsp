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
int count = 0;
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
					<td class="time" rowspan = "2">8:00 AM</td>				
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][0];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}		
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][1];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>
				<tr>
					<td class="time" rowspan = "2">9:00 AM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][2];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}														
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][3];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>
				<tr>
					<td class="time" rowspan = "2">10:00 AM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][4];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>	
							<%}													
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][5];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>
				<tr>
					<td class="time" rowspan = "2">11:00 AM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][6];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>	
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][7];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>
				<tr>
					<td class="time" rowspan = "2">12:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][8];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>	
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][9];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">1:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][10];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][11];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">2:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][12];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][13];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						}%>				
				</tr>
				<tr>
					<td class="time" rowspan = "2">3:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][14];
							if(cell != null)
							{%>
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>							
							<%}													
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][15];
							if(cell != null)
							{%>
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}										
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">4:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][16];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][17];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">5:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][18];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][19];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">6:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][20];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][21];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">7:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][22];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][23];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">8:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<% cell = array[i][24];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][25];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">9:00 PM</td>
					<% for(int i = 0; i < 5; i++)
						{ %>
							<% cell = array[i][26];
							if(cell != null)
							{ %>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						} %>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][27];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}	
						}%>				
				</tr>				
				<tr>
					<td class="time" rowspan = "2">10:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][28];
							if(cell != null)
							{%>							
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<%for(int i = 0; i < 5; i++)
						{%>
							<%cell = array[i][29];
							if(cell != null)
							{%>						
								<td class = "<%=cell.getType()%>" rowspan = "<%=cell.getRowSpan()%>"><span><%=cell.getTitle()%> <br> <%=cell.getHours()%> <br> <%=cell.getRoom()%></span></td>
							<%}
						}%>				
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