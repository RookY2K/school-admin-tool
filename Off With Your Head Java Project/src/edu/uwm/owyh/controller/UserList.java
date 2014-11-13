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
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		Person user = (Person)Auth.getSessionVariable(request, "user");
			
		List<Person> clients = user.getAllPersons();

		String[] firstname = new String[clients.size()];
		String[] lastname = new String[clients.size()];
		String[] username = new String[clients.size()];
		int[] accesslevel = new int[clients.size()];
		
		for (int i = 0; i < clients.size(); i++) {
			firstname[i] = (String) clients.get(i).getProperty("firstname");
			lastname[i] = (String) clients.get(i).getProperty("lastname");
			username[i] = (String) clients.get(i).getProperty("username");
			accesslevel[i] = ((AccessLevel) clients.get(i).getProperty("accesslevel")).getVal();
		}
		
		request.setAttribute("user", user);
		request.setAttribute("firstname", firstname);
		request.setAttribute("lastname", lastname);
		request.setAttribute("username", username);
		request.setAttribute("accesslevel", accesslevel);
		
		request.getRequestDispatcher("users.jsp").forward(request, response);	
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		Person helper = UserFactory.getUser();
		Person me = (Person) Auth.getSessionVariable(request, "user");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> item = request.getParameterMap();
		
		if (item.keySet().size() > 0) {
			for (String key : item.keySet()) {
				Person user = helper.findPerson(key);
				if (user != null) {
					if (user.getUserName().equals(me.getUserName())) {
						response.sendRedirect(request.getContextPath() + "/userlist?error");	
						return;
					}
					else {
						user.removePerson(key);
						response.sendRedirect(request.getContextPath() + "/userlist?deleted");	
						return;	
					}
				}
			}
		}
		
		response.sendRedirect(request.getContextPath() + "/userlist");	
	}
}