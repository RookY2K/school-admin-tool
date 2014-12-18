<%@ page import="edu.uwm.owyh.interfaces.WrapperObject"%>
<%@ page import="edu.uwm.owyh.jdo.Course" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.jdo.Section" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Layout Title" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="officehour.css" />
    <jsp:param name="stylesheet" value="classlist.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/homenavagation.jsp" />

<% List<Map<String, Object>> taclasses = (List<Map<String, Object>>) request.getAttribute("classes"); 
if (taclasses == null) { out.print("No Correct Attribute Was Passed Into JSP!"); return; }
List<String> errors = (List<String>) request.getAttribute("errors");
List<String> messages = (List<String>) request.getAttribute("messages");

Map<Integer, WrapperObject<Course>> courses = (Map<Integer, WrapperObject<Course>>)Auth.getSessionVariable(request, "courses");
List<Integer> courseKeyList = (List<Integer>) Auth.getSessionVariable(request, "coursekeys");
Integer selectedCourseNumber = (Integer) request.getAttribute("selectedCourseNumber");
%>

<div id="body">

	<div style="float:left;width:60%;">
		
		<ul class="message" style="margin-top:0px;">		
			<% if (errors != null) {
				for (String error : errors) {
					%> 
					<li class="error-message"><%=error %></li>
				<%
				}
			}
			%>	
			
			<% if (messages != null) {
				for (String message : messages) {
					%> 
					<li class="good-message"><%=message %></li>
				<%
				}
			}
			%>
		</ul>	
		
		<p><strong>TA Class Schedule</strong></p>

		<% if (courses !=null && courseKeyList != null) { %>
		
		<p style="text-decoration:underline;">Add NON-CompSCI class to your schedule</p>
		
    <form action="/taclass" method="post">
		<input type="hidden" name="addNonCSClass" value="addNonCSClass" />
	 	<table>	 
	 		<tr>  
		 		<td>Class Name:</td>
		 		<td colspan="2"><input type="text" name="classname" value="" required /></td>
	 		</tr>	
	 		<tr>  
		 		<td>Class Number:</td>
		 		<td colspan="2"><input type="text" name="classnum1" value="" pattern="^[a-zA-Z]+"  size="5" title="At least one letter, must contain no numbers." required /> 
		 		<input type="text" name="classnum2" value="" pattern="[0-9]{3,4}"  size="5" title="3 digit or 4 digit number" required /></td>
	 		</tr>	
	 		<tr>  
		 		<td>Class Type:</td>
		 		<td colspan="2"><select name="classtype" value="" required >
		 		<option value="LEC">LEC</option>
		 		<option value="DIS">DIS</option>
		 		<option value="LAB">LAB</option>
		 		<option value="IND">IND</option>
		 		<option value="SEM">SEM</option>
		 		</select>
		 		</td>
	 		</tr>		   
	   		<tr>
	   			<td>Days:</td>
		   		<td colspan="2">
		   			<input type="checkbox" name="M" value="M"> Mon
		   			<input type="checkbox" name="T" value="T"> Tues
		   			<input type="checkbox" name="W" value="W"> Wed
		   			<input type="checkbox" name="R" value="R"> Thur
		   			<input type="checkbox" name="F" value="F"> Fri
	   			</td>
	   		</tr>
	   		<tr>
	   		<tr>
	   			<td>Start Time:</td>
	   			<td colspan="2">
	   			 <select name="start_hour">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select>
					<select name="start_minute">
						<option value="00">00</option>
						<option value="05">05</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
						<option value="25">25</option>
						<option value="30">30</option>
						<option value="35">35</option>
						<option value="40">40</option>
						<option value="45">45</option>
						<option value="50">50</option>
						<option value="55">55</option>
					</select>
					<select name="start_ampm">
						<option value="AM">AM</option>
						<option value="PM">PM</option>
					</select>
	   			</td>
	   		</tr>
	   		<tr>
	   			<td>End Time:</td>
	   			<td>
		   			 <select name="end_hour">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select>
					<select name="end_minute">
						<option value="00">00</option>
						<option value="05">05</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
						<option value="25">25</option>
						<option value="30">30</option>
						<option value="35">35</option>
						<option value="40">40</option>
						<option value="45">45</option>
						<option value="50">50</option>
						<option value="55">55</option>
					</select>
					<select name="end_ampm">
						<option value="AM">AM</option>
						<option value="PM">PM</option>
					</select>
	   			</td>
				<td class="submitinfo">
					<input type="submit" class="submit" value="Add Class"/>
				</td>
			</tr>
		</table>
	</form>

	</div>

	<div style="float:right;width:38%;">
		<div class="officehour-tab"  style="min-height:220px; height:100%">
			<p><strong>My Class Schedule</strong></p>
			<table class="officehour-table">
				<% if (!taclasses.isEmpty()) { %>
				<tr style="text-align:left;">
					<td class="underline">Class</td><td class="underline">Days</td><td class="underline">Time</td>
				</tr>	
				<% } else { %>
				<tr style="text-align:left;">
					<td>You have no class scheduled.</td>
				</tr>	
				<% } %>				
				<% for (Map<String, Object> taclass : taclasses) { %>
				<tr>
					<td><%=taclass.get("classnum") %></td>
					<td><%=taclass.get("days") %></td>
					<td><%=taclass.get("starttime") %> - <%=taclass.get("endtime") %></td>
				</tr>
				
				<% } %>
			</table>
		</div>
	</div>
	
	<br class="clear" /><br /><p style="text-decoration:underline;">Add CompSCI class to your schedule</p>
	
	<form action="/taclass" method="post" />
	<select name="courselist" required>
		  <option value="">Select a course...</option>
	<%
			if(courseKeyList != null){
				for(int key : courseKeyList){
	%>
		  <option value="<%=key%>" <% if (selectedCourseNumber != null && selectedCourseNumber == key) { out.print("selected"); } %>>COMPSCI-<%=key%>: <% 
		  String courseName = (String) courses.get(key).getProperty("coursename");
		  if (courseName.length() > 40)
			  courseName = courseName.substring(0, 39) + "...";
			  out.print(courseName);
			  
		 %></option>
	<%
				}
		} 
	}
	%>
	</select> &nbsp;	
	<input type="submit" class="submit" value="Select" />
	</form>
	
	<br /><br />
	
<%
	WrapperObject<Course> selectedCourse = (WrapperObject<Course>)request.getAttribute("selectedcourse");
	boolean isAdmin = Auth.getAuth(request).verifyAdmin();
	if(selectedCourse != null){
		String courseNum = (String)selectedCourse.getProperty("coursenum");
		String courseName = (String)selectedCourse.getProperty("coursename");
		List<WrapperObject<Section>>sections = (List<WrapperObject<Section>>)selectedCourse.getProperty("sections");

%>	
	<table id="course_info_table">
		<thead>
			<tr id="section_headers">
				<th class="section_header_cell">Section</th>
				<th class="section_header_cell">Dates</th>
				<th class="section_header_cell">Days</th>
				<th class="section_header_cell">Hours</th>
				<th class="section_header_cell">Instructor</th>
				<th class="section_header_cell">Add</th>
			</tr>
		</thead>
		<tbody>
  	<%
  			int row = -1;
  			for(WrapperObject<Section> section : sections){
  				row++;
  				String className = row % 2 == 0 ? "evenrow" : "oddrow";
  				
			String sectionNum = (String)section.getProperty("sectionnum");
			String credits = (String)section.getProperty("credits");
			String startDate = (String)section.getProperty("startdate");
			String endDate = (String)section.getProperty("enddate");
			String dates = startDate + "-" + endDate;
			String days = (String)section.getProperty("days");
			String startTime = (String)section.getProperty("starttime");
			String endTime = (String)section.getProperty("endtime");
			String hours = startTime + "-" + endTime;
			String room = (String)section.getProperty("room");
			String firstName = (String)section.getProperty("instructorfirstname");
			String lastName = (String)section.getProperty("instructorlastname");
			String instructor = (lastName != null && lastName.trim().isEmpty()) ? firstName : lastName + ", " + firstName;
%>
			<tr class="<%=className %>">
				<td class="section_cell"><%=sectionNum %></td>
				<td class="section_cell"><%=dates %></td>
				<td class="section_cell"><%=days %></td>
				<td class="section_cell"><%=hours %></td>
				<td class="section_cell"><%=instructor %></td>
				<td class="section_cell">
					<form method="post" action="/taclass">
						<input type="hidden" name="addCSClass" value="<%=sectionNum %>" />
						<input type="hidden" name="courselist" value="<% if (selectedCourseNumber != null) { out.print(selectedCourseNumber); } %>" />
						<input type="submit" class="submit" value="Add Class" style="cursor: pointer;"/>
					</form>
				</td>
			</tr>
<%
  			}
%>			
		</tbody>
		</table>
				
<%
	}
%>	
	
	
	<br class="clear" />
	
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />
