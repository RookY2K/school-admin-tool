package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

import edu.uwm.owyh.model.DataStore;

@SuppressWarnings("serial")
public class InitialLogin extends HttpServlet{
			
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			request.getRequestDispatcher("initiallogin.jsp").forward(request, response);			
		}
		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			DataStore store = DataStore.getDataStore();
			/* DO NOT DELETE
			Entity softwareKey = new Entity("softwarekey");
			softwareKey.setProperty("keyValue", "63D07BtB09");
			store.insertEntity(softwareKey);*/
			
			//TODO Add ability to query on software key added by user
			//TODO Redirect to correct controller
			Filter filter = new Query.FilterPredicate("keyValue", Query.FilterOperator.EQUAL, "63D07BtB09");
			if(store.findEntities("softwarekey", filter).isEmpty())
				request.getRequestDispatcher("initiallogin.jsp").forward(request, response);
			else
				response.sendRedirect("/admin/addAdmin");
		}
}
