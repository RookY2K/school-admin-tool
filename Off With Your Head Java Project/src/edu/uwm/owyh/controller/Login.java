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
		
		if ((boolean) request.getAttribute("isLogout")) {
			// TODO user requested logout, Destroy Session!
		}
		
		request.getRequestDispatcher("/").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// TODO: Login		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Entity user = Auth.getAuth(null).verifyLogin(username,password);
		boolean isLogin = (user != null);
		request.setAttribute("isLogin", isLogin);
		
		if (isLogin){
			Long accessLong = (Long)user.getProperty("accesslevel");
			int accessVal = accessLong.intValue();
			AccessLevel access = AccessLevel.getAccessLevel(accessVal);
			request.setAttribute("isAdmin", access == AccessLevel.ADMIN);
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("accesslevel", access);
			
			request.getRequestDispatcher("/").forward(request, response);			
		}else{					
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
}
