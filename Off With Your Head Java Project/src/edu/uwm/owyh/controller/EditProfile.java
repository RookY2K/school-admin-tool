package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;

@SuppressWarnings("serial")
public class EditProfile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Person user = (Person)Auth.getSessionVariable(request, "user");
		if (user != null)
		{
			response.sendRedirect(request.getContextPath() + "/editprofile.jsp");
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
		if (! auth.verifyAdmin(response)) return;
		Person user = (Person) Auth.getSessionVariable(request, "user");
	    Map<String, Object> properties = 
	    		Library.propertySetBuilder("name",request.getParameter("name")
	    				                  ,"phone",request.getParameter("phone")
	    				                  ,"address",request.getParameter("address"));
		
	    List<String> errors = user.editPerson(request.getParameter("email"), properties);
	    		
	    if (!errors.isEmpty())
		{
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(request.getContextPath()+"/editprofile.jsp").forward(request,response);
			
		}else{
			Auth.setSessionVariable(request, "user", user);
			response.sendRedirect(request.getContextPath() + "/profile");			
		}
	}
}