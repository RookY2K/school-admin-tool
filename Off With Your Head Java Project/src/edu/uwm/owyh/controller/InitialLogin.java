package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;

@SuppressWarnings("serial")
public class InitialLogin extends HttpServlet{
			
		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			Boolean noUsers = (Boolean)request.getAttribute("noUsers");
			
			if(noUsers != null && noUsers.booleanValue()){
												
				Auth.setSessionVariable(request, "noUsers", noUsers);
				request.getRequestDispatcher(request.getContextPath() + "/initiallogin.jsp").forward(request, response);
				return;
			}else{
				request.getRequestDispatcher("/").forward(request, response);
				return;
			}
		}
		
		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			DataStore store = DataStore.getDataStore();
			
			/* DO NOT DELETE */
			
//			SoftwareKey appkey = new SoftwareKey();
//			appkey.setUnlock("63D07BtB09");
//			store.insertEntity(appkey, appkey.getId());
           
			 

			String userEnteredKey = request.getParameter("appkey");
			String filter = "unlock== '" + userEnteredKey + "'";
			boolean isKey = !(store.findEntities(SoftwareKey.class, filter, null).isEmpty()); 
			
			if(!isKey){
				request.setAttribute("isKey", isKey);
				request.getRequestDispatcher(request.getContextPath() + "/initiallogin.jsp").forward(request, response);
				return;
			}else{
				Auth.setSessionVariable(request, "isAddAdmin", true);
				response.sendRedirect("/admin/addAdmin");
				return;
			}
		}
		
		@PersistenceCapable
		private static class SoftwareKey{
			
			@PrimaryKey
			@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
			private Key id;
			@Persistent
			private String unlock;
			
			@SuppressWarnings("unused")
			private void setUnlock(String key){
				unlock = key;
			}
			
			@SuppressWarnings("unused")
			private Key getId(){
				return id;
			}
		}
}
