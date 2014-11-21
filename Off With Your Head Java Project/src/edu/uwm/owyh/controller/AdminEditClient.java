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
public class AdminEditClient extends HttpServlet {
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
	    
	    Map<String, Object> properties = 
	    		Library.propertySetBuilder("password", request.getParameter("password")
	    				                  ,"accesslevel", accessLevel);
	    WrapperObject user = WrapperObjectFactory.getPerson();
	    List<String> errors = user.editObject(request.getParameter("username"), properties);
	    
		if (errors.isEmpty())
		{
			response.sendRedirect("/userlist");
		}else{
			request.setAttribute("errors", errors);
			//TODO forward to correct jsp page
		}		
	}
}