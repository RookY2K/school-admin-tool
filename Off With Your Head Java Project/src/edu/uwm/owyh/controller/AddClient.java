package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Client;
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class AddClient extends HttpServlet {
	
	Auth auth;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyUser(response);

		request.getRequestDispatcher("adduser.jsp").forward(request, response);	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		auth.verifyAdmin(response);
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");	

		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
			
			Person newUser = UserFactory.getUser(email, password, accessLevel);
			if (newUser.addPerson()) 
				request.setAttribute("addNewUser", true);
			else 
				request.setAttribute("addNewUser", false);
		}
		catch (NumberFormatException e) {
			request.setAttribute("addNewUser", false);
		}
		
		request.getRequestDispatcher("adduser.jsp").forward(request, response);	
	}
}
