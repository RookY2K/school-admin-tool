package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class Profile extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Find the logged in user. They're the only ones who can view their profile. */
		Person user = (Person)Auth.getSessionVariable(request, "user");
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
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		Person helper = UserFactory.getUser();
		Person user = null;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> item = request.getParameterMap();
		
		if (item.keySet().size() > 0) {
			for (String key : item.keySet()) {
				user = helper.findPerson(key);
				break;
			}
		}
		
		if (user != null) 
			request.setAttribute("user", user);
		
		request.getRequestDispatcher(request.getContextPath() + "profile.jsp").forward(request, response);			
	}
}