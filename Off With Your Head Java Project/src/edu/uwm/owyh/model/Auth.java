package edu.uwm.owyh.model;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

import edu.uwm.owyh.model.Person.AccessLevel;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	private String _goodPassword; 
	
	private Auth(HttpServletRequest request){
		Person user = (Person)getSessionVariable(request, "user");
		if (user == null) return;
		_goodUserName = user.getUserName();
		_goodAccess = (AccessLevel) user.getProperty("accesslevel");
	}
	
	private Auth(){
		_goodAccess = null;
		_goodUserName = null;
		_goodPassword = null;
	}	
	
	public static Object getSessionVariable(HttpServletRequest request, String key){
		if(request == null || key == null) return null;
		
		HttpSession session = request.getSession();
		
		return session.getAttribute(key);
	}
	
	public static void setSessionVariable(HttpServletRequest request, String key, Object attribute){
		if(request == null || key == null) return;
		
		HttpSession session = request.getSession();
		
		session.setAttribute(key, attribute);
	}
	
	public static void removeSessionVariable(HttpServletRequest request, String key){
		if(request == null || key == null) return;
		
		HttpSession session = request.getSession();
		
		session.removeAttribute(key);
	}
	
	public Entity verifyLogin(String userName, String password){
		if(userName == null || password == null)return null;
		
		DataStore store = DataStore.getDataStore();
		Filter filter = new Query.FilterPredicate("toupperusername", Query.FilterOperator.EQUAL, userName.toUpperCase());
		List<Entity> users = store.findEntities(Client.getClientTable(), filter, Person.USERKEY);
		if(users.isEmpty()) return null;
		Entity user = users.get(0);
		
		if(!user.getProperty("password").equals(password)) return null;
		
		
		return user;
	}
	
	public boolean verifyUser() {
		return (_goodUserName != null);
	}
	
	public boolean verifyAdmin() {
		if ((_goodUserName == null)) return false;
		return (_goodAccess == AccessLevel.ADMIN);
	}
	
	public boolean verifyUser(HttpServletResponse response) throws IOException {
		if (_goodUserName == null) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
	
	public boolean verifyAdmin(HttpServletResponse response) throws IOException {
		if (_goodUserName == null || _goodAccess != AccessLevel.ADMIN) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static void destroySession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Enumeration<String> attributeNames = session.getAttributeNames();
		
		while(attributeNames.hasMoreElements()){
			removeSessionVariable(request, attributeNames.nextElement());
		}
	}
	
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
}
