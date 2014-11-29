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
public class UserList extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Get user info and all userlist */
		WrapperObject<Person> self = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		List<WrapperObject<Person>> clients = self.getAllObjects();
		
		List<Map<String, Object>> clientList = new ArrayList<Map<String, Object>>();
		
		for (WrapperObject<Person> client : clients)
			clientList.add(Library.makeUserProperties(client));
		
		/* Admin edit User Profile */
		String username = request.getParameter("modifyuser");
		if (request.getAttribute("modifyuser") != null)
			username = (String) request.getAttribute("modifyuser");
		if (username != null) {
			Key id = Library.generateIdFromUserName(username);
			WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
			if (user != null) {
				List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObject(null, user);
				request.setAttribute("officehours", Library.makeWrapperProperties(officeHours));
				request.setAttribute("modifyuser", Library.makeUserProperties(user));		
			}
		}		
		
		request.setAttribute("self", Library.makeUserProperties(self));
		request.setAttribute("users", clientList);
		request.getRequestDispatcher(request.getContextPath() + "userlist.jsp").forward(request, response);	
	}
	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* This allow Admin to edit, delete user Redirected from another page */
		if (request.getParameter("modifyuser") != null) {
			doGet(request, response);
			return;
		}

		List<String> errors = new ArrayList<String>();
		WrapperObject<Person> user = null;
		
		/* Make sure a correct username was pass through */
		String username = (String) request.getParameter("email");
		
		if (username == null)
			errors.add("No username was posted!");
		else {
			Key id = Library.generateIdFromUserName(username);
			user = WrapperObjectFactory.getPerson().findObjectById(id);
			if (user == null)
				errors.add("Username was not found!");
		}
		
		if (!errors.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/userlist");	
			return;
		}

		/* Admin delete a User */
		if (request.getParameter("deleteuserconfirm") != null) {
			if (WrapperObjectFactory.getPerson().removeObject(username)) {
				response.sendRedirect(request.getContextPath() + "/userlist#deleteuser");	
				return;
			}
			errors.add("Could not delete user!");
			request.setAttribute("deleteusererrors", errors);
		}
		
		/* Admin edit someone's Profile */
		Map<String, Object> properties;

		if (request.getParameter("edituserprofile") != null) {
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
				errors = user.editObject(username, properties);
				
				request.setAttribute("modifyuser", username);	
				request.setAttribute("edituserprofileerrors", errors);
				if (errors.isEmpty())
					request.setAttribute("goodedituser", "true");
				doGet(request, response);
				return;
		}
		
		/* Admin change user password */
		if (request.getParameter("changeuserpassword")  != null) {
			String newPassword = request.getParameter("newpassword");
			String verifyNewPassword = request.getParameter("verifynewpassword");
			if (!newPassword.equals(verifyNewPassword))
				errors.add("Password did not match!");
			else {
				properties = Library.propertyMapBuilder("password", newPassword);
				errors = user.editObject(username, properties);
			}

			request.setAttribute("changepassworderrors", errors);
			request.setAttribute("modifyuser", username);
			if (errors.isEmpty())
				request.setAttribute("goodchangepassword", "true");
			doGet(request, response);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/userlist");	
	}
}