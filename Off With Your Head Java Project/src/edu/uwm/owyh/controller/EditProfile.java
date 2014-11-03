package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class EditProfile extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Person user = (Person)Auth.getSessionVariable(request, "user");
		if (user != null)
		{
			response.sendRedirect(request.getContextPath() + "/editprofile.jsp");
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
		
	    Person user = (Person) request.getSession().getAttribute("user");
		if (user != null)
		{
			user.setName(name);
			user.setPhone(phone);
			user.setEmail(email);
			user.setAddress(address);
			
			user.editPerson();
		}
		response.sendRedirect(request.getContextPath() + "/profile");
			
	}
}