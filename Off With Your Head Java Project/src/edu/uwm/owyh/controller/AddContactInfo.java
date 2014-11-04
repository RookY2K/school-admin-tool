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
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class AddContactInfo extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyUser(response);

		response.sendRedirect(request.getContextPath() + "/admin/addContactInfo.jsp");	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		auth.verifyAdmin(response);
		
		AccessLevel accessLevel = null;
		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			accessLevel = AccessLevel.getAccessLevel(access);
		}catch (NumberFormatException e) {
			//Should not happen
			request.setAttribute("addNewUser", false);
		}	
		
		Map<String, Object> properties = 
				Library.propertySetBuilder("name",request.getParameter("name")
						                  ,"phone",request.getParameter("phone")
						                  ,"address",request.getParameter("address")
						                  ,"accesslever",accessLevel);
		
		Person newUser = UserFactory.getUser();
		List<String> errors = newUser.addPerson(request.getParameter("email"), properties);
		if (errors.isEmpty()) {
			request.setAttribute("addNewUser", true);
		}
		else {
			request.setAttribute("errors",errors);
			request.setAttribute("addNewUser", false);
		}		
		
		request.getRequestDispatcher("addContactInfo.jsp").forward(request, response);	
	}
}
