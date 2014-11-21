package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public interface WrapperObject extends Serializable{
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
	
	abstract Object getProperty(String propertyKey);
	
	abstract List<String> addObject(String id, Map<String,Object> properties);
	
	abstract List<String> editObject(String id, Map<String,Object> properties);

	abstract boolean removeObject(String id);
	
	abstract WrapperObject findObject(String filter);
	
	abstract List<WrapperObject> getAllObjects();
}