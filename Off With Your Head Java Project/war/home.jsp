<%@ page import="edu.uwm.owyh.interfaces.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="edu.uwm.owyh.model.UserSchedule"%>
<%@ page import="edu.uwm.owyh.model.UserScheduleElement"%>
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
					<td class="time">8:00 AM</td>
						<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "8:00AM", "9:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "8:00AM", "9:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "8:00AM", "9:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "8:00AM", "9:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "8:00AM", "9:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">9:00 AM</td>	
						<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "9:00AM", "10:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "9:00AM", "10:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "9:00AM", "10:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "9:00AM", "10:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "9:00AM", "10:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">10:00 AM</td>	
						<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "10:00AM", "11:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "10:00AM", "11:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "10:00AM", "11:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "10:00AM", "11:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "10:00AM", "11:00AM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">11:00 AM</td>	
						<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "11:00AM", "12:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "11:00AM", "12:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "11:00AM", "12:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "11:00AM", "12:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "11:00AM", "12:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">12:00 PM</td>
					<% 
					for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "12:00PM", "1:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "12:00PM", "1:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "12:00PM", "1:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "12:00PM", "1:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "12:00PM", "1:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>	
				</tr>
				<tr>
					<td class="time">1:00 PM</td>
					<% 
					for(int i = 0; i < 5; i++)
						{
						count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "1:00PM", "2:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "1:00PM", "2:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "1:00PM", "2:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "1:00PM", "2:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "1:00PM", "2:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">2:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "2:00PM", "3:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "2:00PM", "3:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "2:00PM", "3:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "2:00PM", "3:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "2:00PM", "3:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">3:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "3:00PM", "4:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "3:00PM", "4:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "3:00PM", "4:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "3:00PM", "4:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "3:00PM", "4:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">4:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "4:00PM", "5:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "4:00PM", "5:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "4:00PM", "5:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "4:00PM", "5:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "4:00PM", "5:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">5:00 PM</td>
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "5:00PM", "6:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "5:00PM", "6:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "5:00PM", "6:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "5:00PM", "6:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "5:00PM", "6:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">6:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "6:00PM", "7:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "6:00PM", "7:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "6:00PM", "7:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "6:00PM", "7:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "6:00PM", "7:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">7:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "7:00PM", "8:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "7:00PM", "8:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "7:00PM", "8:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "7:00PM", "8:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "7:00PM", "8:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
				</tr>
				<tr>
					<td class="time">8:00 PM</td>	
					<%for(int i = 0; i < 5; i++)
						{
							count = 0;
							for(UserScheduleElement element : schedule)
							{
								if(i == 0)
								{
									if(element.isPartOfElement("M", "8:00PM", "9:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 1)
								{
									if(element.isPartOfElement("T", "8:00PM", "9:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 2)
								{
									if(element.isPartOfElement("W", "8:00PM", "9:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 3)
								{
									if(element.isPartOfElement("R", "8:00PM", "9:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
								
								if(i == 4)
								{
									if(element.isPartOfElement("F", "8:00PM", "9:00PM"))
									{
										++count;%>
										<td class="office-background"><span class="office-hour"><%= element.getTitle()%></span></td>
									<%}
								}
							}
							if(count != 1)
							{ %>
								<td class="hours"><span class="office-hour"></span></td>
							<%}
						}%>
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
				<span class="class-key">*Classes</span> <br />
				<span class="office-key">*Office Hours</span> 
			</p>
		</div>
	
	<br />

	</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />