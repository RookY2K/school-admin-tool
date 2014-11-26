package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class AddContactInfo extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;

		request.getRequestDispatcher(request.getContextPath() + "/admin/addContactInfo.jsp").forward(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		AccessLevel accessLevel = null;
		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			accessLevel = AccessLevel.getAccessLevel(access);
		}catch (NumberFormatException e) {
			//Should not happen
			request.setAttribute("addNewUser", false);
		}	
		
		Map<String, Object> properties = 
				Library.propertyMapBuilder("firstname",request.getParameter("firstname")
										  ,"lastname",request.getParameter("lastname")
						                  ,"phone",request.getParameter("phone")
						                  ,"streetaddress",request.getParameter("streetaddress")
						                  ,"city",request.getParameter("city")
						                  ,"state",request.getParameter("state")
						                  ,"zip",request.getParameter("zip")
						                  ,"accesslevel",accessLevel
						                  );
		
		WrapperObject<Person> newUser = WrapperObjectFactory.getPerson();
		List<String> errors = newUser.addObject(request.getParameter("email"), properties);
		
		properties.put("email", request.getParameter("email"));
		request.setAttribute("properties", properties);
		
		if (! errors.isEmpty()) {
			request.setAttribute("errors",errors);
			request.getRequestDispatcher(request.getContextPath() + "/admin/addContactInfo.jsp").forward(request, response);	
			return;
		}

		request.getRequestDispatcher(request.getContextPath() + "/admin/addConfirm.jsp").forward(request, response);	
	}
}
