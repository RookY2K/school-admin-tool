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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Find the logged in user. They're the only ones who can view thier profile. */
		Person user = (Person) request.getSession().getAttribute("user");
		if (user != null)
		{
			request.setAttribute("user", user);
			request.getRequestDispatcher("profile.jsp").forward(request, response);	
			//String phoneString = user.getPhone();
			/*request.getSession().setAttribute("user", user);
			request.setAttribute("user", user);*/
			//response.sendRedirect(request.getContextPath() + "/profile.jsp");
			//request.getRequestDispatcher("profile.jsp").forward(request, response);
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
		
		Person helper = UserFactory.getUser(true);
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
		
		request.getRequestDispatcher("profile.jsp").forward(request, response);			
	}
}