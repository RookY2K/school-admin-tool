package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.User.AccessLevel;

@SuppressWarnings("serial")
public class Admin extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		auth.verifyUser(response);
		auth.verifyAdmin(response);
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("username")!= null){
			if(session.getAttribute("accesslevel") == AccessLevel.ADMIN){
				response.sendRedirect("/admin/admin.jsp");
			}else{
				response.sendRedirect("home.jsp");
			}
		}else{
			request.getRequestDispatcher("/").forward(request, response);
		}
	}

}
