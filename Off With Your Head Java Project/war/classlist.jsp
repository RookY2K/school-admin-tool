<%@ page import="edu.uwm.owyh.interfaces.WrapperObject"%>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.model.DataStore" %>
<%@ page import="edu.uwm.owyh.jdo.Course" %>
<%@ page import="edu.uwm.owyh.jdo.Section" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="classlist.css" />
</jsp:include>
<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/genericnavagation.jsp">
	<jsp:param name="content" value="Class List" />
</jsp:include>


<%
	List<String> errors = (List<String>)Auth.getSessionVariable(request, "errors");
	Map<Integer, WrapperObject<Course>> courses = (Map<Integer, WrapperObject<Course>>)Auth.getSessionVariable(request, "courses");
	List<Integer> courseKeyList = (List<Integer>) Auth.getSessionVariable(request, "coursekeys");
%>

  	
<div id="body">
<%  
	if(errors != null){
		for(String error : errors){
%>
	<span class="error-message"><%=error %><br /></span>
<%
		}
	%><br /> <%
	}
	Auth.removeSessionVariable(request, "errors");
	
%>	
	<form method="post" action="/classlist">
			<table id="courselist_table">
			<tr>
				<td class="cell">Courses: </td>
				<td class="cell"> 
					<select name="courselist" required>
					  <option value="">Select a course...</option>
<%
						if(courseKeyList != null){
							for(int key : courseKeyList){
%>
					  <option value="<%=key%>">COMPSCI-<%=key%>: <%=courses.get(key).getProperty("coursename") %></option>
<%
							}
						}
%>
					</select> &nbsp;	
					<input type="submit" id="select_course" value="Select Course" />	
				</td>
			</tr>
			</table>
				
	</form>
<%
	WrapperObject<Course> selectedCourse = (WrapperObject<Course>)request.getAttribute("selectedcourse");
	boolean isAdmin = Auth.getAuth(request).verifyAdmin();
	if(selectedCourse != null){
		String courseNum = (String)selectedCourse.getProperty("coursenum");
		String courseName = (String)selectedCourse.getProperty("coursename");
		List<WrapperObject<Section>>sections = (List<WrapperObject<Section>>)selectedCourse.getProperty("sections");

%>	
	<br />
	<form method="post" action="/admin/editcourses">
		<table id="course_info_table">
		<thead>
			<tr>
				<th id="course_info_table_header" colspan="7">COMPSCI-<%=courseNum %>: <%=courseName %></th>
			</tr>
			<tr id="section_headers">
				<th class="section_header_cell">Section</th>
				<th class="section_header_cell">Credits</th>
				<th class="section_header_cell">Dates</th>
				<th class="section_header_cell">Days</th>
				<th class="section_header_cell">Hours</th>
				<th class="section_header_cell">Room</th>
				<th class="section_header_cell">Instructor</th>
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
			String instructor = lastName.trim().isEmpty() ? firstName : lastName + ", " + firstName;
%>
			<tr class="<%=className %>">
				<td class="section_cell"><%=sectionNum %></td>
				<td class="section_cell"><%=credits %></td>
				<td class="section_cell"><%=dates %></td>
				<td class="section_cell"><%=days %></td>
				<td class="section_cell"><%=hours %></td>
				<td class="section_cell"><%=room %></td>
				<td class="section_cell"><%=instructor %></td>
			</tr>
<%
  			}
%>			
		</tbody>
		</table>
	</form>
<%
	}
%>	
	
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />