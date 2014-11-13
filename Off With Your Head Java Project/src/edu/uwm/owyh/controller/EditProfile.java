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
import edu.uwm.owyh.model.WrapperObject;
import edu.uwm.owyh.model.WrapperObjectFactory;

@SuppressWarnings("serial")
public class EditProfile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(! auth.verifyUser(response)) return;
		
		/* Admin edit another User's Profile */
		String username = request.getParameter("username");
		WrapperObject user = null;
		if (username != null && auth.verifyAdmin()) {
			user = WrapperObjectFactory.getPerson().findObject(username);
		}
		
		/* User Edit there Own Profile */
		if (user == null)
			user = (Person)Auth.getSessionVariable(request,"user");
		
		request.setAttribute("user", user);
		request.getRequestDispatcher("/editprofile.jsp").forward(request, response);	
			
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Admin attempt to start editing profile from UserList */
		String username = request.getParameter("username");
		if (username != null) {
			doGet(request, response);
			return;
		}
		
		String email = request.getParameter("email");
		WrapperObject user = WrapperObjectFactory.getPerson().findObject(email);
		WrapperObject self = (WrapperObject)Auth.getSessionVariable(request, "user");
		
		/* Prevent non-Admin from editing other people, Redirect to User own profile */
		if (user == null || (!self.getUserName().equals(user.getUserName()) && !auth.verifyAdmin())) {
			response.sendRedirect("/profile");		
			return;
		}
			
	    Map<String, Object> properties = 
	    		Library.propertySetBuilder("firstname",request.getParameter("firstname")
	    								  ,"lastname",request.getParameter("lastname")
	    				                  ,"phone",request.getParameter("phone")
	    				                  ,"streetaddress",request.getParameter("streetaddress")
	    				                  ,"city",request.getParameter("city")
	    				                  ,"state",request.getParameter("state")
	    				                  ,"zip",request.getParameter("zip")
	    				                  );
		
	    List<String> errors = user.editObject(request.getParameter("email"), properties);
	    	
	    
	    if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(request.getContextPath()+"/editprofile.jsp").forward(request,response);
			
		}
	    else if (self.getUserName().equals(user.getUserName())) {
	    	/* User edit there own profile, go back to view there profile */
			response.sendRedirect("/profile");	
			Auth.setSessionVariable(request, "user", user);
		}
	    else {
	    	/* Admin edit another User Profile, go to userList */
	    	response.sendRedirect("/userlist");	
	    }
	}
}