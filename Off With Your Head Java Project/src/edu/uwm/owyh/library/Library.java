package edu.uwm.owyh.library;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.jdo.Person;

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
	
	public static Key generateIdFromUserName(String userName){
		return Person.generateIdFromUserName(userName);
	}
	
	public static double parseTimeToDouble(String time){
		double hours = parseHours(time);
		double minutes = parseMinutes(time)/60;
		String AmPm = parseAmPm(time);

		if(AmPm.equalsIgnoreCase("pm")){
			if(hours != 12) hours += 12;
		}else{
			if(hours == 12) hours += 12;
		}

		return hours + minutes;
	}
	
	private static String parseAmPm(String time) {
		int startIndex = time.length() - 2;
		String AmPm = time.substring(startIndex);

		if(!(AmPm.equalsIgnoreCase("am") || AmPm.equalsIgnoreCase("pm"))){
			throw new IllegalArgumentException("Am or Pm was not appended to the time!");
		}

		return AmPm;
	}

	private static double parseMinutes(String time) { 
		int startIndex = time.indexOf(':') + 1;
		int endIndex = startIndex + 2;

		if(startIndex == -1)
			throw new IllegalArgumentException("Missing ':' separator between hours and minutes!");

		String minutes = time.substring(startIndex, endIndex);

		double numMinutes = -1;

		try{
			numMinutes = Double.parseDouble(minutes);
		}catch(NumberFormatException nfe){
			throw new IllegalArgumentException("Minutes portion of the time was not a number");
		}

		if(numMinutes >= 60 || numMinutes < 0){
			throw new IllegalArgumentException("Minutes should be between 0 and 59!");
		}

		return numMinutes;
	}

	private static double parseHours(String time) {
		int startIndex = 0;
		int endIndex = time.indexOf(':');

		if(endIndex == -1)
			throw new IllegalArgumentException("Missing ':' separator between hours and minutes!");

		String hours = time.substring(startIndex, endIndex);

		double numHours = -1;

		try{
			numHours = Double.parseDouble(hours);
		}catch(NumberFormatException nfe){
			throw new IllegalArgumentException("Hours portion of the time was not a number!");
		}

		if(numHours > 12 || numHours <= 0)
			throw new IllegalArgumentException("Hours should be between 1 and 12!");

		return numHours;		
	}

}
