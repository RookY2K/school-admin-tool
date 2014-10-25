package edu.uwm.owyh.model;

public class User {
	public enum AccessLevel {TA, INSTRUCTOR, ADMIN};
	private String _userName;
	private String _password;
	AccessLevel _accessLevel;
	
	private User(String userName, String pwd, AccessLevel access){
		_userName = userName;
		_password = pwd;
		_accessLevel = access;
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
	
	public User getUser(String userName, String pwd, AccessLevel access){
		return new User(userName, pwd, access);
	}
}
