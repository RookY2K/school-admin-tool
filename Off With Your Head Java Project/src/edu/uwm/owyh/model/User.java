package edu.uwm.owyh.model;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;

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
	
	private User(String userName, String pwd, AccessLevel access){
		_userName = userName;
		_password = pwd;
		_accessLevel = access;
	}
	
	private User(Entity user) {
		_userName = (String) user.getProperty("username");
		_password = (String) user.getProperty("password");
		int getAccess = (int) user.getProperty("accesslevel");
		_accessLevel = convertAccessLevel(getAccess);
		_userEntity = user;
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
	
	public static User getUser(String userName, String pwd, AccessLevel access){
		return new User(userName, pwd, access);
	}
	
	public static User getUser(Entity user) {
		return new User(user);
	}
	
	public static List<User> getUserFromList(List<Entity> entities) {
		List<User> users = new ArrayList<User>();
		for (Entity item : entities)
			users.add(User.getUser(item));
		return users;
	}
	
	public void saveUser() {
		if (_userEntity == null)
			_userEntity = createUserEntity();
		DataStore store = DataStore.getDataStore();
		store.insertEntity(_userEntity);
	}
	
	public void removeUser() {
		if (_userEntity == null)
			return;
		DataStore store = DataStore.getDataStore();
		store.deleteEntity(_userEntity);
	}
	
	private Entity createUserEntity() {
		Entity user = new Entity("users");
		user.setProperty("username", _userName);
		user.setProperty("password", _password);
		user.setProperty("accesslevel", _accessLevel.ordinal());
		return user;
	}
	
	private static AccessLevel convertAccessLevel(int access) {
		// Entity cannot save Enumerators, must Convert
		switch (access) {
			case 0: return AccessLevel.TA;
			case 1: return AccessLevel.INSTRUCTOR;
			case 2: return AccessLevel.ADMIN;
			default: return null;
		}
	}
}
