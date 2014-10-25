package edu.uwm.owyh.model;

import edu.uwm.owyh.model.User.*;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	private String _goodPassword;
	
	private AccessLevel getAccessLevel(){
		return _goodAccess;
	}
	
	private String getUserName(){
		return _goodUserName;
	}
	
	private String getPassword(){
		return _goodPassword;
	}	
	
	public boolean verifyUser(String userName, String password){
		return userName.equalsIgnoreCase(getUserName()) && password.equals(getPassword());
	}
	
	public static Auth getAuth(){
		return new Auth();
	}
}
