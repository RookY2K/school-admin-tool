<%@ page import="edu.uwm.owyh.model.Auth" %>

	<div id="main">
		<div id="top">
		<span id="site-title">UWM - Computer Science Administration</span>
		</div>
	
		<div id="navbar1">
			<ul class="nav">
				<% 	Auth auth = Auth.getAuth(request);
					if(auth.verifyAdmin()) { %>
				<li class="nav"><a href="/admin" class="navbar1-link">Admin</a>
					<ul class="nav">
						<li class="nav"><a href="/admin" class="navbar1-link">Admin Console</a></li>
						<li class="nav"><a href="/admin/tamanager" class="navbar1-link">TA Manager</a></li>
					</ul>
				</li>
				<% }
					else {
				%>
				<li class="nav"><a href="/" class="navbar1-link">Home</a>
					<ul class="nav">
						<li class="nav"><a href="/" class="navbar1-link">Weekly Calendar</a></li>
					</ul>
				</li>
				<% } %>
				<li class="nav"><a href="/profile" class="navbar1-link">Profile</a>
				<ul class="nav">
					<li class="nav"><a href="/profile" class="navbar1-link">Profile Manager</a></li>
					<li class="nav"><a href="/officehours" class="navbar1-link">Office Hour Manager</a></li>
				</ul>
				<li class="nav"><a href="/userlist" class="navbar1-link">User List</a></li>
				<li class="nav"><a href="/classlist" class="navbar1-link">Class List</a></li>
				<% if (!auth.verifyAdmin() && auth.verifyUser()) { %>
				<li class="nav"><a href="/emailadmin" class="navbar1-link">Contact Us</a></li>
				<% } %>
				<li class="nav" style="float:right;"><a href="/login?logout=true" class="navbar1-link">Log Out</a></li>
			</ul>
		
		</div>
	</div>
	
		