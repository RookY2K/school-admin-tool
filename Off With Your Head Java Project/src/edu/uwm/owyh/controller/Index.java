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
		/*HttpSession session = request.getSession();
		
		if(session.getAttribute("username")!= null){
			if(session.getAttribute("accesslevel") == AccessLevel.ADMIN){
				request.getRequestDispatcher("/admin/admin.jsp").forward(request,response);
			}
		}*/
		
		DataStore store = DataStore.getDataStore();
		int userCount = store.findEntities(User.getUserTable(), null).size();
		
		if(userCount == 0) response.sendRedirect("/initiallogin");
		// TODO: actually check if user is login and if user is admin
		boolean isLogin = false;
		
		if(request.getParameter("login") != null){ 
			isLogin = request.getParameter("login").equalsIgnoreCase("true");
		}
		
		if (isLogin) {
			boolean isAdmin = request.getParameter("admin").equalsIgnoreCase("true");
			if (isAdmin) 
				request.getRequestDispatcher("/admin/admin.jsp").forward(request, response);	
			else 
				request.getRequestDispatcher("home.jsp").forward(request, response);

		}
		else {
			request.getRequestDispatcher("index.jsp").forward(request, response);	
		}
				
		
	}
}
