package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.User;

@SuppressWarnings("serial")
public class UserList extends HttpServlet {
	
	Auth auth;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: admin authentication
		
		DataStore store = DataStore.getDataStore();
		List<User> users = User.getUserFromList(store.findEntities(User.getUserTable(), null));

		String[] username = new String[users.size()];
		int[] accesslevel = new int[users.size()];
		
		for (int i = 0; i < users.size(); i++) {
			username[i] = users.get(i).getUserName();
			accesslevel[i] = users.get(i).getAccessLevel().getVal();
			
		}
		
		request.setAttribute("username", username);
		request.setAttribute("accesslevel", accesslevel);
		
		request.getRequestDispatcher("users.jsp").forward(request, response);	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// TODO: admin authentication
		
		@SuppressWarnings("unchecked")
		Map<String, Object> item = request.getParameterMap();
		
		if (item.keySet().size() > 0) {
			for (String key : item.keySet()) {
				User user = User.findUser(key);
				if (user != null)
					user.removeUser();
			}
		}
		
		response.setContentType("text/html");
		response.getWriter().write("<meta http-equiv=\"refresh\" content=\"6; url=/userlist\">");
		response.getWriter().write("Writing to Database, You be will automaticlly rediected in 6 seconds...");
		
		//response.sendRedirect("/userlist");	
	}
}