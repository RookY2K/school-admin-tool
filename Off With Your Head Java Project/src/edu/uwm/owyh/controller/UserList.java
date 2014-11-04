package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

@SuppressWarnings("serial")
public class UserList extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyUser(response);
		
		Person helper = UserFactory.getUser();
		
		List<Person> clients = helper.getAllPersons();

		String[] name = new String[clients.size()];
		String[] username = new String[clients.size()];
		int[] accesslevel = new int[clients.size()];
		
		for (int i = 0; i < clients.size(); i++) {
			name[i] = (String) clients.get(i).getProperty("name");
			username[i] = clients.get(i).getUserName();
			accesslevel[i] = ((AccessLevel) clients.get(i).getProperty("accesslevel")).getVal();
		}
		
		request.setAttribute("name", name);
		request.setAttribute("username", username);
		request.setAttribute("accesslevel", accesslevel);
		
		request.getRequestDispatcher("users.jsp").forward(request, response);	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyAdmin(response);
		
		Person helper = UserFactory.getUser();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> item = request.getParameterMap();
		
		if (item.keySet().size() > 0) {
			for (String key : item.keySet()) {
				Person user = helper.findPerson(key);
				if (user != null)
					user.removePerson(key);
			}
		}
		
		response.sendRedirect(request.getContextPath() + "/userlist");	
	}
}