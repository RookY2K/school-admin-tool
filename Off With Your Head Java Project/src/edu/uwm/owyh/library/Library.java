package edu.uwm.owyh.library;

import java.util.HashMap;
import java.util.Map;

public class Library {
	public static Map<String, Object> propertySetBuilder(Object...properties){
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
	
	public static String[] getStates(){
		String stateString = "AL,AK,AZ,AR,CA,CO,CT,DE,FL,GA,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,"
				+ "MO,MS,MT,NE,NV,NH,NJ,NM,NY,NC,ND,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VT,VA,WA,WV,WI,WY";
		
		String delim = "[,]";
		
		String[] states = stateString.split(delim);
		
		return states;
	}
}
