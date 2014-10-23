package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: Logout
		//if (request.getParameter("login") == "false") 
		//		Logout();	
		
		response.sendRedirect("/");	
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		// TODO: Login
		// String username = request.getParameter("username")
		// String password = request.getParameter("password")
		// 		Login(username, password);

		boolean loginFail = true;
		
		if (loginFail)
			resp.sendRedirect("/?login=bad");
		else
			resp.sendRedirect("/");
		
	}
}
