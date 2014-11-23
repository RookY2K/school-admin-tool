package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public interface WrapperObject<E> extends Serializable{
//	public static final Key USERKEY = KeyFactory.createKey("rootKey", "root");
	public static final String EMAILPATTERN = "^\\w+@uwm.edu$";
	public static final String PHONEPATTERN = "^((\\(\\d{3}\\))|(\\d{3}))[-\\.\\s]{0,1}\\d{3}[-\\.\\s]{0,1}\\d{4}$";
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
	abstract Key getId();
	
	abstract Class<E> getTable();
	
	abstract Object getProperty(String propertyKey);
	
	/**
	 * Adds the object to the datastore if it does not already exist. Returns any
	 * Errors that occur. If errors do occur, then nothing is added to the datastore.
	 * @param id - Equal to objects identifying property, if it has one, else equal to it's parents identifying property
	 * @param properties - Map of properties to be set. 
	 * @return List of errors if there were any. A non-empty list means the object was not added to the datastore.
	 */
	abstract List<String> addObject(String id, Map<String,Object> properties);
	
	abstract List<String> editObject(String id, Map<String,Object> properties);

	abstract boolean removeObject(String id);
	
	abstract List<WrapperObject<E>> findObject(String filter);
	
	abstract WrapperObject<E> findObjectById(Key id);

	abstract List<WrapperObject<E>> getAllObjects();

	abstract List<String> addChildObject(Object childJDO);	
	
	abstract boolean removeChildObject(Object childJDO);
}