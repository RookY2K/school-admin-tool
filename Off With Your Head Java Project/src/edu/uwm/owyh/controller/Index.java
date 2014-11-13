package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;
@SuppressWarnings("serial")
public class Index extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Person client = UserFactory.getUser();
		
		int userCount = client.getAllPersons().size();
		
//		DataStore store = DataStore.getDataStore();
//		int userCount = store.findEntities(Client.getClientTable(), null).size();	
		if(userCount == 0){
			request.setAttribute("noUsers", true);
			request.getRequestDispatcher("/initiallogin").forward(request, response);
			return;
		}
		
		Auth auth = Auth.getAuth(request);
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
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request,response);
	}
}
