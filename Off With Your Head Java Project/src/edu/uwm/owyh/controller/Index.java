package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;


@SuppressWarnings("serial")
public class Index extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: actually check if user is login and if user is admin
		
		boolean isLogin = false;
		boolean isAdmin = true;
		
		if (isLogin) {
			
			if (isAdmin) 
				request.getRequestDispatcher("admin.jsp").forward(request, response);	
			else 
				request.getRequestDispatcher("home.jsp").forward(request, response);

		}
		else {
			request.getRequestDispatcher("index.jsp").forward(request, response);	
		}
				
		
	}
}
