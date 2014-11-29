<%@ page import="edu.uwm.owyh.library.LocalDevLibrary" %>
<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
<%@ page import="edu.uwm.owyh.model.Auth" %>
<%! @SuppressWarnings("unchecked") %>

<%
WrapperObject<Person> user = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
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
							<li class="global-link-item"><a class="global-link" href="/editofficehours">Add/Edit Office Hours</a></li>
						</ul>
					</li>
					<li class="global-link-item"><a class="global-link" href="/userlist">Users</a></li>
					
					<li class="global-link-item"><a class="global-link" href="/classlist">Class List</a></li>
					<% 
					if (user != null) {
						AccessLevel accesslevel = (AccessLevel) user.getProperty("accesslevel");
					if (accesslevel == AccessLevel.ADMIN) { %>
                     
                     <li class="global-link-item"><a class="global-link" href="/">Admin</a>
						<ul class="global-dropdown-list">
	   						<li class="global-link-item"><a class="global-link" href="/admin/addContactInfo">Add New User</a></li>
							<li class="global-link-item"><a class="global-link" href="/admin/addContactInfo#addcontactinfo">Add Contact Info</a></li>
							<li class="global-link-item"><a class="global-link" href="">Get Course List</a></li>
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