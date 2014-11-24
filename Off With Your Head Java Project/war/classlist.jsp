<%@ page import="edu.uwm.owyh.mockjdo.Course" %>
<%@ page import="edu.uwm.owyh.mockjdo.Section" %>
<%@ page import="java.util.List" %>
<%! @SuppressWarnings("unchecked") %>
<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Class List" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="classlist.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />
<%
	List<Course> courses = (List<Course>)request.getAttribute("courses");
	List<String> errors = (List<String>)request.getAttribute("errors");
	if(courses == null){
		errors = (List<String>)request.getAttribute("errors");
	}
%>


<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
		<!-- 
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			<li><a class="nav-link" href="queryPage.html">Queries</a></li>
		 -->
		</ul>
	</div>
	  	
	<div id="body">
	<%  
		if(errors != null){
			for(String error : errors){
	%>
		<span class="error-message"><br /><%=error %></span>
	<%
			}
		}
		if(courses != null){
			for(int i=0; i<courses.size(); ++i){
				Course course = courses.get(i);
				String courseNum = course.getCourseNum();
				String courseName = course.getCourseName();
				List<Section> sections = course.getSections();
	%>
		<span class="course_info"><%=courseNum%>&nbsp;<%=courseName %></span>
		<%
				for(int j=0; i<sections.size(); ++j){
					Section section = sections.get(i);
					String sectionNum = section.getSectionNum();
					String creditLoad = section.getCreditLoad();
					String days = section.getDays();
					String hours = section.getHours();
					String dates = section.getDates();
					String room = section.getRoom();
					String instructorName = section.getInstructorName();		
		%>
		<table class="section_table">
			<thead>
				<tr>
					<th class="section_header">Section Number</th>
					<th class="section_header">Units</th>
					<th class="section_header">Hours</th>
					<th class="section_header">Days</th>
					<th class="section_header">Dates</th>
					<th class="section_header">Instructor</th>
					<th class="section_header">Room</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="section_info"><%=sectionNum %></td>
					<td class="section_info"><%=creditLoad %></td>
					<td class="section_info"><%=hours %></td>
					<td class="section_info"><%=days %></td>
					<td class="section_info"><%=dates %></td>
					<td class="section_info"><%=instructorName %></td>
					<td class="section_info"><%=room %></td>
				</tr>
			</tbody>
		
		</table><br />
	<%
				}	
			}
	%>
		<br />
	<%
		}
	%>
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />