package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Client;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class UserListEditButton extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.sendRedirect("/userlist");	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> item = request.getParameterMap();
		
		Person helper = UserFactory.getUser(true);
		if (item.keySet().size() > 0) {
			for (String key : item.keySet()) {
				Person user = helper.findPerson(key);
				if (user != null)
				{
					request.setAttribute("user", user);
					request.getRequestDispatcher("/admin/AdminEditUser.jsp").forward(request, response);
				}
				else
					response.sendRedirect("/userlist");
			}
		}
        else
        	response.sendRedirect("/userlist");	
	}
}