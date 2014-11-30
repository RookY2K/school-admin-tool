package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;


@SuppressWarnings("serial")
public class Admin extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)) return;
		
		request.getRequestDispatcher(request.getContextPath() + "/admin/admin.jsp").forward(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)) return;
		
		Map<String, Object> properties = null;
		List<String> errors = new ArrayList<String>();
		
		AccessLevel accessLevel = null;
		if(request.getParameter("accesslevel") != null) {
			try {
				int access = Integer.parseInt(request.getParameter("accesslevel"));
				accessLevel = AccessLevel.getAccessLevel(access);
			}
			catch (NumberFormatException e) {
				errors.add("Invalid AccessLevel!");
			}
		}
		
		// Add new User
		if (request.getParameter("addnewuser") !=null) {
			
			String password = request.getParameter("password");	
			String verifyPassword = request.getParameter("verifypassword");	
			
			if (password == null || password.equals("") || verifyPassword == null)
				errors.add("Invalid Password!");
			if (password != null && !password.equals(verifyPassword))
				errors.add("Passwords does not Match!");
			
			if (errors.isEmpty()) {			
				properties = Library.propertyMapBuilder("firstname",""
			              ,"lastname",""
			              ,"email", request.getParameter("email")
			              ,"phone",""
			              ,"streetaddress",""
			              ,"city",""
			              ,"state",""
			              ,"zip",""
			              ,"password", request.getParameter("password")
			              ,"accesslevel", accessLevel
			             );
				for(String key : properties.keySet())
					if(properties.get(key) == null) properties.put(key, "");
				errors = WrapperObjectFactory.getPerson().addObject(request.getParameter("email"), properties);
			}
			
			request.setAttribute("addnewusererrors", errors);
			request.setAttribute("newuser", properties);
			request.getRequestDispatcher(request.getContextPath() + "/admin/admin.jsp").forward(request, response);
			return;
		}

		// Add Contact Information
		if (request.getParameter("addcontactinfo") !=null) {
			properties = Library.propertyMapBuilder("firstname", request.getParameter("firstname")
		              ,"lastname", request.getParameter("lastname")
		              ,"email", request.getParameter("email")
		              ,"phone", request.getParameter("phone")
		              ,"streetaddress", request.getParameter("streetaddress")
		              ,"city",request.getParameter("city")
		              ,"state",request.getParameter("state")
		              ,"zip",request.getParameter("zip")
		              ,"password", ""
		              ,"accesslevel", accessLevel
		             );
			for(String key : properties.keySet())
				if(properties.get(key) == null) properties.put(key, "");
			errors = WrapperObjectFactory.getPerson().addObject(request.getParameter("email"), properties);
				
			if (!errors.isEmpty()) {
				request.setAttribute("addcontactinfoerrors", errors);
				request.setAttribute("baduserinfo", properties);
			}
			else
				request.setAttribute("newuser", properties);
			
			request.getRequestDispatcher(request.getContextPath() + "/admin/admin.jsp").forward(request, response);	
			return;
		}
		
		// Load Classlist
		if(request.getParameter("reloadclassschedule") != null){
			response.sendRedirect(request.getContextPath() + "/admin/scraper");
			return;
		}
	
		response.sendRedirect(request.getContextPath() + "/admin#close");
	}
}
