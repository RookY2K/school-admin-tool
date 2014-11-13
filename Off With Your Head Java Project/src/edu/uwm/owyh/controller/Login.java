package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (request.getParameter("login").equals("logout")) {
			Auth.destroySession(request);
		}
		
		response.sendRedirect("/");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Person user = Auth.getAuth(null).verifyLogin(username,password);
		boolean isLogin = (user != null);
		request.setAttribute("isLogin", isLogin);
		
		if (isLogin){
//			Person user = UserFactory.getUser().findPerson(username);
			
			Auth.setSessionVariable(request, "user", user);					
		}
		response.sendRedirect(request.getContextPath() + "/");
	}
}
