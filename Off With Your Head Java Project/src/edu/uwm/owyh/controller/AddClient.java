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
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class AddClient extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)){
			return;
		}

		response.sendRedirect(request.getContextPath() + "/admin/addContactInfo");	
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)){
			return;
		}
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");	
		String verifyPassword = request.getParameter("verifypassword");	
		
		List<String> errors = new ArrayList<String>();
		Map<String, Object> properties = null;
		
		if (password == null || password.equals("") || verifyPassword == null)
			errors.add("Invalid Password!");
		if (! password.equals(verifyPassword))
			errors.add("Passwords does not Match!");

		if (! errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(request.getContextPath() + "/admin/addContactInfo.jsp").forward(request, response);
			return;
		}
		
		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
			properties = Library.propertySetBuilder("password",password
					                                 ,"accesslevel",accessLevel);
			WrapperObject<Person> newUser = WrapperObjectFactory.getPerson();
			errors = newUser.addObject(email, properties);
			
			properties = Library.propertySetBuilder("firstname",""
		              ,"lastname",""
		              ,"email", request.getParameter("email")
		              ,"phone",""
		              ,"accesslevel",""
		              ,"streetaddress",""
		              ,"city",""
		              ,"state",""
		              ,"zip",""
		              ,"password", password
		              ,"accesslevel", accessLevel
		              );
		}
		catch (NumberFormatException e) {
			errors.add("Invalid AccessLevel!");
		}
		
		request.setAttribute("properties", properties);
		
		if (! errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("addContactInfo.jsp").forward(request, response);
			return;
		}
		
		request.getRequestDispatcher(request.getContextPath() + "/admin/addConfirm.jsp").forward(request, response);	
	}
}
