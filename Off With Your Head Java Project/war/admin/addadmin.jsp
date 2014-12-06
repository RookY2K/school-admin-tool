<%@ page import="edu.uwm.owyh.jdowrappers.WrapperObject"%>
<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List"%>
<%! @SuppressWarnings("unchecked")%>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="App Key" />
    <jsp:param name="stylesheet" value="main.css" />
    <jsp:param name="stylesheet" value="index.css" />
</jsp:include>

<%		
	Boolean isAddAdmin = (Boolean)session.getAttribute("isAddAdmin");
	if(isAddAdmin == null){
		response.sendRedirect(request.getContextPath() + "/");
	}
%>
<form id="addAdmin" action="/admin/addadmin" method="post">
	<table id="addAdmin-table">
	<tr>
		<td id="addAdmin-title" colspan="2">UWM - Computer Science Administration</td>
	</tr>
	<tr><td><div style="margin: 10px;"></div></td></tr>
<% 
	if (request.getAttribute("addNewUser") != null) {
		boolean addNewUser = (Boolean) request.getAttribute("addNewUser");
		if (!addNewUser) {
			List<String> errors = (List<String>)request.getAttribute("errors");
			for(String error:errors){
%>
	<tr>
		<td colspan="2" class="error-message" style="padding:0px 0px 10px 20px"><%=error%></td>
	</tr>
<% 
			}
		}
	}
%>
	<tr>
		<td class="text_info">Username:</td>
		<td class="text_block"><input type ="email" name="email" id="email" pattern="^\w+@uwm.edu$" placeholder="somename@uwm.edu" required />
	</tr>
	<tr>
		<td class="text_info">Password: </td>
		<td class="text_block"><input type = "password" name="password" id="password" required />
	</tr>
	<tr>
		<td class="text_info">Re-Enter Password: </td>
		<td class="text_block"><input type = "password" name="passwordcopy" id="passwordcopy" required />
	</tr>
	<tr>
		<td class="text_info">AccessLevel:</td>
		<td class="text_block"> 
			<select name="accesslevel">
			  <option value="<% out.print(AccessLevel.ADMIN.getVal());%>">ADMIN</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="text_info"></td>
		<td class="text_block"><input type="submit" class="submit" value="Create New User" style="width:80%;"/></td>
	</tr>
	<tr>
		<td id="footer" colspan="2">
			<br />Username must be in the format "someone@uwm.edu" <br /><br />
			For help, please contact help desk at <br />
			<a href="mailto:vamaiuri@uwm.edu?subject=UWMCSA%20Help">vamaiuri@uwm.edu</a><br /><br />
		</td>
	</tr>
	</table>
</form>


<br class="clear" />
	
<jsp:include page="/WEB-INF/templates/footer.jsp" />