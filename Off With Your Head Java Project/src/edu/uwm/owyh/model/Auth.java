package edu.uwm.owyh.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

import edu.uwm.owyh.model.User.AccessLevel;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	private String _goodPassword; 
	
	private Auth(HttpServletRequest request){
		if (getSessionVariable(request, "username") == null) return;
		_goodUserName = (String) getSessionVariable(request, "username");
		_goodAccess = (AccessLevel) getSessionVariable(request, "accesslevel");
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
		List<Entity> users = store.findEntities(User.getUserTable(), filter);
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
	
	public void verifyUser(HttpServletResponse response) throws IOException {
		if (_goodUserName == null) response.sendRedirect("/");
	}
	
	public void verifyAdmin(HttpServletResponse response) throws IOException {
		if (_goodUserName == null || _goodAccess != AccessLevel.ADMIN) response.sendRedirect("/");
	}
	
	public static void destroySession(HttpServletRequest request) {
		removeSessionVariable(request, "username");
		removeSessionVariable(request, "accesslevel");
	}
	
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
	
	public boolean verifyUserIsLoggedIn(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
			
		if (session.getAttribute("username") == null)
		{
			return false;
		}
		
		else
			return true;
	}
	
	public void redirectAccessLevel(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		verifyAdmin(response);
	}
	
	public void redirectInvalidLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		verifyUser(response);
	}
}
