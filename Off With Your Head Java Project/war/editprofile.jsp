<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdo.OfficeHours" %>
<%@ page import="edu.uwm.owyh.factories.WrapperObjectFactory" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Edit User" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="editprofile.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<%
	Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
	Map<String, Object> self = (Map<String, Object>) request.getAttribute("self");

	if (user == null || self == null) return;
	
	WrapperObject.AccessLevel accesslevel = (WrapperObject.AccessLevel) self.get("accesslevel");
%>

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
		    <li><a class="nav-link" href="/profile">View My Profile</a></li>
		    <li><a class="nav-link" href="/editprofile">Edit My Profile</a></li>
			<li><a class="nav-link" href="/editprofile#changepassword">Change Password</a></li>
			<li><a class="nav-link" href="/editofficehours">Add Office Hour</a></li>
			<li><a class="nav-link" href="/editofficehours#changeofficehour">Edit Office Hours</a></li>
			<!--
			<li><a class="nav-link" href="officehour.html">Office Hours</a></li>
			<li><a class="nav-link" href="taclasses.html">Class Schedule</a></li>
			-->
		</ul>
	</div>

	<div id="body">
		<%
			List<String> errors = (List<String>)request.getAttribute("errors");
			if(errors != null){
				for(String error: errors){
		%>
		<span style="color:red;"><%=error%></span>
		<%			
				}
			}
		%>
       <form action="/editprofile" method="post">
    	   <input type="hidden" name="editprofile" value="editprofile" />
    	    <input hidden="true" type = "text" name="email" id="email" value="<%=user.get("email") %>" />
		   <fieldset>
			   <legend><%= user.get("lastname") %>, <%= user.get("firstname") %> </legend>
			   <table>
			   <tr>
				   <td class="cell"><label class="field" for="firstname">First Name: </label></td>
				   <td class="cell"><input type = "text" name="firstname" id="firstname" value="<%=user.get("firstname") %>" required /></td>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="lastname">Last Name: </label></td>
				   <td class="cell"><input type = "text" name="lastname" id="lastname" value="<%=user.get("lastname") %>" required /></td>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="emaildisplay">Email: </label></td>
				   <td class="cell"><input type = "text" name="emaildisplay" id="emaildisplay" value="<%=user.get("email") %>" disabled /></td>
				  
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="phone">Phone: </label></td>
				   <td class="cell"><input type = "text" name="phone" id="phone" value="<%=user.get("phone") %>"/></td>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="streetaddress">Address: </label></td>
				   <td class="cell"><input type = "text" name="streetaddress" id="streetaddress" value="<%=user.get("streetaddress") %>"/></td>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="city">City: </label></td>
				   <td class="cell"><input type = "text" name="city" id="city" value="<%=user.get("city") %>"/></td>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="state">State: </label></td>
				   
				   <td class="cell">
				   <jsp:include page="/WEB-INF/templates/stateselect.jsp">
				    	<jsp:param name="selected" value='<%=user.get("state") %>' />
					</jsp:include>	
					</td>					   
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="zip">Zip Code: </label></td>
				   <td class="cell"><input type = "text" name="zip" id="zip" value="<%=user.get("zip") %>"/></td>
			   </tr>
			   <tr>
				   <td class="cell" colspan="2"><input type="submit" name="editprofilesubmit" value="Edit Profile"/></td>
			   </tr>
			   </table>
		   </fieldset>
	   </form>
	   
	   <br /><a id="changepassword" ></a>
	    <form action="/editprofile" method="post">
			<input type="hidden" name="changepassword" value="changepassword" />
			<input type="hidden" name="email" value="<%=user.get("email") %>" />
		   <fieldset>
			   <legend>Change Password</legend>
			   <table>
			   
			   <% 
			   String myUsername = (String) self.get("email");
			   String targetUsername = (String) user.get("email");
			   if (accesslevel != WrapperObject.AccessLevel.ADMIN || myUsername.equals(targetUsername)) { 
			   %>
			   		
			   <tr>
				   <td class="cell"><label class="field" for="orginalpassword">Original Password: </label></td>
				   <td class="cell"><input type = "password" name="orginalpassword" id="orginalpassword" value="" required />
			   </tr>
			   
			   <% } %>
			   
			    <tr>
				   <td class="cell"><label class="field" for="newpassword">New Password: </label></td>
				   <td class="cell"><input type = "password" name="newpassword" id="newpassword" value="" required />
			   </tr>
			    <tr>
				   <td class="cell"><label class="field" for="firstname">Verify New Password: </label></td>
				   <td class="cell"><input type = "password" name="verifynewpassword" id="verifynewpassword" value="" required />
			   </tr>
			   <tr>
				   <td class="cell" colspan="2"><input type="submit" name="changeuserpasswordsubmit" value="Change Password"/></td>
			   </tr>
			   </table>
		   </fieldset>
	   </form>

	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />
