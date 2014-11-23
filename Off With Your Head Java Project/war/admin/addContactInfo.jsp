<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
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
	
	String taAccess = Integer.toString(WrapperObject.AccessLevel.TA.getVal());
	String instructorAccess = Integer.toString(WrapperObject.AccessLevel.INSTRUCTOR.getVal());
	String adminAccess = Integer.toString(WrapperObject.AccessLevel.ADMIN.getVal());

%>
<div id="content">
 	<div id="local-nav-bar">
		<ul id="local-list">
	      <li><a class="nav-link" href="/admin/addContactInfo">Add New User</a></li>
	      <li><a class="nav-link" href="/admin/addContactInfo#addcontactinfo">Add Contact Info</a></li>
		</ul>
	</div>
	  	
	<div id="body">
	
		<% 
			List<String> errors = (List<String>)request.getAttribute("errors");
				if(errors != null){
					for(String error:errors){
		%>
		<span class="error-message"><%=error%><br /></span>
		<% 
					}
				}
		%>			
		<%	
			if(request.getAttribute("properties") != null) {				
				properties = (Map<String,Object>)request.getAttribute("properties");
			}
		
			String accessLevel = "";
			  if (properties.get("accesslevel") != null && !(properties.get("accesslevel") instanceof String))
				  	accessLevel = Integer.toString(((WrapperObject.AccessLevel) properties.get("accesslevel")).getVal());
		%>
		
		<form action="/admin/addClient" method="post">
			<input type="hidden" name="addnewuser" value="addnewuser" />
			<fieldset>
				<legend> Add New User Account</legend>
				<table>
					<tr>
						<td class="cell"><label class="field" for="email">Email: </label></td>
						<td class="cell"><input type = "email" name="email" id="email" placeholder="somename@uwm.edu" value="<%=properties.get("email") %>" pattern="^\w+@uwm.edu$" required /></td>
					</tr>
					<tr>
						<td class="cell"><label class="field" for="email">Password: </label></td>
						<td class="cell"><input type = "password" name="password" id="password" required /></td>
					</tr>
					<tr>
						<td class="cell"><label class="field" for="email">Verify Password: </label></td>
						<td class="cell"><input type = "password" name="verifypassword" id="password" required /></td>
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
					<td class="cell" colspan="2"><input type="submit" value="Add New User" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
		<br />
		
		<form action="/admin/addContactInfo" method="post">
		<a id="addcontactinfo"></a>
		<fieldset>
			<legend> Add Contact Information</legend>
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
					  <option value="<%=taAccess%>" <%if(taAccess.equals(accessLevel)){%>selected<%}%>>TA</option>
					  <option value="<%=instructorAccess%>"<%if(instructorAccess.equals(accessLevel)){%>selected<%}%>>INSTRUCTOR</option>
					  <option value="<%=adminAccess%>"<%if(adminAccess.equals(accessLevel)){%>selected<%}%>>ADMIN</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="cell" colspan="2"><input type="submit" value="Add User Contact Info" /></td>
			</tr>
			</table>
		</fieldset>
	</form>
	
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />