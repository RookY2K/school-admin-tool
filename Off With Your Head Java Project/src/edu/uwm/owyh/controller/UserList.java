package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Email;

@SuppressWarnings("serial")
public class UserList extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if (!auth.verifyUser(response))	return;
		
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		List<WrapperObject<Person>> clients = null;
		
		/* Search User */
		Map<String, String> searchUser = new HashMap<String, String>();
		searchUser.put("name", request.getParameter("searchName"));
		searchUser.put("Admin", request.getParameter("searchAdmin"));
		searchUser.put("Instructor", request.getParameter("searchInstructor"));
		searchUser.put("TA", request.getParameter("searchTA"));
		
		if (request.getParameter("filteruser") != null) {
			// Filter by Role, Inclusively
			String filterRole = "(";
			if (searchUser.get("Admin") != null) filterRole += "accessLevel == 1";
			if (searchUser.get("Instructor") != null) filterRole += (filterRole.length() > 1) ? " || accessLevel == 2" : "accessLevel == 2";
			if (searchUser.get("TA") != null) filterRole += (filterRole.length() > 1) ? " || accessLevel == 3" : "accessLevel == 3";
			filterRole += ")";
			if (filterRole.length() < 3) filterRole = "";
			
			// FIlter by Email, Exclusively
			String filterUsername = (filterRole.length() > 0) ? " && " : "" ;
			if (searchUser.get("name") != null && !searchUser.get("name").equals("") && searchUser.get("name").toLowerCase().indexOf("@uwm.edu") >= 0)
				filterUsername += "(toUpperUserName == '" + searchUser.get("name").toUpperCase() + "')";
			else 
				filterUsername = "";

			if (filterUsername.length() + filterRole.length() != 0)
				clients = WrapperObjectFactory.getPerson().findObjects(filterRole + filterUsername, null, "userName");
			request.setAttribute("filteruser", searchUser);
		}

		/* Get list, if no search was entered */
		if (clients == null) clients = self.findObjects(null, null, "userName");
		List<Map<String, Object>> clientList = new ArrayList<Map<String, Object>>();
		
		for (WrapperObject<Person> client : clients)
			clientList.add(PropertyHelper.makeUserProperties(client));
		
		/* Filter By First or Last Name, Exclusively */
		if (searchUser.get("name") != null && !searchUser.get("name").equals("") && searchUser.get("name").toLowerCase().indexOf("@uwm.edu") < 0) {
			for (int i = 0; i < clientList.size();) {
				String firstName = (String) clientList.get(i).get("firstname");
				String lastName = (String) clientList.get(i).get("lastname");
				
				if (firstName.equalsIgnoreCase(searchUser.get("name")) || lastName.equalsIgnoreCase(searchUser.get("name")))
						i++;
				else
					clientList.remove(i);
			}
		}

		/* Admin view User Profile */
		String username = request.getParameter("modifyuser");
		if (request.getAttribute("modifyuser") != null)
			username = (String) request.getAttribute("modifyuser");
		if (username != null) {
			Key id = WrapperObjectFactory.generateIdFromUserName(username);
			WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
			if (user != null) {
				List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory
						.getOfficeHours().findObjects(null, user, null);
				request.setAttribute("officehours", PropertyHelper.makeOfficeHoursProperties(officeHours));
				request.setAttribute("modifyuser", PropertyHelper.makeUserProperties(user));
				
				/* User Skills */
				List<String> userSkills = (List<String>) user.getProperty("skills");
				String userSkillString = "";
				if (userSkills != null) {
					 for(String skill: userSkills)
						userSkillString += skill += ", ";
	
					/* Remove the last comma. */
					if (userSkillString.endsWith(", "))
						userSkillString = userSkillString.substring(0, userSkillString.length() - 2);
				}
				request.setAttribute("skills", userSkillString);
			}
		}

		request.setAttribute("self", PropertyHelper.makeUserProperties(self));
		request.setAttribute("users", clientList);
		request.getRequestDispatcher(request.getContextPath() + "userlist.jsp")
				.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if (!auth.verifyUser(response))
			return;

		/*
		 * This allow non-Admin to view and Admin to edit, delete user
		 * Redirected from another page
		 */
		if (request.getParameter("modifyuser") != null || request.getParameter("filteruser") != null) {
			doGet(request, response);
			return;
		}

		/* Only Admin can Edit Users, and go beyond this point */
		if (!auth.verifyAdmin(response))
			return;

		List<String> errors = new ArrayList<String>();
		WrapperObject<Person> user = null;

		/* Make sure a correct username was pass through */
		String username = (String) request.getParameter("email");

		if (username == null)
			errors.add("No username was posted!");
		else {
			Key id = WrapperObjectFactory.generateIdFromUserName(username);
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
			if (user.removeObject()) {
				response.sendRedirect(request.getContextPath()
						+ "/userlist#deleteuser");
				return;
			}
			errors.add("Could not delete user!");
			request.setAttribute("deleteusererrors", errors);
		}

		/* Admin edit someone's Profile */
		Map<String, Object> properties;

		AccessLevel accessLevel = null;
		if (request.getParameter("accesslevel") != null) {
			try {
				int access = Integer.parseInt(request
						.getParameter("accesslevel"));
				accessLevel = AccessLevel.getAccessLevel(access);
			} catch (NumberFormatException e) {
				errors.add("Invalid AccessLevel!");
			}
		}
		
		if (request.getParameter("edituserprofile") != null) {
			properties = PropertyHelper.propertyMapBuilder("firstname", request.getParameter("firstname")
					, "lastname", request.getParameter("lastname")
					, "email", request.getParameter("email")
					, "phone", request.getParameter("phone")
					, "streetaddress", request.getParameter("streetaddress")
					, "city", request.getParameter("city")
					, "state", request.getParameter("state")
					, "zip", request.getParameter("zip")
					, "accesslevel", accessLevel
					);
			for (String key : properties.keySet())
				if (properties.get(key) == null)
					properties.put(key, "");
			if (properties.get("accesslevel").equals(""))
				properties.remove("accesslevel");
			errors = user.editObject(properties);

			request.setAttribute("modifyuser", username);
			request.setAttribute("edituserprofileerrors", errors);
			if (errors.isEmpty())
				request.setAttribute("goodedituser", "true");
			doGet(request, response);
			return;
		}

		/* Admin change user password */
		if (request.getParameter("changeuserpassword") != null) {
			String newPassword = request.getParameter("newpassword");
			String verifyNewPassword = request
					.getParameter("verifynewpassword");
			if (!newPassword.equals(verifyNewPassword))
				errors.add("Password did not match!");
			else {
				properties = PropertyHelper
						.propertyMapBuilder("password", newPassword);
				errors = user.editObject(properties);
			}

			if (errors.isEmpty()) {
				request.setAttribute("goodchangepassword", "true");
				String name = (String) user.getProperty("firstname") + " "
						+ (String) user.getProperty("lastname");
				String msg = "Off With Your Head \n Your password has been change by the Administrator. \n Your new Password is: "
						+ newPassword;
				errors = Email.sendMessage(username, name,
						"OWYH Password Change", msg);
			}
			request.setAttribute("changepassworderrors", errors);
			request.setAttribute("modifyuser", username);
			doGet(request, response);
			return;
		}

		/* Admit edit user skills */
		String inputString = request.getParameter("skilllist");

		if (inputString == null)
			inputString = "";

		List<String> skillList = new ArrayList<String>(
				Arrays.asList(inputString.split(",")));
		
		for (int i = 0; i < skillList.size();) {
			skillList.set(i, skillList.get(i).trim());
			if (skillList.get(i).equals(""))
				skillList.remove(i);
			else 
				i++;	
		}

		if (request.getParameter("edituserskills") != null) {
			properties = PropertyHelper.propertyMapBuilder("skills", skillList);
			errors = user.editObject(properties);

			request.setAttribute("modifyuser", username);
			request.setAttribute("edituserprofileerrors", errors);
			if (errors.isEmpty()) {
				request.setAttribute("goodedituser", "true");
				doGet(request, response);
				return;
			}
		}

		response.sendRedirect(request.getContextPath() + "/userlist");
	}
}