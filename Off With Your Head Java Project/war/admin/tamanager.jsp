<%@ page import="edu.uwm.owyh.interfaces.WrapperObject"%>
<%@ page import="edu.uwm.owyh.factories.WrapperObjectFactory"%>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
<%@ page import="edu.uwm.owyh.jdo.Course" %>
<%@ page import="edu.uwm.owyh.jdo.Section" %>
<%@ page import="edu.uwm.owyh.jdo.TAClass" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="TA Mangaer" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="tamanager.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/adminnavation.jsp" />

<div id="body">

<% List<Map<String, Object>> taList = (List<Map<String, Object>>) request.getAttribute("listofta");
List<String> taSkills = (List<String>) request.getAttribute("listoftaskills");
WrapperObject<Course> selectedCourse = (WrapperObject<Course>) request.getAttribute("selectedCourse");
List<Map<String, Object>> taFromSelectedCourseList = (List<Map<String, Object>>) request.getAttribute("taFromSelectedCourseList");

if (taList == null) { out.print("No Correct Attribute Was Passed Into JSP!"); return; }

List<String> errors =  (List<String>) request.getAttribute("errors");
List<String> messages =  (List<String>) request.getAttribute("messages");

Map<Integer, WrapperObject<Course>> courses = (Map<Integer, WrapperObject<Course>>)Auth.getSessionVariable(request, "courses");
List<Integer> courseKeyList = (List<Integer>) Auth.getSessionVariable(request, "coursekeys");
Integer selectedCourseNumber = (Integer) request.getAttribute("selectedCourseNumber");
List<String> skillSelectList = (List<String>) Auth.getSessionVariable(request, "skillSelectList");
%>

<% if (errors != null) { %>
	<ul class="message">
	<% for (String error : errors) { %>
		<li class="error-message"><%=error %></li>
	<% } %>
	</ul>
<% } %>

<% if (messages != null) { %>
	<ul class="message">
	<% for (String message : messages) { %>
		<li class="good-message"><%=message %></li>
	<% } %>
	</ul>
<% } %>

<p><strong>Skills</strong></p>
<form action="/admin/tamanager" method="post" style="display:inline">
<% if (taSkills != null && !taSkills.isEmpty()) {%>
 
<%	int i = 0;
for (String skill : taSkills) { 
	String checked = "";
	 if (skillSelectList != null) {
		for (String skillSelect : skillSelectList) { 
			if (skill.equalsIgnoreCase(skillSelect)) { checked = "checked"; break; } 
	 	} 
	} 
%>
<input type="hidden" name="skillcount" value="<%=taSkills.size() %>" />
<input type="checkbox" name="skill<%=i %>" value="<%=skill %>" <%=checked %> /> <%=skill %>
<% i++; }
} else {%>
There is currently no skills listed for any TA. You should go to <a href="/userlist">Userlist</a> to add skills to TA.
<% } %>
<br /><br />

<% if (courses !=null && courseKeyList != null) { %>

	<select name="courselist" required>
	  <option value="">Select a course...</option>
	  <option value="-72" <% if (selectedCourseNumber != null && selectedCourseNumber == -72) { out.print("selected"); } %>>Any Course</option>
<%
		if(courseKeyList != null){
			for(int key : courseKeyList){
%>
	  <option value="<%=key%>" <% if (selectedCourseNumber != null && selectedCourseNumber == key) { out.print("selected"); } %>>COMPSCI-<%=key%>: <%=courses.get(key).getProperty("coursename") %></option>
<%
		
	} 
}
%>
	</select> &nbsp;	
<% } %>					
	<input type="submit" class="submit" value="Filter TA"/>  &nbsp; 
</form>
<form action="/admin/tamanager" method="post" style="display:inline">
	<input type="hidden" name="skillclear" />
	<input type="submit" class="submit" value="Clear Filter"/>	
</form>

<ul class="message">
	<li>Checking no skills will select every skills, same as checking every skills.</li>
	<li>All skills checked are searched inclusively.</li>
	<li>Go to <a href="/userlist">Userlist</a> to change or add new skills to TAs.</li>
</ul>

<% if (selectedCourseNumber != null && taFromSelectedCourseList != null) { %>
<p><strong>TA currently assign to selected course.</strong></p>
<% if (taFromSelectedCourseList.isEmpty()) { %>
No TA is currently assigned to this course.
<% } else { %>
<table id="users">
	<tr>
		<td class="cell-header">Last Name</td>
		<td class="cell-header">First Name</td>
		<td class="cell-header">Email</td>
		<td class="cell-header">Skills</td>
		<td class="cell-header">Class Instructing</td>
		<td class="cell-header">Class Taking</td>
		<td class="cell-header">Modify</td>
	</tr>
<% int i = 0;
for (Map<String, Object> ta : taFromSelectedCourseList) {
	i++;
		String skills = "";
		if (ta.get("skills") != null) {
			for (String skill : (List<String>) ta.get("skills"))
				skills += skill + "<br /> ";
		}
		if (skills.endsWith("<br /> "))
			skills = skills.substring(0, skills.length() - 7);
%> 

	<tr class="line<%=i % 2 %>">
		<td class="cell"><%=ta.get("lastname") %></td>
		<td class="cell"><%=ta.get("firstname") %></td>
		<td class="cell"><%=ta.get("email") %></td>
		<td class="cell"><%=skills %></td>
		<td class="cell">
		<% List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) ta.get("sections");
			if ( sections  != null) {
				for (WrapperObject<Section> section : sections) { 					
					WrapperObject<Course> course = WrapperObjectFactory.getCourse().findObjectById(section.getId().getParent());
				%>
				COMPSCI-<%=course.getProperty("coursenum") %><br />
					<%=section.getProperty("days") %>
					<%=section.getProperty("starttime") %>-<%=section.getProperty("endtime") %>
					<br /><br />
				<% }
		} %>
		</td>
		<td class="cell">
					<% List<WrapperObject<TAClass>> taClasses = (List<WrapperObject<TAClass>>) ta.get("taclasses");
			if (taClasses  != null) {
				for (WrapperObject<TAClass> taClass : taClasses) {
				%>
					<%=taClass.getProperty("classnum") %><br />
					<%=taClass.getProperty("days") %>
					<%=taClass.getProperty("starttime") %>-<%=taClass.getProperty("endtime") %>
					<br /><br />
				<% }
		} %>
		</td>
		<td class="cell">
		<form action="/admin/tamanager" method="post">
			<input type="hidden" name="removeTAfromCourse" value="<%=selectedCourseNumber %>" />
			<input type="hidden" name="taEmail" value="<%=ta.get("email") %>" />
			<input type="hidden" name="courselist" value="<%=selectedCourseNumber %>" />
			<input type="submit" name="submit" class="submit" value="Remove From" />
		</form>
		</td>
	</tr>

<% 		}
	} 
} %>
	</table>

<p><strong>TA with filtered skills that does not belong to currenty selected course</strong></p>
<% if (taList == null || taList.isEmpty()) { %>
	No other TA can be added to this Course, you may need to change your filter options.
<% } else { %>
<table id="users">
	<tr>
		<td class="cell-header">Last Name</td>
		<td class="cell-header">First Name</td>
		<td class="cell-header">Email</td>
		<td class="cell-header">Skills</td>
		<td class="cell-header">Class Instructing</td>
		<td class="cell-header">Class Taking</td>
		<td class="cell-header">Modify From Course</td>
	</tr>
<% int i = 0;
for (Map<String, Object> ta : taList) {
	i++;
		String skills = "";
		if (ta.get("skills") != null) {
			for (String skill : (List<String>) ta.get("skills"))
				skills += skill + "<br /> ";
		}
		if (skills.endsWith("<br /> "))
			skills = skills.substring(0, skills.length() - 7);
%> 

	<tr class="line<%=i % 2 %>">
		<td class="cell"><%=ta.get("lastname") %></td>
		<td class="cell"><%=ta.get("firstname") %></td>
		<td class="cell"><%=ta.get("email") %></td>
		<td class="cell"><%=skills %></td>
		<td class="cell">
		<% List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) ta.get("sections");
			if (sections  != null) {
				for (WrapperObject<Section> section : sections) {
					WrapperObject<Course> course = WrapperObjectFactory.getCourse().findObjectById(section.getId().getParent());
				%>
					COMPSCI-<%=course.getProperty("coursenum") %><br />
					<%=section.getProperty("days") %>
					<%=section.getProperty("starttime") %>-<%=section.getProperty("endtime") %>
					<br /><br />
				<% }
		} %>
		</td>
		<td class="cell">
			<% List<WrapperObject<TAClass>> taClasses = (List<WrapperObject<TAClass>>) ta.get("taclasses");
			if (taClasses  != null) {
				for (WrapperObject<TAClass> taClass : taClasses) {
				%>
					<%=taClass.getProperty("classnum") %><br />
					<%=taClass.getProperty("days") %>
					<%=taClass.getProperty("starttime") %>-<%=taClass.getProperty("endtime") %>
					<br /><br />
				<% }
		} %>
		</td>
		<td class="cell">
			<% if (selectedCourseNumber != null && selectedCourseNumber != -72) { %>
			<form action="/admin/tamanager" method="post">
				<input type="hidden" name="addTAtoCourse" value="<%=selectedCourseNumber %>" />
				<input type="hidden" name="taEmail" value="<%=ta.get("email") %>" />
				<input type="hidden" name="courselist" value="<%=selectedCourseNumber %>" />
				<input type="submit" name="submit" class="submit" value="Add To" />
			</form>
			<% } %>
		</td>
	</tr>

<% } %>
	</table>
<% } %>

</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />