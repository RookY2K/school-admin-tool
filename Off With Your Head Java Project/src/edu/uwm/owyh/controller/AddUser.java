package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.User;
import edu.uwm.owyh.model.User.AccessLevel;

@SuppressWarnings("serial")
public class AddUser extends HttpServlet {
	
	Auth auth;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: admin authentication

		request.getRequestDispatcher("adduser.jsp").forward(request, response);	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// TODO: admin authentication
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");	

		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
			
			User newUser = User.getUser(email, password, accessLevel);
			if (newUser.saveUser()) 
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
