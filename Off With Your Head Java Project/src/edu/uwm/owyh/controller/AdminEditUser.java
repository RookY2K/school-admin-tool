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
public class AdminEditUser extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.sendRedirect("/userlist");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		int access = Integer.parseInt(request.getParameter("accesslevel"));
		AccessLevel accessLevel = AccessLevel.getAccessLevel(access);
	    WrapperObject user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = 
				Library.propertySetBuilder("accesslevel",accessLevel
						                  ,"firstname", request.getParameter("firstname")
						                  ,"lastname",request.getParameter("lastname")
						                  ,"phone", request.getParameter("phone")
						                  ,"streetaddress", request.getParameter("streetaddress")
						                  ,"city",request.getParameter("city")
						                  ,"state",request.getParameter("state")
						                  ,"zip",request.getParameter("zip")
						                  );
		
		List<String> errors = user.editObject(request.getParameter("username"), properties);
		if (errors.isEmpty())
		{
			request.setAttribute("user", user);
			request.setAttribute("isEdited", true);
			WrapperObject client = (WrapperObject)Auth.getSessionVariable(request, "user");
			String userName = (String)user.getProperty("userName");
			String clientName = (String)client.getProperty("userName");
			if(userName.equalsIgnoreCase(clientName)){
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