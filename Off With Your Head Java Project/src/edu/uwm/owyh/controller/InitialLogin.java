package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;

@SuppressWarnings("serial")
public class InitialLogin extends HttpServlet{
			
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			Boolean noUsers = (Boolean)request.getAttribute("noUsers");
			
			if(noUsers != null && noUsers.booleanValue()){
												
				Auth.setSessionVariable(request, "noUsers", noUsers);
				response.sendRedirect(request.getContextPath() + "/initiallogin.jsp");
				return;
			}else{
				request.getRequestDispatcher("/").forward(request, response);
				return;
			}
		}
		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			DataStore store = DataStore.getDataStore();
			
			/* DO NOT DELETE
			Key software = KeyFactory.createKey("softwarekey", "appkey");
			Entity softwareKey = new Entity(software);
			softwareKey.setProperty("keyValue", "63D07BtB09");
			store.insertEntity(softwareKey);*/

			String userEnteredKey = request.getParameter("appkey");
			Filter filter = new Query.FilterPredicate("keyValue", Query.FilterOperator.EQUAL, userEnteredKey);
			boolean isKey = !(store.findEntities("softwarekey",filter, null).isEmpty());
			
			if(!isKey){
				request.setAttribute("isKey", isKey);
				request.getRequestDispatcher(request.getContextPath() + "/initiallogin.jsp").forward(request, response);;
				return;
			}else{
				Auth.setSessionVariable(request, "isAddAdmin", true);
				response.sendRedirect("/admin/addAdmin");
				return;
			}
		}
}
