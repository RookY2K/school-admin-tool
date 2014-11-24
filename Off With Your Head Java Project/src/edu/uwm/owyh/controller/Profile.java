package edu.uwm.owyh.controller;

import java.io.IOException;

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
public class Profile extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Find the logged in user. They're the only ones who can view their profile. */
		WrapperObject<Person> user = (WrapperObject<Person>)Auth.getSessionVariable(request, "user");
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		if (user != null)
		{
			request.setAttribute("user", Library.makeUserProperties(user));
			request.setAttribute("self", Library.makeUserProperties(self));
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
		
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		request.setAttribute("self", Library.makeUserProperties(self));
		
		/* View a User's Profile from the UserList */
		String username = request.getParameter("username");
		if (username == null) {
			response.sendRedirect("/userlist");
			return;
		}
		Key id = Library.generateIdFromUserName(username);
		WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
		
		if (user == null) 
			user = self;

		request.setAttribute("user", Library.makeUserProperties(user));
		
		request.getRequestDispatcher(request.getContextPath() + "profile.jsp").forward(request, response);			
	}
}