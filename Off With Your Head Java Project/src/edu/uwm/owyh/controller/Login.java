package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	Auth auth;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (request.getParameter("login") == "false") {
			// TODO user requested logout, Destroy Session!
		}
		
		response.sendRedirect("/");	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		auth = Auth.getAuth(request);
		boolean loginFail = false;
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username.equals("") || password.equals("")) loginFail = true;
		else if (! auth.verifyLogin(username, password)) loginFail = true;

		if (loginFail)
			response.sendRedirect("/?login=bad");
		else {
			
			// TODO: Log good at this point. Create User Session!
			
			response.sendRedirect("/");
		}

	}
}
