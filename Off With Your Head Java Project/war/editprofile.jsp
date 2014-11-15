<%@ page import="edu.uwm.owyh.model.WrapperObject" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.uwm.owyh.library.Library"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="Edit User" />
    <jsp:param name="stylesheet" value="layout.css" />
    <jsp:param name="stylesheet" value="editprofile.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/layout.jsp" />

<%
	WrapperObject user = (WrapperObject) request.getAttribute("user");
	WrapperObject self = (WrapperObject) Auth.getSessionVariable(request, "user");
	if(user == null) user = self;
	Map<String, Object> properties = null;
	if(user != null){
		properties = Library.propertySetBuilder("firstname",user.getProperty("firstname")
		                                                   ,"lastname",user.getProperty("lastname")
		                                                   ,"email",user.getProperty("email")
		                                                   ,"phone",user.getProperty("phone")
		                                                   ,"streetaddress",user.getProperty("streetaddress")
		                                                   ,"city",user.getProperty("city")
		                                                   ,"state",user.getProperty("state")
		                                                   ,"zip",user.getProperty("zip")
		                                                   ,"password",user.getProperty("password")
		                                                   );
	
		Set<String> keySet = properties.keySet();	
		
		for(String key : keySet){
			String val = "";
			if(properties.get(key) == null) properties.put(key, val);
		}
	}
	
	if (user == null || properties == null || self == null) return;
	WrapperObject.AccessLevel accesslevel = (WrapperObject.AccessLevel) self.getProperty("accesslevel");
%>

<div id="content">
	<div id="local-nav-bar">
		<ul id="local-list">
		    <li><a class="nav-link" href="/profile">View My Profile</a></li>
		    <li><a class="nav-link" href="/editprofile">Edit My Profile</a></li>
			<li><a class="nav-link" href="/editprofile#changepassword">Change Password</a></li>
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
		   <fieldset>
			   <legend><%= properties.get("lastname") %>, <%= properties.get("firstname") %> </legend>
			   <table>
			   <tr>
				   <td class="cell"><label class="field" for="firstname">First Name: </label></td>
				   <td class="cell"><input type = "text" name="firstname" id="firstname" value="<%=properties.get("firstname") %>" required />
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="lastname">Last Name: </label></td>
				   <td class="cell"><input type = "text" name="lastname" id="lastname" value="<%=properties.get("lastname") %>" required />
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="emaildisplay">Email: </label></td>
				   <td class="cell"><input type = "text" name="emaildisplay" id="emaildisplay" value="<%=properties.get("email") %>" disabled />
				   <td class="cell"><input hidden="true" type = "text" name="email" id="email" value="<%=properties.get("email") %>" />
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="phone">Phone: </label></td>
				   <td class="cell"><input type = "text" name="phone" id="phone" value="<%=properties.get("phone") %>"/>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="streetaddress">Address: </label></td>
				   <td class="cell"><input type = "text" name="streetaddress" id="streetaddress" value="<%=properties.get("streetaddress") %>"/>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="city">City: </label></td>
				   <td class="cell"><input type = "text" name="city" id="city" value="<%=properties.get("city") %>"/>
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="state">State: </label></td>
				   
				   <td class="cell">
				   <jsp:include page="/WEB-INF/templates/stateselect.jsp">
				    	<jsp:param name="selected" value='<%=properties.get("state") %>' />
					</jsp:include>	
					</td>					   
			   </tr>
			   <tr>
				   <td class="cell"><label class="field" for="zip">Zip Code: </label></td>
				   <td class="cell"><input type = "text" name="zip" id="zip" value="<%=properties.get("zip") %>"/>
			   </tr>
			   <tr>
				   <td class="cell" colspan="2"><input type="submit" value="Submit"/></td>
			   </tr>
			   </table>
		   </fieldset>
	   </form>
	   
	   <br /><a id="changepassword" ></a>
	    <form action="/editprofile" method="post">
			<input type="hidden" name="changepassword" value="changepassword" />
			<input type="hidden" name="email" value="<%=properties.get("email") %>" />
		   <fieldset>
			   <legend>Change Password</legend>
			   <table>
			   
			   <% 
			   String myUsername = (String) self.getProperty("username");
			   String targetUsername = (String) user.getProperty("username");
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
				   <td class="cell" colspan="2"><input type="submit" name="changeuserpassword" value="Change Password"/></td>
			   </tr>
			   <%  if (accesslevel == WrapperObject.AccessLevel.ADMIN && !myUsername.equals(targetUsername)) { %>
		  		<tr>
				   <td class="cell" colspan="2"><input type="submit" name="changeuserpasswordandemail" value="Change & Email User Password"/></td>
			   </tr>
			   <% } %>
			   </table>
		   </fieldset>
	   </form>
	</div>
</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />
