package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.library.CalendarHelper;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.CellObject;
@SuppressWarnings("serial")
public class Index extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		WrapperObject<Person> client = WrapperObjectFactory.getPerson();
		
		int userCount = client.getAllObjects().size();

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
				response.sendRedirect(request.getContextPath() + "/admin");
				return;
			}
			else{
				WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
				Key myId = WrapperObjectFactory.generateIdFromUserName((String) self.getProperty("username"));
				self = WrapperObjectFactory.getPerson().findObjectById(myId);
				
				CellObject[][] newArray = CalendarHelper.getCellObjectArray(self);
				
				request.setAttribute("array", newArray);
				request.setAttribute("self", PropertyHelper.makeUserProperties(self));
				request.getRequestDispatcher(request.getContextPath() + "/home.jsp").forward(request, response);	
				return;
			}
		}
		else {
			request.getRequestDispatcher(request.getContextPath() + "/index.jsp").forward(request, response);
			return;
		}		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request,response);
	}
}
