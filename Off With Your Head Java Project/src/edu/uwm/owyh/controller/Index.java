package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.User;
import edu.uwm.owyh.model.User.AccessLevel;

@SuppressWarnings("serial")
public class Index extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("username")!= null){
			if(session.getAttribute("accesslevel") == AccessLevel.ADMIN){
				response.sendRedirect("/admin/admin.jsp");
				return;
			}else{
				response.sendRedirect("home.jsp");
				return;
			}
		}
		
		DataStore store = DataStore.getDataStore();
		int userCount = store.findEntities(User.getUserTable(), null).size();
		
		if(userCount == 0){
			request.getRequestDispatcher("/initiallogin").forward(request, response);
			return;
		}
		// TODO: actually check if user is login and if user is admin
		boolean isLogin = false;
		
		if(request.getAttribute("login") != null){ 
			isLogin = (boolean)request.getAttribute("isLogin");
		}
		
		if (isLogin) {
			boolean isAdmin = (boolean)request.getAttribute("isAdmin");
			if (isAdmin) {
				response.sendRedirect("/admin/admin.jsp");
				return;
			}	
			else{
				response.sendRedirect("home.jsp");
				return;
			}
		}
		else {
			response.sendRedirect("index.jsp");	
		}
				
		
	}
}
