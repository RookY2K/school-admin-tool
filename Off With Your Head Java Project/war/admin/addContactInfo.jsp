<%@ page import="edu.uwm.owyh.model.Person" %>
<%@ page import="java.util.List" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Add Contact" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />
<%
	String name = "";
	String email = "";
	String address = "";
	String phonenumber = "";
	String accessLevel = "";
	String taAccess = Integer.toString(Person.AccessLevel.TA.getVal());
	String instructorAccess = Integer.toString(Person.AccessLevel.INSTRUCTOR.getVal());
	String adminAccess = Integer.toString(Person.AccessLevel.ADMIN.getVal());
%>
<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <!-- <li><a class="nav-link" href="/admin/addClient">Add User</a></li> -->
	      <li><a class="nav-link" href="/admin/addContactInfo">Add User Contact</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	
		<% 
		if (request.getAttribute("addNewUser") != null) {
			boolean addNewUser = (Boolean) request.getAttribute("addNewUser");
			if (addNewUser) { 
		%>
		<span class="good-message">User Contact Was Added!</span>
		<%	
			}else{
				List<String> errors = (List<String>)request.getAttribute("errors");
				for(String error:errors){
		%>
		<span class="error-message"><%=error%><br /></span>
		<% 
				}
			}
		}
		%>
			
		<%			
			if(request.getAttribute("badUserInfo") != null) {
				Person user = (Person) request.getAttribute("badUserInfo");
				name = user.getProperty("name").toString();
				email = user.getProperty("email").toString();
				address = user.getProperty("address").toString();
				phonenumber = user.getProperty("phone").toString();
				int access = ((Person.AccessLevel)user.getProperty("accesslevel")).getVal();
				accessLevel = Integer.toString(access); 
			}
		
		%>
	
		<form action="/admin/addContactInfo" method="post">
			<fieldset>
				<legend> Add User Contact</legend>
				<table>
				<tr>
					<td class="cell"><label class="field" for="name">Name: </label></td>
					<td class="cell"><input type = "text" name="name" id="name" value="<%=name %>" required/>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="email">Email: </label></td>
					<td class="cell"><input type = "text" name="email" id="email" value="<%=email %>" required />
				</tr>
				<tr>
					<td class="cell"><label class="field" for="address">Address: </label></td>
					<td class="cell"><input type = "text" name="address" id="address" value="<%=address%>" />
				</tr>
				<tr>
					<td class="cell"><label class="field" for="phone">Phone number: </label></td>
					<td class="cell"><input type = "text" name="phone" id="phone" value="<%=phonenumber%>"/>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="accesslevel">Role: </label></td>
					<td class="cell"> 
						<select name="accesslevel" required>
						  <option value="">Please Select</option>
						  <option value="<%=taAccess%>" <%if(taAccess.equals(accessLevel)){%>selected<%}%>>TA</option>
						  <option value="<%=instructorAccess%>"<%if(instructorAccess.equals(accessLevel)){%>selected<%}%>>INSTRUCTOR</option>
						  <option value="<%=adminAccess%>"<%if(adminAccess.equals(accessLevel)){%>selected<%}%>>ADMIN</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="cell" colspan="2"><input type="submit" value="Submit Contact Info" /></td>
				</tr>
				</table>
			</fieldset>
		</form>
	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />