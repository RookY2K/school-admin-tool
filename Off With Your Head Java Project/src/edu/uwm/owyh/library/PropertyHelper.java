package edu.uwm.owyh.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;

public final class PropertyHelper {

	private PropertyHelper() {
		//prevent instantiation
	}

	/**
	 * <pre>Utility method to build a Property Map for editing/adding JDO .Expects key-value pairs. 
	 * Will throw Exception if there is an odd number of arguments,the supposed key is null, or
	 * key is not a string.
	 * Otherwise, there is no check to ensure keys actually exist for any JDO or that the value
	 * is of the correct type for the property. Expectation is that the JDO wrapper classes will
	 * perform these specific checks.</pre>
	 * 
	 *   <p><pre>      E.G. key: name   value: Vince
	 *           key: age    value: 33
	 *           key: gender value: male
	 *        
	 *           Library.propertyMapBuilder("name", "Vince"
	 *                                     ,"age", 33
	 *                                     ,"gender","male");</pre></p>
	 *                                  
	 * @param properties <pre>Iterable array of JDO property Key-value pairs (String, Object). Expectation
	 * is that the Key and value are next to each other and that the key will precede the value.</pre>
	 * @return a map with the inputted properties.
	 */
	public static Map<String, Object> propertyMapBuilder(Object...properties){
		if(properties.length % 2 != 0){
			throw new IllegalArgumentException("Odd number of arguments!");
		}
		Map<String, Object> result = new HashMap<String, Object>(); 
		String key = null;
		int pointer = 0;
		
		for(Object val: properties){
			switch(pointer % 2){
			case 0: 
				if(!(val instanceof String)){
					throw new IllegalArgumentException("Key values are null or not string objects!");
				}
				key = (String)val;
				break;
			case 1:
				result.put(key, val);
				break;
			}
			pointer++;
		}
		
		return result;
	}

	/**
	 * Utility Method to build a person's Property map. 
	 * @param user - WrapperObject<Person> object
	 * @return A map of properties.
	 */
	public static Map<String, Object> makeUserProperties(WrapperObject<Person> user) { 
		if (user == null) return null;
		Map<String, Object> properties =
				propertyMapBuilder("firstname",user.getProperty("firstname")
	                                       ,"lastname",user.getProperty("lastname")
	                                       ,"email",user.getProperty("email")
	                                       ,"phone",user.getProperty("phone")
	                                       ,"streetaddress",user.getProperty("streetaddress")
	                                       ,"city",user.getProperty("city")
	                                       ,"state",user.getProperty("state")
	                                       ,"zip",user.getProperty("zip")
	                                       ,"password",user.getProperty("password")
	                                       ,"accesslevel",user.getProperty("accesslevel")
	                                       ,"officeroom", user.getProperty("officeroom")
	                                       ,"skills", user.getProperty("skills")
	                                       ,"sections", user.getProperty("sections")
	                                       );
		for(String key : properties.keySet())
			if(properties.get(key) == null) properties.put(key, "");
		return properties;
	}

	/**
	 * Utility Method to build a person's Property map. 
	 * @param user - WrapperObject<OfficeHours> object
	 * @return A map of properties.
	 */
	public static List<Map<String, Object>> makeOfficeHoursProperties(List<WrapperObject<OfficeHours>> hours) {
		if (hours == null) return null;
		List<Map<String, Object>> officeHours = new ArrayList<Map<String, Object>>();
		for (WrapperObject<OfficeHours> hour : hours) {
			Map<String, Object> properties =
					propertyMapBuilder("starttime",hour.getProperty("starttime")
	                                           ,"endtime",hour.getProperty("endtime")
	                                           ,"days",hour.getProperty("days")
	                                           );
			for(String key : properties.keySet())
				if(properties.get(key) == null) properties.put(key, "");
			officeHours.add(properties);
		}
		return officeHours;
	}
	
	

}
