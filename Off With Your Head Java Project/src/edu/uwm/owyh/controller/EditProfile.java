package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.User;

@SuppressWarnings("serial")
public class EditProfile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		User user = User.findUser((String)Auth.getSessionVariable(request, "username"));
		if (user != null)
		{
			request.setAttribute("user", user);
			request.getRequestDispatcher("editprofile.jsp").forward(request, response);
		}
		else
		{
			/* Should never get here, but this implies user isn't logged. 
			 * Redirect to index. */
			response.sendRedirect("/");
		}
			
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
        
	    String name = request.getParameter("name");
	    String phone = request.getParameter("phone");
	    String address = request.getParameter("address");
	    String email = request.getParameter("email");
			
		User user = User.findUser((String)Auth.getSessionVariable(request, "username"));
		if (user != null)
		{
			user.setName(name);
			user.setPhone(phone);
			user.setEmail(email);
			user.setAddress(address);
			
			user.saveUser();
			
			response.setContentType("text/html");
			response.getWriter().write("<meta http-equiv=\"refresh\" content=\"4; url=/profile\">");
			response.getWriter().write("Writing to Database, You be will automaticlly rediected in 4 seconds...");
		}
		else
		{
			response.sendRedirect("/profile");
		}
			
	}
}