package edu.uwm.owyh.model;

import java.util.List;

import edu.uwm.owyh.model.User.*;
import edu.uwm.owyh.model.DataStore;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	private String _goodPassword;
	private static final String TABLE = "users"; 
	
	private Auth(HttpServletRequest request){
		//TODO constructor for use in grabbing session variables rather than 
		//querying the datastore. 
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
		Filter filter = new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, userName.toUpperCase());
		List<Entity> users = store.findEntities(TABLE, filter);
		if(users.isEmpty()) return null;
		Entity user = users.get(0);
		
		if(!user.getProperty("password").equals(password)) return null;
		
		
		return user;
	}
	
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
}
