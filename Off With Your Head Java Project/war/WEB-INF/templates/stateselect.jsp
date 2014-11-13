<%@ page import="edu.uwm.owyh.library.Library" %>

<%
	String[] states = Library.getStates();
	String selected = request.getParameter("selected");
%>

<select name="state" id="state">
	<option value=""></option>
	
	<% for (String state : states) {
		if 	(selected != null && selected.equals(state)) {
	%>
		<option value="<%=state %>" selected><%=state %></option>
		<% } else { %>
		<option value="<%=state %>" ><%=state %></option>
		<% } %>
	<% } %>

</select>