package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Client;
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class AdminEditClient extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.sendRedirect("/userlist");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyAdmin(response);
		
	    String name = request.getParameter("username");
	    String password = request.getParameter("password");
	    String accesslevel = request.getParameter("accesslevel");
	    Person helper = UserFactory.getUser(true);
	    Person user = helper.findPerson(name);
		if (user != null)
		{
			if(!password.isEmpty()){
				user.setPassword(password);
			}
			
			user.setAccessLevel(AccessLevel.getAccessLevel(Integer.parseInt(accesslevel)));
			
			user.editPerson();
			
		}
		
		response.sendRedirect(request.getContextPath() + "/userlist");
	}
}