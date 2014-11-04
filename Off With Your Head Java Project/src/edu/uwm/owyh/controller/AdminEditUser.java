package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

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
		
		int access = Integer.parseInt(request.getParameter("accesslevel"));
		AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
	    Person user = UserFactory.getUser();
		Map<String, Object> properties = 
				Library.propertySetBuilder("accesslevel",accessLevel
						                  ,"name", request.getParameter("name")
						                  ,"phone", request.getParameter("phone")
						                  ,"address", request.getParameter("address"));
		
		List<String> errors = user.editPerson(request.getParameter("username"), properties);
		if (errors.isEmpty())
		{
			request.setAttribute("user", user);
			request.setAttribute("isEdited", true);
			Person client = (Person)Auth.getSessionVariable(request, "user");
				if(user.getUserName().equalsIgnoreCase(client.getUserName())){
					Auth.setSessionVariable(request, "user", user);
				}
		}else{
			request.setAttribute("errors", errors);
			request.setAttribute("user", user);
			request.setAttribute("isEdited", false);
		}
			
		request.getRequestDispatcher(request.getContextPath() + "/admin/AdminEditUser.jsp").forward(request,response);	
	}
}