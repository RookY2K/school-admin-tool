<%@ page import="edu.uwm.owyh.interfaces.WrapperObject"%>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.model.DataStore" %>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
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
					<input type="submit" class="submit" id="select_course" value="Select Course" />	
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
	<ul class="message"><li>Click on Edit or Instructor Name to change Instructor for a Section.</li>
	<li>Instructor can only change LAB TA if they are assign to a LEC.</li>
	</ul>
	
		<table id="course_info_table">
		<form method="post" action="/admin/editcourses">
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
		</form>
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
				<td class="section_cell"><%=credits %></td>
				<td class="section_cell"><%=dates %></td>
				<td class="section_cell"><%=days %></td>
				<td class="section_cell"><%=hours %></td>
				<td class="section_cell"><%=room %></td>
				<td class="section_cell">
				<% if (isAdmin || (request.getAttribute("isInstructorOfSection") != null && !isAdmin && sectionNum.indexOf("LEC") < 0)) { %>
					<form action="#editsection" method="post">
						<input type="hidden" name="viewsection" value="viewsection" />
						<input type="hidden" name="courselist" value="<%=selectedCourse.getProperty("coursenum") %>" />
						<input type="hidden" name="sectionnumber" value="<%=sectionNum %>" />
						<% if (firstName == null || firstName.equals("")) { %>
						<input type="submit" class="classlistedit" value="Edit" style="color:#3c6c91;" />
						<% } else { %>
						<input type="submit" class="classlistedit" value="<%=instructor %>" style="color:#3c6c91;"  />
						<% } %>
					</form>
				<% } else { %>
				<input type="submit" class="classlistedit" value="<%=instructor %>" style="cursor: default;" />
				<% } %>
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
	
</div>

<aside id="editsection" class="modal">
    <div>
    	
    	<p><strong>Edit Section's User Instructing</strong></p>
    	
    	<% WrapperObject<Section> editSection =  (WrapperObject<Section>) request.getAttribute("editsection");
    	List<WrapperObject<Person>> possiableUser = (List<WrapperObject<Person>>) request.getAttribute("editsectionusers");
    	if (editSection != null) {
    	List<String> editSectionErrors = (List<String>) request.getAttribute("editsectionerrors");
    	List<String> editSectionMessages = (List<String>) request.getAttribute("editsectionmessages");
		if (editSectionErrors != null) { %>
		<ul class="message">
		<%	for (String error : editSectionErrors) { %>
			<li class="error-message"><%=error %></li>
			<% } %>
		</ul>
		<% } 
		if (editSectionMessages != null) { %>
		<ul class="message">
		<%	for (String message : editSectionMessages) { %>
			<li class="good-message"><%=message %></li>
			<% } %>
		</ul>
		<% } %>
		
		<ul class="message"><li>
		<% if (editSection.getProperty("instructorFirstName") != null && !editSection.getProperty("instructorFirstName").equals("")) { %>
		<strong><%=editSection.getProperty("instructorFirstName") %> <%=editSection.getProperty("instructorLastName") %></strong> is currently teaching this course.
		<% } else { %>
		No instructor currently teaching this course.
		<% } %>
		</li></ul>
		
		<% if (possiableUser == null || possiableUser.size() == 0) { %>
			You can assign no User to this Course or Section.
		<% } else { %>
		
 		<form action="#editsection" method="post">
 			<input type="hidden" name="viewsection" value="viewsection" />
 			<input type="hidden" name="editsection" value="editsection" />
			<input type="hidden" name="courselist" value="<%=selectedCourse.getProperty("coursenum")%>" />
			<input type="hidden" name="sectionnumber" value="<%=editSection.getProperty("sectionnum") %>" />
			<select name="changeinstructor" required>
			<option value="">Change Section Instructor...</option>
			<% for (WrapperObject<Person> user : possiableUser) { %>
			<option value="<%=user.getProperty("email") %>">
			<% if (user.getProperty("firstname") != null && user.getProperty("lastname") != null) out.print(user.getProperty("lastname") + ", " + user.getProperty("firstname")); 
			else out.print(user.getProperty("email")); %>
			</option>
			<% } %>
			</select>
			<input type="submit" class="submit" value="Change" />
		</form>
		
		<% } } %>
    	
		<a href="#close" title="Close" class="unselectable">Close</a>
    </div>
</aside>

<jsp:include page="/WEB-INF/templates/footer.jsp" />