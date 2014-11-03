package edu.uwm.owyh.model;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public interface Person {
	public static final Key USERKEY = KeyFactory.createKey("rootKey", "root");
	public static final String EMAILPATTERN = "^\\w+@uwm.edu$";
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

	/**
	 * Accessor for user name
	 * @return _userName
	 */
	public abstract String getUserName();

	public abstract String getToUpperUserName();

	/**
	 * Accessor for password
	 * @return _password
	 */
	public abstract String getPassword();

	/**
	 * Accessor method for Access level
	 * @return _accessLevel
	 */
	public abstract AccessLevel getAccessLevel();

	/**
	 * Accessor method for name
	 * @return _name
	 */
	public abstract String getName();

	/**
	 * Accessor method for email
	 * @return _email
	 */
	public abstract String getEmail();

	/**
	 * Accessor method for phone
	 * @return _phone
	 */
	public abstract String getPhone();

	/**
	 * Accessor method for address
	 * @return _address
	 */
	public abstract String getAddress();

	/**
	 * Mutator for Password
	 * @param password
	 */
	public abstract void setPassword(String password);

	/**
	 * Mutator for Access Level
	 * @param accessLevel
	 */
	public abstract void setAccessLevel(AccessLevel accessLevel);

	/**
	 * Mutator for name
	 * @param name
	 */
	public abstract void setName(String name);

	/**
	 * Mutator for Email
	 * @param email
	 */
	public abstract void setEmail(String email);

	/**
	 * Mutator for phone
	 * @param phone
	 */
	public abstract void setPhone(String phone);

	/**
	 * Mutator for address
	 * @param address
	 */
	public abstract void setAddress(String address);

	//public abstract boolean savePerson();
	
	public abstract boolean addPerson();
	
	public abstract boolean editPerson();

	public abstract void removePerson();
	
	public abstract Person findPerson(String identifier);
	
	public abstract List<Person> getAllPersons();
}