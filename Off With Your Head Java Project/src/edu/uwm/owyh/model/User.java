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
	private String _name;
	private String _phone;
	private String _address;
	private String _email;
	
	private AccessLevel _accessLevel;
	private Entity _userEntity;
	private static final String TABLE = "users";
	
	private User(String userName, String pwd, AccessLevel access){
		_userName = userName;
		_password = pwd;
		_accessLevel = access;
		_userEntity = createUserEntity();
		_email = userName;
		_name = "";
		_phone = "";
		_address = "";
	}
	
	private User(Entity user) {
		_userName = (String) user.getProperty("username");
		_password = (String) user.getProperty("password");
		Long accessLong = (Long) user.getProperty("accesslevel");
		int getAccess = accessLong.intValue();
		_accessLevel = AccessLevel.getAccessLevel(getAccess);
		_email = (String) user.getProperty("email");
		_name = (String) user.getProperty("name");
		_phone = (String) user.getProperty("phone");
		_address = (String) user.getProperty("address");
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
	
	public String getToUpperUserName(){
		if(_userName == null) return null;
		else
			return _userName.toUpperCase();
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
	 * Accessor method for name
	 * @return _name
	 */
	public String getName(){
		return _name;
	}
	
	/**
	 * Accessor method for email
	 * @return _email
	 */
	public String getEmail(){
		return _email;
	}
	
	/**
	 * Accessor method for phone
	 * @return _phone
	 */
	public String getPhone(){
		return _phone;
	}
	
	/**
	 * Accessor method for address
	 * @return _address
	 */
	public String getAddress(){
		return _address;
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
	
	/**
	 * Mutator for name
	 * @param name
	 */
	public void setName(String name){
		_name = name;
		_userEntity.setProperty("name", name);
	}
	
	/**
	 * Mutator for Email
	 * @param email
	 */
	public void setEmail(String email){
		_email = email;
		_userEntity.setProperty("email", email);
	}
	
	/**
	 * Mutator for phone
	 * @param phone
	 */
	public void setPhone(String phone){
		_phone = phone;
		_userEntity.setProperty("phone", phone);
	}
	
	/**
	 * Mutator for address
	 * @param address
	 */
	public void setAddress(String address){
		_address = address;
		_userEntity.setProperty("address", address);
	}
	
	public static User getUser(String userName, String pwd, AccessLevel access){
		return new User(userName, pwd, access);
	}
	
	public static User getUser(Entity user) {
		return new User(user);
	}
	
	public static User findUser(String username) {
		DataStore store = DataStore.getDataStore();
		Filter filterUsername = new FilterPredicate("toupperusername", FilterOperator.EQUAL, username.toUpperCase());
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

		Filter filterUsername = new FilterPredicate("toupperusername", FilterOperator.EQUAL, getToUpperUserName());
		List<Entity> users = store.findEntities(TABLE, filterUsername);
		
		if (users.size() > 0)
		{
		    if(!users.get(0).getKey().equals(_userEntity.getKey())){
		    	return false;
		    }else{
		    	store.updateEntity(_userEntity);
		    }
		        
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
		user.setProperty("name", _name);
		user.setProperty("email", _email);
		user.setProperty("phone", _phone);
		user.setProperty("address", _address);
		return user;
	}

}
