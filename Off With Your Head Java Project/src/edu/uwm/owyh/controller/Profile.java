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
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.library.CalendarHelper;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.CellObject;

@SuppressWarnings("serial")
public class Profile extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Find the logged in user. They're the only ones who can view their profile. */
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		Key myId = WrapperObjectFactory.generateIdFromUserName((String) self.getProperty("username"));
		self = WrapperObjectFactory.getPerson().findObjectById(myId);
		WrapperObject<Person> user = self;
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(null, self, null);
		request.setAttribute("officehours", PropertyHelper.makeOfficeHoursProperties(officeHours));
		CellObject[][] newArray = CalendarHelper.getCellObjectArray(self);
		
		if (user != null)
		{
			request.setAttribute("array", newArray);
			request.setAttribute("user", PropertyHelper.makeUserProperties(user));
			request.setAttribute("self", PropertyHelper.makeUserProperties(self));
			request.getRequestDispatcher(request.getContextPath() + "/profile.jsp").forward(request, response);		
		}
		else
		{
			/* Should never get here, but this implies user isn't logged. 
			 * Redirect to index. */
			response.sendRedirect("/");
		}
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Stuff to get User Profile Information */
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		String username = (String) self.getProperty("email");
		Key myId = WrapperObjectFactory.generateIdFromUserName(username);
		self = WrapperObjectFactory.getPerson().findObjectById(myId);
		request.setAttribute("self", PropertyHelper.makeUserProperties(self));
		
		Map<String, Object> properties;
		List<String> errors = new ArrayList<String>();
		
		/* User tries to change password */
		if (request.getParameter("changepassword") != null) {
			String userPassword = (String) self.getProperty("password");
			String originalPassword = request.getParameter("orginalpassword");
			String newPassword = request.getParameter("newpassword");
			String verifyNewPassword = request.getParameter("verifynewpassword");
			errors = new ArrayList<String>();
			if (originalPassword == null && !auth.verifyAdmin())
				errors.add("Password Change Error!");
			if (userPassword == null || verifyNewPassword == null)
				errors.add("Invalid New Passwords!");
			if (newPassword != null && !newPassword.equals(verifyNewPassword))
				errors.add("New Password Does Not Match!");
			if (userPassword != null && !userPassword.equals(originalPassword))
				errors.add("Password Does Not Match Original!");
			
			if (errors.isEmpty()) {
				properties = PropertyHelper.propertyMapBuilder("password", newPassword);
				errors = self.editObject(properties);
				if (errors.isEmpty()) {
					response.sendRedirect(request.getContextPath() + "/profile#passwordchanged");
					return;
				}
			}
			request.setAttribute("changepassworderrors", errors);
		}
		
		/* User tries to edit contact information */
		if (request.getParameter("editprofile") !=null) {
			properties = 
				PropertyHelper.propertyMapBuilder("firstname",request.getParameter("firstname")
						  ,"lastname",request.getParameter("lastname")
						  ,"email",request.getParameter("email")
		                  ,"phone",request.getParameter("phone")
		                  ,"streetaddress",request.getParameter("streetaddress")
		                  ,"city",request.getParameter("city")
		                  ,"state",request.getParameter("state")
		                  ,"zip",request.getParameter("zip")
			             );
			errors = self.editObject(properties);
			if (errors.isEmpty()) {
				response.sendRedirect(request.getContextPath() + "/profile#profilechanged");
				return;
			}
			request.setAttribute("editprofileerrors", errors);
		}
		
		request.getRequestDispatcher(request.getContextPath() + "profile.jsp").forward(request, response);			
	}
	
}