package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

import edu.uwm.owyh.model.DataStore;

@SuppressWarnings("serial")
public class InitialLogin extends HttpServlet{
			
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			response.sendRedirect(request.getContextPath() + "/initiallogin.jsp");
			return;
		}
		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			DataStore store = DataStore.getDataStore();
			/* DO NOT DELETE
			Entity softwareKey = new Entity("softwarekey");
			softwareKey.setProperty("keyValue", "63D07BtB09");
			store.insertEntity(softwareKey);*/
			boolean isKey = false;
			String userEnteredKey = request.getParameter("appkey");
			//TODO Add ability to query on software key added by user
			//TODO Redirect to correct controller
			Filter filter = new Query.FilterPredicate("keyValue", Query.FilterOperator.EQUAL, userEnteredKey);
			isKey = !(store.findEntities("softwarekey",filter).isEmpty());
			
			if(!isKey){
				request.setAttribute("isKey", isKey);
				request.getRequestDispatcher(request.getContextPath() + "/initiallogin.jsp").forward(request, response);;
				return;
			}else{
				//request.setAttribute("isAddAdmin", true);
				HttpSession session = request.getSession();
				session.setAttribute("isAddAdmin", true);
				response.sendRedirect("/admin/addAdmin");
				//request.getRequestDispatcher(request.getContextPath() + "/admin/addAdmin").forward(request, response);
				return;
			}
		}
}
