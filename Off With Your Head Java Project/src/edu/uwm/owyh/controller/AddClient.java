package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class AddClient extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)){
			return;
		}

		response.sendRedirect(request.getContextPath() + "/admin/adduser.jsp");	
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)){
			return;
		}
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");	

		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
			Map<String, Object> properties = Library.propertySetBuilder("password",password
					                                                   ,"accesslevel",accessLevel);
			WrapperObject newUser = WrapperObjectFactory.getPerson();
			List<String> errors = newUser.addObject(email, properties);
			if (errors.isEmpty()) 
				request.setAttribute("addNewUser", true);
			else{
				request.setAttribute("addNewUser", false);
				request.setAttribute("errors", errors);
			}
		}
		catch (NumberFormatException e) {
			//Shouldn't happen
			request.setAttribute("addNewUser", false);
		}
		
		request.getRequestDispatcher("adduser.jsp").forward(request, response);	
	}
}
