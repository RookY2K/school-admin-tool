package edu.uwm.owyh.model;

import java.io.IOException;
import java.util.List;

import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.User.AccessLevel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	private String _goodPassword; 
	
	private Auth(HttpServletRequest request){
		HttpSession session = request.getSession();
		if (session.getAttribute("username") == null) return;
		_goodUserName = (String) session.getAttribute("username");
		_goodAccess = (AccessLevel) session.getAttribute("accesslevel");
	}
	
	private Auth(){
		_goodAccess = null;
		_goodUserName = null;
		_goodPassword = null;
	}
	
	private void setUserName(String userName){
		_goodUserName = userName;
	}
	
	private void setPassword(String password){
		_goodPassword = password;
	}
	
	private AccessLevel getAccessLevel(){
		return _goodAccess;
	}
	
	private String getUserName(){
		return _goodUserName;
	}
	
	private String getPassword(){
		return _goodPassword;
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
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		session.removeAttribute("acesslevel");
	}
	
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
}
