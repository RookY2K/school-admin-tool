package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if (! auth.verifyAdmin(response)) return;
		
	    String accesslevel = request.getParameter("accesslevel");
	    String name = request.getParameter("name");
	    String phone = request.getParameter("phone");
	    String address = request.getParameter("address");
	    String userName = request.getParameter("username");
		
	    Person helper = UserFactory.getUser(true);
		Person user = helper.findPerson(userName);
		if (user != null)
		{
			user.setAccessLevel(AccessLevel.getAccessLevel(Integer.parseInt(accesslevel)));
			user.setName(name);
			user.setPhone(phone);
			user.setAddress(address);
			
			if(user.editPerson()){
				request.setAttribute("user", user);
				request.setAttribute("isEdited", true);
				Person client = (Person)request.getSession().getAttribute("user");
				if(user.getUserName().equalsIgnoreCase(client.getUserName())){
					//Auth.removeSessionVariable(request, "user");
					Auth.setSessionVariable(request, "user", user);
				}
			}else{
				request.setAttribute("user", user);
				request.setAttribute("isEdited", false);
			}
		}	
		request.getRequestDispatcher(request.getContextPath() + "/admin/AdminEditUser.jsp").forward(request,response);	
	}
}