package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;

@SuppressWarnings("serial")
public class Profile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		/* Find the logged in user. They're the only ones who can view thier profile. */
		Person user = (Person)Auth.getSessionVariable(request, "user");
		if (user != null)
		{
			response.sendRedirect(request.getContextPath() + "/profile.jsp");		
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