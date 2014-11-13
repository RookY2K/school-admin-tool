package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.WrapperObject;
import edu.uwm.owyh.model.WrapperObjectFactory;

@SuppressWarnings("serial")
public class UserList extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyUser(response)) return;
		
		/* Any Login User View User List */
		WrapperObject user = (WrapperObject)Auth.getSessionVariable(request, "user");
		List<WrapperObject> clients = user.getAllObjects();
		
		request.setAttribute("users", clients);
		request.getRequestDispatcher(request.getContextPath() + "userlist.jsp").forward(request, response);	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if (! auth.verifyAdmin(response)) return;
		
		/* Admin delete a User */
		String username = (String) request.getParameter("username");
		if (username != null) {
			WrapperObject user = WrapperObjectFactory.getPerson().findObject(username);
			if (user != null) {
				if (WrapperObjectFactory.getPerson().removeObject(user.getUserName())) {
					response.sendRedirect(request.getContextPath() + "/userlist?deleted");	
					return;
				}

			}
		}

		response.sendRedirect(request.getContextPath() + "/userlist");	
	}
}