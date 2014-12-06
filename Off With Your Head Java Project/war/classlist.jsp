<%@ page import="edu.uwm.owyh.jdo.Course" %>
<%@ page import="edu.uwm.owyh.jdo.Section" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.model.DataStore" %>
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
	Map<Integer, Course> courses = (Map<Integer, Course>)Auth.getSessionVariable(request, "courses");
	List<Integer> courseKeyList = (List<Integer>) Auth.getSessionVariable(request, "coursekeys");
%>

  	
<div id="body">
<%  
	if(errors != null){
		for(String error : errors){
%>
	<span class="error-message"><br /><%=error %></span>
<%
		}
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
					  <option value="<%=key%>">COMPSCI-<%=key%>: <%=courses.get(key).getCourseName() %></option>
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
	Course selectedCourse = (Course)request.getAttribute("selectedcourse");
	boolean isAdmin = Auth.getAuth(request).verifyAdmin();
	if(selectedCourse != null){
		String courseNum = selectedCourse.getCourseNum();
		String courseName = selectedCourse.getCourseName();
		List<Section>sections = selectedCourse.getSections();

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
  	  			for(Section section : sections){
  	  				row++;
  	  				String className = row % 2 == 0 ? "evenrow" : "oddrow";
  	  				
  		String sectionNum = section.getSectionNum();
  		String credits = section.getCredits();
  		String dates = section.getDates();
  		String days = section.getDays();
  		String hours = section.getHours();
  		String room = section.getRoom();
  		String instructor = section.getInstructorName();
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