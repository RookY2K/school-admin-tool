package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (request.getParameter("logout") != null && request.getParameter("logout").equals("true")) {
			Auth.destroySession(request);
		}
		
		response.sendRedirect("/");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		WrapperObject<Person> user = Auth.getAuth(null).verifyLogin(username,password);
		if (user == null) {
			user = Auth.getAuth(null).verifyTempLogin(username,password);
		}
			
		boolean isLogin = (user != null);
		
		if (isLogin){
			Auth.setSessionVariable(request, "user", user);		
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}

		
		response.sendRedirect(request.getContextPath() + "/?login=fail");
	}
}
