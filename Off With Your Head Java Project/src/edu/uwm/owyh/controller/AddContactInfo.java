package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.ContactCard;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Client;
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
		
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		AccessLevel accessLevel = null;
		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			accessLevel = AccessLevel.getAccessLevel(access);
		}catch (NumberFormatException e) {
			//Should not happen
			request.setAttribute("addNewUser", false);
		}	
		
		Person newUser = UserFactory.getUser(name, phone, address, email);
		if(accessLevel != null) newUser.setAccessLevel(accessLevel);
		
		if (newUser.addPerson()) {
			request.setAttribute("addNewUser", true);
		}
		else {
			request.setAttribute("addNewUser", false);
		}		
		
		request.getRequestDispatcher("addContactInfo.jsp").forward(request, response);	
	}
}
