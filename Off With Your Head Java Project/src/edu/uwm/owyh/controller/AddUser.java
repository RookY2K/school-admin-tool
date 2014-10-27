package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class AddUser extends HttpServlet {
	
	Auth auth;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: admin authentication
		
		request.getRequestDispatcher("adduser.jsp").forward(request, response);	
	}
	
	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// TODO: admin authentication
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		
		// TODO: call Add User function from where ever it is
		
		// TODO: add a message displaying that user was atually added
		
		request.getRequestDispatcher("adduser.jsp").forward(request, response);	
	}
}
