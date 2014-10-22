package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: Catch Logout
		//if (request.getParameter("login") == "false") 
		//		Logout();	
		
		response.sendRedirect("/");	
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		// TODO: Check login
		// 		Login();

		// will always fail for now
		boolean loginFail = true;
		
		if (loginFail)
			resp.sendRedirect("/?login=bad");
		else
			resp.sendRedirect("/");
		
	}
}
