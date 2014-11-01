package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.User;
@SuppressWarnings("serial")
public class Index extends HttpServlet {
	
	Auth auth;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		DataStore store = DataStore.getDataStore();
		int userCount = store.findEntities(User.getUserTable(), null).size();	
		if(userCount == 0){
			request.getRequestDispatcher("/initiallogin").forward(request, response);
			return;
		}
		
		auth = Auth.getAuth(request);
		boolean isLogin = auth.verifyUser();
		boolean isAdmin = auth.verifyAdmin();
		
		if (isLogin) {
			if (isAdmin){ 
				response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");
				return;
			}
			else{
				response.sendRedirect(request.getContextPath() + "/home.jsp");
				return;
			}
		}
		else {
			response.sendRedirect("index.jsp");
			return;
		}		
	}
}
