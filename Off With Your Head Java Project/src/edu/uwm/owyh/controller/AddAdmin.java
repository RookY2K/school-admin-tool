package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class AddAdmin extends HttpServlet{
	
	@Override
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
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: prevent direct access input, when initial admin already exist
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String copyPassword = request.getParameter("passwordcopy");
		if(!password.equals(copyPassword)){
			List<String> errors = new ArrayList<String>();
			errors.add("Passwords do not match!");
			forwardForError(request, response, errors);
			return;
		}
		
		try {
			int access = Integer.parseInt(request.getParameter("accesslevel"));
			AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
			
			Map<String, Object> properties = 
					Library.propertySetBuilder("password",password
											  ,"accesslevel",accessLevel
											  );
			
			WrapperObject newUser = WrapperObjectFactory.getPerson();
			List<String> errors = newUser.addObject(email, properties);
			
			if (errors.isEmpty()){
				Auth.setSessionVariable(request, "user", newUser);
				Auth.removeSessionVariable(request, "isAddAdmin");
				response.sendRedirect(request.getContextPath() + "/editprofile.jsp");	
			}else{ 
				forwardForError(request, response, errors);
			}
		}
		catch (NumberFormatException e) {
			request.setAttribute("addNewUser", false);
			request.getRequestDispatcher("addadmin.jsp").forward(request, response);
		}
		
			
	}

	private void forwardForError(HttpServletRequest request,
			HttpServletResponse response, List<String> errors)
			throws ServletException, IOException {
		
		request.setAttribute("addNewUser", false);
		request.setAttribute("errors", errors);
		request.getRequestDispatcher("addadmin.jsp").forward(request, response);
	}
}
