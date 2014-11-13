package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class UserList extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		Person user = (Person)Auth.getSessionVariable(request, "user");
			
		List<Person> clients = user.getAllPersons();

		request.setAttribute("users", clients);
		
		request.getRequestDispatcher(request.getContextPath() + "userlist.jsp").forward(request, response);	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		String username = (String) request.getParameter("username");
		
		if (username != null) {
			Person user = UserFactory.getUser().findPerson(username);
			if (user != null) {
				if (UserFactory.getUser().removePerson(user.getUserName())) {
					response.sendRedirect(request.getContextPath() + "/userlist?deleted");	
					return;
				}

			}
		}

		response.sendRedirect(request.getContextPath() + "/userlist");	
	}
}