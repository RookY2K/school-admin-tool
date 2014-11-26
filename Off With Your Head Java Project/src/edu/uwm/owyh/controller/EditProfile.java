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
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class EditProfile extends HttpServlet {
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(! auth.verifyUser(response)) return;
		
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		Key myId = Library.generateIdFromUserName((String) self.getProperty("username"));
		self = WrapperObjectFactory.getPerson().findObjectById(myId);
		
		request.setAttribute("self", Library.makeUserProperties(self));
		
		/* Admin edit another User's Profile */
		String username = request.getParameter("username");
		WrapperObject<Person> user = null;
		if (username != null && auth.verifyAdmin()) {
			Key id = Library.generateIdFromUserName(username);
			user = WrapperObjectFactory.getPerson().findObjectById(id);
		}
		
		/* User Edit there Own Profile */
		if (user == null)
			user = self;
		
		request.setAttribute("user", Library.makeUserProperties(user));
		request.getRequestDispatcher(request.getContextPath() + "editprofile.jsp").forward(request, response);	
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;		
		
		/* Admin attempt to start editing profile from UserList */
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		if (username != null || email == null) {
			doGet(request, response);
			return;
		}
		
		Key id = Library.generateIdFromUserName(email);
		WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		Key myId = Library.generateIdFromUserName((String) self.getProperty("username"));
		self = WrapperObjectFactory.getPerson().findObjectById(myId);
		request.setAttribute("self", Library.makeUserProperties(self));
		
		/* Prevent non-Admin from editing other people, Redirect to User own profile */
		if (user == null || (!self.getId().equals(user.getId()) && !auth.verifyAdmin())) {
			response.sendRedirect("/profile");		
			return;
		}
		
		/* Admin and User change password */
		Map<String, Object> properties;
		List<String> errors = new ArrayList<String>();
		String changepassword = request.getParameter("changepassword");

		if (changepassword != null) {
			String userPassword = (String) user.getProperty("password");
			String originalPassword = request.getParameter("orginalpassword");
			String newPassword = request.getParameter("newpassword");
			String verifyNewPassword = request.getParameter("verifynewpassword");
			errors = new ArrayList<String>();
			if (originalPassword == null && !auth.verifyAdmin())
				errors.add("Non-Admin Password Change Error!");
			if (!newPassword.equals(verifyNewPassword ))
				errors.add("New Password Does Not Match!");
			if (userPassword != null && !userPassword.equals(originalPassword) && self.getId().equals(user.getId()))
				errors.add("Password Does Not Match Original!");
			
			if (errors.isEmpty()) {
				properties = Library.propertyMapBuilder("password", newPassword);
				errors = user.editObject(request.getParameter("email"), properties);
			}
		}

		/* User change Profile */
		String editprofile = request.getParameter("editprofile");
		
		if (editprofile != null) {
			properties = 
				Library.propertyMapBuilder("firstname",request.getParameter("firstname")
	    								  ,"lastname",request.getParameter("lastname")
	    								  ,"email",request.getParameter("email")
	    				                  ,"phone",request.getParameter("phone")
	    				                  ,"streetaddress",request.getParameter("streetaddress")
	    				                  ,"city",request.getParameter("city")
	    				                  ,"state",request.getParameter("state")
	    				                  ,"zip",request.getParameter("zip")
	    				                  );
			errors = user.editObject(request.getParameter("email"), properties);
		}
		
	    

		if (!errors.isEmpty()) {
			request.setAttribute("user", Library.makeUserProperties(user));
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(request.getContextPath() + "editprofile.jsp").forward(request,response);	
		}
	    else if (self.getId().equals(user.getId())) {
	    	/* User edit there own profile, go back to view there profile */
			response.sendRedirect("/profile");	
			//Auth.setSessionVariable(request, "user", user);
		}
	    else {
	    	/* Admin edit another User Profile, go to userList */
	    	response.sendRedirect("/userlist");	
	    }
	}
}