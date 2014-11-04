package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;

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
		Entity userEntity = Auth.getAuth(null).verifyLogin(username,password);
		boolean isLogin = (userEntity != null);
		request.setAttribute("isLogin", isLogin);
		
		if (isLogin){
			Person user = UserFactory.getUser(userEntity);
			
			HttpSession session = request.getSession();
			session.setAttribute("user", user);	
			response.sendRedirect(request.getContextPath() + "/");
		}
		else {
			response.sendRedirect(request.getContextPath() + "/index.jsp?login=false");
		}
	}
}
