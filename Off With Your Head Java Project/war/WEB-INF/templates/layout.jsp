<%@ page import="edu.uwm.owyh.model.WrapperObject" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>

<%
WrapperObject user = (WrapperObject)Auth.getSessionVariable(request, "user");
%>

<div id="wrapper">
	<div id="page">
		<div id="header">
			<div id="banner">
	           <h1 id="program-title">UWM - Computer Science Administration</h1>
			</div>                    
			<div id="global-nav-bar">
				<ul id="global-list">
					<li class="global-link-item"><a class="global-link" href="/">Home</a>
						<ul class="global-dropdown-list">
						</ul>
					</li>
					
                    <li class="global-link-item"><a class="global-link" href="/profile">Profile</a>
						<ul class="global-dropdown-list">
							<li class="global-link-item"><a class="global-link" href="/editprofile">Edit My Profile</a></li>
							<li class="global-link-item"><a class="global-link" href="/editprofile#changepassword">Change Password</a></li>
						</ul>
					</li>
					<li class="global-link-item"><a class="global-link" href="/userlist">Users</a></li>
					
					<% 
					if (user != null) {
						WrapperObject.AccessLevel accesslevel = (WrapperObject.AccessLevel) user.getProperty("accesslevel");
					if (accesslevel == WrapperObject.AccessLevel.ADMIN) { %>
                     
                     <li class="global-link-item"><a class="global-link" href="/">Admin</a>
						<ul class="global-dropdown-list">
							<li class="global-link-item"><a class="global-link" href="/admin/addContactInfo">Add User Contact</a></li>
							<!--<li class="global-link-item"><a class="global-link" href="/admin/addClient">Edit User Login</a></li>  -->	
					    </ul>
					</li>
					<% } } %>
				</ul>
				
				
                 <ul id="logout">
                     <li class="global-link-item"><a class="global-link" href="/login?login=logout">Log Out</a></li>
                 </ul>
             </div>
	   		</div>