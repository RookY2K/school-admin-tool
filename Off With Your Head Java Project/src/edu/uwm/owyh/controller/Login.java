package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.User.AccessLevel;

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
		Entity user = Auth.getAuth(null).verifyLogin(username,password);
		boolean isLogin = (user != null);
		
		if (isLogin){
			Long accessLong = (Long)user.getProperty("accesslevel");
			int accessVal = accessLong.intValue();
			AccessLevel access = AccessLevel.getAccessLevel(accessVal);
			boolean isAdmin = access == AccessLevel.ADMIN;
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("accesslevel", access);
			response.sendRedirect("/?login=" + isLogin + "&admin=" + isAdmin);			
		}else{					
			response.sendRedirect("/?login=" + isLogin);
		}
	}
}
