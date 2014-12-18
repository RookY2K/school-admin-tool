<%@ page import="edu.uwm.owyh.model.Auth" %>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="edu.uwm.owyh.interfaces.WrapperObject"%>
<%@ page import="edu.uwm.owyh.jdo.Person" %>
<%! @SuppressWarnings("unchecked") %>

<% WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user"); %>

	<div id="navbar2">
		<a href="/profile" class="navbar2-links">Profile Manager</a>
		<a href="/officehours" class="navbar2-links">Office Hour Manager</a>
		<% if (self != null && self.getProperty("accesslevel") == AccessLevel.TA) { %>
		<a href="/taclass" class="navbar2-links">TA Class Schedule</a>
		<% } %>
	</div>