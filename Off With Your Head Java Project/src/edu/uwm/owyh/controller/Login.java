package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.WrapperObject;
import edu.uwm.owyh.model.WrapperObjectFactory;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (request.getParameter("login").equals("logout")) {
			Auth.destroySession(request);
		}
		
		response.sendRedirect("/");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		WrapperObject user = Auth.getAuth(null).verifyLogin(username,password);
		boolean isLogin = (user != null);
		request.setAttribute("isLogin", isLogin);
		
		if (isLogin){
//			WrapperObject user = WrapperObjectFactory.getUser().findPerson(username);
			
			Auth.setSessionVariable(request, "user", user);					
		}
		response.sendRedirect(request.getContextPath() + "/");
	}
}
