package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.User;
import edu.uwm.owyh.model.User.AccessLevel;

@SuppressWarnings("serial")
public class AdminEditUser extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.sendRedirect("/userlist");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyAdmin(response);
		
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    String accesslevel = request.getParameter("accesslevel");
	    String name = request.getParameter("name");
	    String phone = request.getParameter("phone");
	    String address = request.getParameter("address");
	    String email = request.getParameter("email");
			
		User user = User.findUser(username);
		if (user != null)
		{
			if(password.isEmpty() == false) 
		        user.setPassword(password);
			
			user.setAccessLevel(AccessLevel.getAccessLevel(Integer.parseInt(accesslevel)));
			user.setName(name);
			user.setPhone(phone);
			user.setEmail(email);
			user.setAddress(address);
			
			user.saveUser();
			
			response.setContentType("text/html");
			response.getWriter().write("<meta http-equiv=\"refresh\" content=\"4; url=/userlist\">");
			response.getWriter().write("Writing to Database, You be will automaticlly rediected in 4 seconds...");
		}
		else
		{
			response.sendRedirect("/userlist");
		}
			
	}
}