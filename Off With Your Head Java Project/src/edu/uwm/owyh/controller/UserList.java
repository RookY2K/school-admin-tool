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
		
		/* Admin edit User Profile, This can come from multiple places */
		String username = request.getParameter("edituserprofilefromview");
		if (request.getParameter("viewadduserprofile") != null)
			username = request.getParameter("viewadduserprofile");
		else if (request.getParameter("modifyuser") != null)
			username = request.getParameter("modifyuser");
		else if (request.getParameter("edituserprofilefromadmin") != null)
			username = request.getParameter("edituserprofilefromadmin");
		if (username != null) {
			Key id = Library.generateIdFromUserName(username);
			WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
			if (user != null)
				request.setAttribute("modifyuser", Library.makeUserProperties(user));				
		}		
		
		request.setAttribute("self", Library.makeUserProperties(self));
		request.setAttribute("users", clientList);
		request.getRequestDispatcher(request.getContextPath() + "userlist.jsp").forward(request, response);	
	}
	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		/* This allow Admin to edit, delete user Redirected from another page */
		if (request.getParameter("edituserprofilefromview") != null || 
				request.getParameter("modifyuser") != null || 
				request.getParameter("edituserprofilefromadmin") != null) {
			doGet(request, response);
			return;
		}

		List<String> errors = new ArrayList<String>();
		WrapperObject<Person> user = null;
		
		/* Make sure a correct username was pass through */
		String username = (String) request.getParameter("username");
		
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
			if (WrapperObjectFactory.getPerson().removeObject((String)user.getProperty("username"))) {
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
				errors = user.editObject(request.getParameter("email"), properties);
				
				request.setAttribute("modifyuser", properties);
				
				request.setAttribute("edituserprofileerrors", errors);
				if (errors.isEmpty())
					request.setAttribute("goodedituser", "true");
				doGet(request, response);
				return;
		}
		
		response.sendRedirect(request.getContextPath() + "/userlist");	
	}
}