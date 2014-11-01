package edu.uwm.owyh.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

import edu.uwm.owyh.model.User.AccessLevel;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	private String _goodPassword;
	private static final String TABLE = "users"; 
	
	private Auth(HttpServletRequest request){
		
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
	
	public boolean verifyLogin(String userName, String password){
		if(userName == null || password == null)return false;
		
		DataStore store = DataStore.getDataStore();
		Filter filter = new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, userName.toUpperCase());
		List<Entity> users = store.findEntities(TABLE, filter);
		if(users.isEmpty()) return false;
		Entity user = users.get(0);
		
		if(!user.getProperty("password").equals(password)) return false;
		
		
		return true;
	}
	
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
	
	
	public boolean verifyUserIsLoggedIn(HttpServletRequest request)
	{
		HttpSession s = request.getSession();
		
		if (s.getAttribute("username") == null)
		{
			return false;
		}
		
		else
			return true;
	}
}
