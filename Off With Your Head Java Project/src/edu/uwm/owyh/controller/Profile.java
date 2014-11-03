package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.User;

@SuppressWarnings("serial")
public class Profile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		/* Find the logged in user. They're the only ones who can view thier profile. */
		User user = User.findUser((String)Auth.getSessionVariable(request, "username"));
		if (user != null)
		{
			request.setAttribute("user", user);
			request.getRequestDispatcher("profile.jsp").forward(request, response);
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
		
		    /* Shouldn't get here, but here's a placeholder just in case. */
			response.sendRedirect("/profile");			
	}
}