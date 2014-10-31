package edu.uwm.owyh.model;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class User {
	public enum AccessLevel {
		TA(3), INSTRUCTOR(2), ADMIN(1);
		private int value;
		private AccessLevel(int val){
			value = val;
		}
		public int getVal(){
			return value;
		}
		public static AccessLevel getAccessLevel(int val){
			switch(val){
			case 1:
				return AccessLevel.ADMIN;
			case 2:
				return AccessLevel.INSTRUCTOR;
			case 3:
				return AccessLevel.TA;
			default:
				return null;
			}
		}
	}	
	private String _userName;
	private String _password;
	AccessLevel _accessLevel;
	private Entity _userEntity;
	private static final String TABLE = "users";
	
	private User(String userName, String pwd, AccessLevel access){
		_userName = userName;
		_password = pwd;
		_accessLevel = access;
		_userEntity = createUserEntity();
	}
	
	private User(Entity user) {
		_userName = (String) user.getProperty("username");
		_password = (String) user.getProperty("password");
		Long accessLong = (Long) user.getProperty("accesslevel");
		int getAccess = accessLong.intValue();
		_accessLevel = AccessLevel.getAccessLevel(getAccess);
		_userEntity = user;
	}
	
	public static String getUserTable(){
		return TABLE;
	}
	
	/**
	 * Accessor for user name
	 * @return _userName
	 */
	public String getUserName(){
		return _userName;
	}
	
	/**
	 * Accessor for password
	 * @return _password
	 */
	public String getPassword(){
		return _password;
	}
	
	/**
	 * Accessor method for Access level
	 * @return _accessLevel
	 */
	public AccessLevel getAccessLevel(){
		return _accessLevel;
	}
	
	/**
	 * Mutator for username
	 * @param userName
	 */
	//Disallowing this for now. 
	//public void setUserName(String userName){ 
	//	_userName = userName;
	//  _userEntity.setProperty("username", userName);
	//}
	
	/**
	 * Mutator for Password
	 * @param password
	 */
	public void setPassword(String password){
		_password = password;
		_userEntity.setProperty("password", password);
	}
	
	/**
	 * Mutator for Access Level
	 * @param accessLevel
	 */
	public void setAccessLevel(AccessLevel accessLevel){
		_accessLevel = accessLevel;
		_userEntity.setProperty("accesslevel", accessLevel.getVal());
	}
	
	public static User getUser(String userName, String pwd, AccessLevel access){
		return new User(userName, pwd, access);
	}
	
	public static User getUser(Entity user) {
		return new User(user);
	}
	
	public static User findUser(String username) {
		DataStore store = DataStore.getDataStore();
		Filter filterUsername = new FilterPredicate("username", FilterOperator.EQUAL, username);
		List<User> users = User.getUserFromList(store.findEntities(User.getUserTable(), filterUsername));
		if (users.size() == 0) return null;
		return users.get(0);
	}
	
	public static List<User> getAllUser() {
		DataStore store = DataStore.getDataStore();
		List<User> users = User.getUserFromList(store.findEntities(User.getUserTable(), null));
		return users;
	}
	
	public static List<User> getUserFromList(List<Entity> entities) {
		List<User> users = new ArrayList<User>();
		for (Entity item : entities)
			users.add(User.getUser(item));
		return users;
	}
	
	public boolean saveUser() {
		DataStore store = DataStore.getDataStore();
		
		if (_userName.trim().equals("") || _password.equals("") || _accessLevel == null)
			return false;

		Filter filterUsername = new FilterPredicate("username", FilterOperator.EQUAL, _userName);
		List<Entity> users = store.findEntities(TABLE, filterUsername);
		
		if (users.size() > 0)
		{
		    if(users.get(0).getKey().equals(_userEntity.getKey()) == false)
		        return false;
		}
		
		store.insertEntity(_userEntity);
		return true;
	}
	
	public void removeUser() {
		DataStore store = DataStore.getDataStore();
		store.deleteEntity(_userEntity);
	}
	
	private Entity createUserEntity() {
		Entity user = new Entity(TABLE);
		user.setProperty("username", _userName);
		user.setProperty("toupperusername", _userName.toUpperCase());
		user.setProperty("password", _password);
		user.setProperty("accesslevel", _accessLevel.getVal());
		return user;
	}

}
