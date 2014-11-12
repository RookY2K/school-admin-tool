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
		
		Auth auth = Auth.getAuth(request);
		if(! auth.verifyUser(response)) return;
		
		response.sendRedirect(request.getContextPath() + "/editprofile.jsp");		
			
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		Person user = (Person) Auth.getSessionVariable(request, "user");
	    Map<String, Object> properties = 
	    		Library.propertySetBuilder("firstname",request.getParameter("firstname")
	    								  ,"lastname",request.getParameter("lastname")
	    				                  ,"phone",request.getParameter("phone")
	    				                  ,"streetaddress",request.getParameter("streetaddress")
	    				                  ,"city",request.getParameter("city")
	    				                  ,"state",request.getParameter("state")
	    				                  ,"zip",request.getParameter("zip")
	    				                  );
		
	    List<String> errors = user.editPerson(request.getParameter("email"), properties);
	    		
	    if (!errors.isEmpty())
		{
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(request.getContextPath()+"/editprofile.jsp").forward(request,response);
			
		}else{
			Auth.setSessionVariable(request, "user", user);
			response.sendRedirect("/profile");			
		}
	}
}