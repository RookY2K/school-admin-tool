<%@ page import="edu.uwm.owyh.model.Person" %>
<%@ page import="edu.uwm.owyh.library.Library" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Add Contact" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="AdminStyle.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />
<%
	Map<String,Object> properties = Library.propertySetBuilder("firstname",""
			                                                  ,"lastname",""
			                                                  ,"email",""
			                                                  ,"phone",""
			                                                  ,"accesslevel",""
			                                                  ,"streetaddress",""
			                                                  ,"city",""
			                                                  ,"state",""
			                                                  ,"zip",""
			                                                  );
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
			Boolean addNewUser = (Boolean)request.getAttribute("addNewUser");
			if (addNewUser != null && addNewUser) {
		%>
		<span class="good-message">User Contact Was Added!</span>
		<%	
			}else{
				List<String> errors = (List<String>)request.getAttribute("errors");
				if(errors != null){
					for(String error:errors){
		%>
		<span class="error-message"><%=error%><br /></span>
		<% 
					}
				}
			}
		%>			
		<%	
			Person user = (Person)request.getAttribute("badUserInfo");
			if(user != null) {
				Integer access = ((Person.AccessLevel)user.getProperty("accesslevel")).getVal();
				 
				properties = Library.propertySetBuilder("firstname",user.getProperty("firstname")
						                               ,"lastname",user.getProperty("lastname")
						                               ,"email",user.getProperty("email")
						                               ,"phone",user.getProperty("phone")
						                               ,"accesslevel",access
						                               ,"streetaddress",user.getProperty("streetaddress")
						                               ,"city",user.getProperty("city")
						                               ,"state",user.getProperty("state")
						                               ,"zip",user.getProperty("zip")
						                               );				
			}		
		%>
	
		<form action="/admin/addContactInfo" method="post">
			<fieldset>
				<legend> Add User Contact</legend>
				<table>
				<tr>
					<td class="cell"><label class="field" for="firstname">First Name: </label></td>
					<td class="cell"><input type = "text" name="firstname" id="firstname" value="<%=properties.get("firstname") %>" required/></td>
					<td class="cell"><label class="field" for="lastname">&nbsp;&nbsp;&nbsp;Last Name: </label></td>
					<td class="cell"><input type = "text" name="lastname" id="lastname" value="<%=properties.get("lastname") %>" required/></td>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="email">Email: </label></td>
					<td class="cell"><input type = "email" name="email" id="email" placeholder="somename@uwm.edu" value="<%=properties.get("email") %>" pattern="^\w+@uwm.edu$" required /></td>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="streetaddress">Street Address: </label></td>
					<td class="cell" colspan="3"><input type = "text" name="streetaddress" id="streetaddress" value="<%=properties.get("streetaddress")%>" /></td>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="city">City: </label></td>
					<td class="cell"><input type = "text" name="city" id="city" value="<%=properties.get("city")%>" /></td>
					<td class="cell"><label class="field" for="state">&nbsp;&nbsp;&nbsp;State:</label></td>
					<td class="cell">
					   <jsp:include page="/WEB-INF/templates/stateselect.jsp">
					    	<jsp:param name="selected" value='<%=properties.get("state") %>' />
						</jsp:include>
					</td>	
				</tr>
				<tr>
					<td class="cell"><label class="field" for="zip">Zip Code: </label></td>
					<td class="cell"><input type = "text" name="zip" id="zip" value="<%=properties.get("zip")%>" /></td>	
				</tr>
				<tr>
					<td class="cell"><label class="field" for="phone">Phone Number: </label></td>
					<td class="cell"><input type = "text" name="phone" id="phone" value="<%=properties.get("phone")%>"/></td>
				</tr>
				<tr>
					<td class="cell"><label class="field" for="accesslevel">Role: </label></td>
					<td class="cell"> 
						<select name="accesslevel" required>
						  <option value="">Please Select</option>
						  <option value="<%=taAccess%>" <%if(taAccess.equals(properties.get("accesslevel").toString())){%>selected<%}%>>TA</option>
						  <option value="<%=instructorAccess%>"<%if(instructorAccess.equals(properties.get("accesslevel").toString())){%>selected<%}%>>INSTRUCTOR</option>
						  <option value="<%=adminAccess%>"<%if(adminAccess.equals(properties.get("accesslevel").toString())){%>selected<%}%>>ADMIN</option>
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