<%@ page import="edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%! @SuppressWarnings("unchecked") %>

<jsp:include page="/WEB-INF/templates/header.jsp">
    <jsp:param name="title" value="User List" />
    <jsp:param name="stylesheet" value="main.css" />
</jsp:include>

<jsp:include page="/WEB-INF/templates/navagation.jsp" />
<jsp:include page="/WEB-INF/templates/homenavagation.jsp" />

<%	Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
	List<Map<String, Object>> officeHours = (List<Map<String, Object>>) request.getAttribute("officehours");
	if (user == null || officeHours == null) { out.print("No Correct Attribute Was Passed Into JSP!"); return; }

	Boolean isAdmin = (Boolean)request.getAttribute("isAdmin");
		
	String modifyUser = (String) request.getAttribute("adminedituser");
 %>

<div id="body">
	<%	List<String> errors = (List<String>)request.getAttribute("errors");
		if(errors != null){
			for(String error: errors){
	%>
	<span class="error-message"><%=error%></span><br />
	<%			
			}
		}
	%>
	<%	List<String> messages = (List<String>)request.getAttribute("messages");
		if(messages != null){
			for(String message: messages){
	%>
	<span class="good-message"><%=message %></span><br />
	<%			
			}
		}
	%>
		  
	<% if (modifyUser != null && isAdmin) { %>	  
	<span class="warning-message">You are currently editing <%= modifyUser %>'s Office Hours, NOT your own.</span><br />
	<% } %>
		  
   	<p><strong>Add Office Hours</strong></p>
   	<div class="officehour-tab">
    <form action="/officehours" method="post">
		<input type="hidden" name="addofficehour" value="addofficehour" />
		<input type="hidden" name="email" value="<%=user.get("email") %>" />
	 	<table>	   					   
	   		<tr>
	   			<td>Days:</td>
		   		<td colspan="2">
		   			<input type="checkbox" name="M" value="M"> Monday 
		   			<input type="checkbox" name="T" value="T"> Tuesday 
		   			<input type="checkbox" name="W" value="W"> Wednesday 
		   			<input type="checkbox" name="R" value="R"> Thursday 
		   			<input type="checkbox" name="F" value="F"> Friday
	   			</td>
	   		</tr>
	   		<tr>
	   		<tr>
	   			<td>Start Time:</td>
	   			<td colspan="2">
	   			 <select name="start_hour">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select>
					<select name="start_minute">
						<option value="00">00</option>
						<option value="05">05</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
						<option value="25">25</option>
						<option value="30">30</option>
						<option value="35">35</option>
						<option value="40">40</option>
						<option value="45">45</option>
						<option value="50">50</option>
						<option value="55">55</option>
					</select>
					<select name="start_ampm">
						<option value="AM">AM</option>
						<option value="PM">PM</option>
					</select>
	   			</td>
	   		</tr>
	   		<tr>
	   			<td>End Time:</td>
	   			<td>
		   			 <select name="end_hour">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select>
					<select name="end_minute">
						<option value="00">00</option>
						<option value="05">05</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
						<option value="25">25</option>
						<option value="30">30</option>
						<option value="35">35</option>
						<option value="40">40</option>
						<option value="45">45</option>
						<option value="50">50</option>
						<option value="55">55</option>
					</select>
					<select name="end_ampm">
						<option value="AM">AM</option>
						<option value="PM">PM</option>
					</select>
	   			</td>
				<td class="submitinfo">
					<input type="submit" class="submit" name="addofficehoursubmit" value="Add Office Hours"/>
				</td>
			</tr>
		</table>
	</form>
	</div>
	
	
	<br /><p><strong>Edit Office Hours</strong></p>
	
	<% 
	for (int k = 0; k < officeHours.size(); k++) {
			Map<String, Object> hours = officeHours.get(k);
		 	String startTime = (String) hours.get("starttime");
		 	String startHour = startTime.split(":")[0];
		 	String startMinute =  startTime.split(":")[1].substring(0,2);
		 	String startAMPM = startTime.split(":")[1].substring(2,4);
		 	String endTime = (String) hours.get("endtime");
		 	String endHour = endTime.split(":")[0];
		 	String endMinute =  endTime.split(":")[1].substring(0,2);
		 	String endAMPM = endTime.split(":")[1].substring(2,4);
		 	String days = (String) hours.get("days");
	%>
	<div class="officehour-tab">
	<table>	   					   				
		<tr>
			<td colspan="3">
			 	<% out.print("Office Hours: (" + days +") " + startTime + " - " + endTime); %>
			</td>
		</tr>
	 	<tr>
	 		<td>
	 			<form action="/officehours" method="post">
				<input type="hidden" name="editofficehour" value="editofficehour" />
				<input type="hidden" name="email" value="<%=user.get("email") %>" />
				<input type="hidden" name="officehourid" value="<%=k %>" />
				Days:
			</td>
			<td colspan="2">
				 <input type="checkbox" name="M" value="M" <% if (days.indexOf("M") >= 0) out.print("checked");  %> /> Monday 
	  			<input type="checkbox" name="T" value="T" <% if (days.indexOf("T") >= 0) out.print("checked");  %> /> Tuesday 
	  			<input type="checkbox" name="W" value="W" <% if (days.indexOf("W") >= 0) out.print("checked");  %> /> Wednesday 
	  			<input type="checkbox" name="R" value="R" <% if (days.indexOf("R") >= 0) out.print("checked");  %> /> Thursday 
	  			<input type="checkbox" name="F" value="F" <% if (days.indexOf("F") >= 0) out.print("checked");  %> /> Friday
 			</td>
	 	</tr>
		<tr>
	 		<td>Start Time: </td>
	 		<td>	
	 		 	<select name="start_hour">
				<% for (int i = 1; i < 13; i++) { %>	
					<option value="<%=i %>" <% if (startHour.equals(String.valueOf(i))) out.print("selected"); %> ><%=i %></option>
				<% } %>
				</select>
				<select name="start_minute">
				<% for (int i = 0; i < 12; i++) {
					int j = i * 5; 
					String extraZero = "";
					if (j < 10) extraZero = "0";
				%>	
					<option value="<%=extraZero %><%=j  %>" <% if (startMinute.equals(String.valueOf(j))) out.print("selected"); %> ><%=extraZero %><%=j %></option>
				<% } %>
				</select>
				<select name="start_ampm">
					<option value="AM" <% if (endAMPM.equals("AM")) out.print("selected"); %> >AM</option>
					<option value="PM" <% if (endAMPM.equals("PM")) out.print("selected"); %> >PM</option>
				</select>
	  	 	</td>
	  	 	<td class="submitinfo"><input type="submit" class="submit" name="editofficehoursubmit" value="Edit Office Hours"/>
	  	 	</td>
	   	</tr>
	   	<tr>
	 		<td>End Time: </td>
	 		 <td>
	 		 	<select name="end_hour"> 
				<% for (int i = 1; i < 13; i++) { %>	
					
					<option value="<%=i %>" <% if (endHour.equals(String.valueOf(i))) out.print("selected"); %> /><%=i %></option>
				<% } %>
				</select>
				<select name="end_minute">
				<% for (int i = 0; i < 12; i++) {
					int j = i * 5; 
					String extraZero = "";
					if (j < 10) extraZero = "0";
					
				%>	
					<option value="<%=extraZero %><%=j  %>"  
					<% if (endMinute.equals(String.valueOf(j))) out.print("selected"); %> >
					<%=extraZero %><%=j %></option>
				<% } %>
				</select>
				<select name="end_ampm">
					<option value="AM" <% if (startAMPM.equals("AM")) out.print("selected"); %>>AM</option>
					<option value="PM" <% if (startAMPM.equals("PM")) out.print("selected"); %>>PM</option>
				</select></form>	
	  	 	</td>
			<td class="submitinfo">
				<form action="/officehours" method="post">
					<input type="hidden" name="deleteofficehour" value="deleteofficehour" />
					<input type="hidden" name="email" value="<%=user.get("email") %>" />
					<input type="hidden" name="officehourid" value="<%=k %>" />
					<input type="submit" class="submit" name="deleteofficehoursubmit" value="Delete Office Hours"/>
				</form>
			 </td>
		</tr>
	</table>
	</div><br /><br />
<% } %>


</div>

<jsp:include page="/WEB-INF/templates/footer.jsp" />
 