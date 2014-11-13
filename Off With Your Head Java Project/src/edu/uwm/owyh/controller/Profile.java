package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.WrapperObject;
import edu.uwm.owyh.model.WrapperObjectFactory;

@SuppressWarnings("serial")
public class Profile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Find the logged in user. They're the only ones who can view their profile. */
		WrapperObject user = (WrapperObject)Auth.getSessionVariable(request, "user");
		if (user != null)
		{
			request.setAttribute("user", user);
			request.getRequestDispatcher(request.getContextPath() + "/profile.jsp").forward(request, response);	
	
		}
		else
		{
			/* Should never get here, but this implies user isn't logged. 
			 * Redirect to index. */
			response.sendRedirect("/");
		}
			
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* View a User's Profile from the UserList */
		String username = request.getParameter("username");
		if (username == null) {
			response.sendRedirect("/userlist");
			return;
		}
		
		WrapperObject user = WrapperObjectFactory.getPerson().findObject(username);
		
		if (user != null) 
			request.setAttribute("user", user);
		
		request.getRequestDispatcher(request.getContextPath() + "profile.jsp").forward(request, response);			
	}
}