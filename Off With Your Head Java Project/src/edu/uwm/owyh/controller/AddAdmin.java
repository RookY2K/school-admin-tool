package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Client;
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class AddAdmin extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Boolean isAddAdmin = (Boolean)session.getAttribute("isAddAdmin");
		
		if(isAddAdmin != null && isAddAdmin.booleanValue()){
			response.sendRedirect(request.getContextPath() + "/admin/addadmin.jsp");
			return;
		}else{
			request.getRequestDispatcher("/").forward(request,response);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");	

		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
			
			Person newUser = UserFactory.getUser(email, password, accessLevel);
			if (newUser.addPerson()){
				HttpSession session = request.getSession();
				session.setAttribute("username", email);
				session.setAttribute("accesslevel", accessLevel);
				session.removeAttribute("isAddAdmin");
				response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");	
			}else{ 
				request.setAttribute("addNewUser", false);
				request.getRequestDispatcher("addadmin.jsp").forward(request, response);
			}
		}
		catch (NumberFormatException e) {
			request.setAttribute("addNewUser", false);
			request.getRequestDispatcher("addadmin.jsp").forward(request, response);
		}
		
			
	}
}
