package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class OfficeHoursManager extends HttpServlet {
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(! auth.verifyUser(response)) return;
	
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		String username = "";
		if (request.getParameter("edituserofficehoursfromadmin") != null || request.getParameter("email") != null)
			username = (String) request.getParameter("email");
		else 
			username = (String) self.getProperty("username");
		
		Key myId = Library.generateIdFromUserName(username);
		WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(myId);
		request.setAttribute("user", Library.makeUserProperties(user));
		request.setAttribute("isAdmin", auth.verifyAdmin());
		
		if (!self.getId().equals(user.getId()))
			request.setAttribute("adminedituser", request.getParameter("email"));
				
		List<WrapperObject<OfficeHours>> officeHoursList = WrapperObjectFactory.getOfficeHours().findObject(null, user);
		
		request.setAttribute("officehours", Library.makeWrapperProperties(officeHoursList));
		request.getRequestDispatcher(request.getContextPath() + "officehours.jsp").forward(request, response);	
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, NumberFormatException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;		
		
		String email = request.getParameter("email");
		if (email == null || request.getParameter("edituserofficehoursfromadmin") != null) {
			doGet(request, response);
			return;
		}
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		Key myId = Library.generateIdFromUserName(email);
		WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(myId);
		request.setAttribute("user", Library.makeUserProperties(user));
		request.setAttribute("isAdmin", auth.verifyAdmin());
		
		/* Prevent non-Admin from editing other people, Redirect to User own profile */
		if (user == null || (!self.getId().equals(user.getId()) && !auth.verifyAdmin())) {
			response.sendRedirect("/profile");		
			return;
		}

		/* Admin User change office hours */		
		String days = "";
		String starttime = "";
		String endtime = "";
		if (request.getParameter("M") != null) days += "M";
		if (request.getParameter("T") != null) days += "T";
		if (request.getParameter("W") != null) days += "W";
		if (request.getParameter("R") != null) days += "R";
		if (request.getParameter("F") != null) days += "F";	
		if (request.getParameter("start_hour") != null) starttime += request.getParameter("start_hour") + ":";
		if (request.getParameter("start_minute") != null)  starttime += request.getParameter("start_minute");
		if (request.getParameter("start_ampm") != null) starttime += request.getParameter("start_ampm");
		if (request.getParameter("end_hour") != null) endtime += request.getParameter("end_hour") + ":";
		if (request.getParameter("end_minute") != null)  endtime += request.getParameter("end_minute");
		if (request.getParameter("end_ampm") != null) endtime += request.getParameter("end_ampm");
		
		Map<String, Object> officehours = 
				Library.propertyMapBuilder("days",days
	    								  ,"starttime",starttime
	    				                  ,"endtime",endtime
											);
		
		List<String> errors = new ArrayList<String>();
		List<String> messages = new ArrayList<String>();


		try {
			if (request.getParameter("addofficehour") != null){
				errors = WrapperObjectFactory.getOfficeHours().addObject(email, officehours);
 				if (errors.isEmpty())
 					messages.add("Office Hours was successfully added.");
			}
			else if (request.getParameter("officehourid") != null) {
				int officeHourID = Integer.valueOf(request.getParameter("officehourid"));
				
				List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObject(null, user);
				
		 		if (officeHourID < 0 || officeHourID > officeHours.size()) {
		 			errors.add("Edit ID error!");
		 		}
		 		else {
		 			if (request.getParameter("deleteofficehour") != null) {
		 				if (! officeHours.get(officeHourID).removeObject(email))
		 					errors.add("Could not Delete Hours");
		 				else 
		 					messages.add("Office Hours was successfully deleted.");
		 			}
		 			else {
		 				errors = officeHours.get(officeHourID).editObject(email, officehours);
		 				if (errors.isEmpty())
		 					messages.add("Office Hours was successfully edited.");
		 			}
		 		}
			}
		}
		catch (IllegalArgumentException e) {
			errors.add("Bad pattern for days!");
		}
		
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			doGet(request, response);
		}
	    else {
	    	request.setAttribute("messages", messages);
	    	doGet(request, response);
	    }
	}
}