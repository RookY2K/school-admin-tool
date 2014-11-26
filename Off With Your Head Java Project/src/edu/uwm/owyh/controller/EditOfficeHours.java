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
public class EditOfficeHours extends HttpServlet {
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(! auth.verifyUser(response)) return;
		
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		Key myId = Library.generateIdFromUserName((String) self.getProperty("username"));
		self = WrapperObjectFactory.getPerson().findObjectById(myId);
		//request.setAttribute("self", Library.makeUserProperties(self));
		
		/* Admin edit another User's Profile 
		String username = request.getParameter("username");
		WrapperObject<Person> user = null;
		if (username != null && auth.verifyAdmin()) {
			Key id = Library.generateIdFromUserName(username);
			user = WrapperObjectFactory.getPerson().findObjectById(id);
		}*/
		
		/* User Edit there Own Profile 
		if (user == null)
			user = self;*/
		
		request.setAttribute("user", Library.makeUserProperties(self));
		
		/* Rewrap OfficeHour JDO back into its Wrapper */
/*		List<edu.uwm.owyh.jdo.OfficeHours> officeHours = (List<edu.uwm.owyh.jdo.OfficeHours>) self.getProperty("officehours");
	 	List<WrapperObject<edu.uwm.owyh.jdo.OfficeHours>> officeHoursWrapped = new ArrayList<WrapperObject<edu.uwm.owyh.jdo.OfficeHours>>();
	 		for (edu.uwm.owyh.jdo.OfficeHours hours : officeHours)
	 			officeHoursWrapped.add(WrapperObjectFactory.getOfficeHours().findObjectById(hours.getId()));*/
		
		List<WrapperObject<OfficeHours>> officeHoursList = WrapperObjectFactory.getOfficeHours().findObject(null, self);
		
		request.setAttribute("officehourswrapped", officeHoursList );
		request.getRequestDispatcher(request.getContextPath() + "editofficehours.jsp").forward(request, response);	
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, NumberFormatException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;		
		
		/* Admin attempt to start editing profile from UserList */
		//String username = request.getParameter("username");
		String email = request.getParameter("email");
		if (email == null) {
			doGet(request, response);
			return;
		}
		
		//Key id = Library.generateIdFromUserName(email);
		//WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		Key myId = Library.generateIdFromUserName((String) self.getProperty("username"));
		self = WrapperObjectFactory.getPerson().findObjectById(myId);
		request.setAttribute("self", Library.makeUserProperties(self));
		
		/* Prevent non-Admin from editing other people, Redirect to User own profile 
		if (user == null || (!self.getId().equals(user.getId()) && !auth.verifyAdmin())) {
			response.sendRedirect("/profile");		
			return;
		}*/

		/* Admin User change office hours */
		String addofficehour = request.getParameter("addofficehour");
		
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


		try {
			if (addofficehour == null && request.getParameter("officehourid") != null) {
				int officeHourID = Integer.valueOf(request.getParameter("officehourid"));
				
				/* Rewrap OfficeHour JDO back into its Wrapper */
//				List<edu.uwm.owyh.jdo.OfficeHours> officeHours = (List<edu.uwm.owyh.jdo.OfficeHours>) self.getProperty("officehours");
//			 	List<WrapperObject<edu.uwm.owyh.jdo.OfficeHours>> officeHoursWrapped = new ArrayList<WrapperObject<edu.uwm.owyh.jdo.OfficeHours>>();
				
//		 		for (edu.uwm.owyh.jdo.OfficeHours hours : officeHours)
//		 			officeHoursWrapped.add(WrapperObjectFactory.getOfficeHours().findObjectById(hours.getId()));
				List<WrapperObject<OfficeHours>> officeHoursWrapped = WrapperObjectFactory.getOfficeHours().findObject(null, self);
				
		 		if (officeHourID < 0 || officeHourID > officeHoursWrapped.size()) {
		 			errors.add("Edit ID error!");
		 		}
		 		else {
		 			if (request.getParameter("deleteofficehour") != null) {
		 				if (! officeHoursWrapped.get(officeHourID).removeObject((String)self.getProperty("username")))
		 						errors.add("Could not Delete Hours");
		 			}
		 			else
		 				errors = officeHoursWrapped.get(officeHourID).editObject(request.getParameter("email"), officehours);
		 		}
				
			}
			else {
				errors = WrapperObjectFactory.getOfficeHours().addObject(request.getParameter("email"), officehours);	
			}
		}
		catch (IllegalArgumentException e) {
			errors.add("Bad pattern for days!");
		}
		
		if (!errors.isEmpty()) {
			request.setAttribute("user", Library.makeUserProperties(self));
			request.setAttribute("errors", errors);
			doGet(request, response);
		}
	    else {
	    	/* Admin edit another Office Hour, go to userList */
	    	response.sendRedirect("/editofficehours");	
	    }
	}
}