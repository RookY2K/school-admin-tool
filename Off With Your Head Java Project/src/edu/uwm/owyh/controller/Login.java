package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;

import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
	
	HttpSession session;
	
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

		// TODO: Login
		// String username = request.getParameter("username")
		// String password = request.getParameter("password")
		// 		Login(username, password);
		boolean loginFail = true;
		Entity user = null;
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		user = Auth.getAuth(null).verifyLogin(username,password);
		
		loginFail = user == null;
		
		if (loginFail)
			resp.sendRedirect("/?login=bad");
		else
			//TODO Save username,accesslevel to session variables
			session = request.getSession();
			response.sendRedirect("/");
		
	}
}
