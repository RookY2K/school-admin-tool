package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.User;

@SuppressWarnings("serial")
public class InitialLogin extends HttpServlet{
			
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			//String url = "/initialLogin.jsp";
			
			//String path = request.getRequestURL().toString();
			//url = response.encodeRedirectURL(url);
			DataStore store = DataStore.getDataStore();
			int userCount = store.findEntities(User.getUserTable(), null).size();
			
			if(userCount > 0){
				request.getRequestDispatcher("/").forward(request, response);
				return;
			}
			
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
			List<Entity> keyEntities = store.findEntities("softwarekey",null);
			
			for(Entity keyEnt : keyEntities){
				if(keyEnt.getProperty("keyValue").equals(userEnteredKey)){
					isKey = true;
					break;
				}
			}
			if(!isKey){
				response.sendRedirect(request.getContextPath() + "/initiallogin.jsp?isKey=false");
				return;
			}else{
				request.getRequestDispatcher("/admin/addAdmin").forward(request, response);
				return;
			}
		}
}
