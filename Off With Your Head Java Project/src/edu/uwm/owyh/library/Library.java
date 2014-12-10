package edu.uwm.owyh.library;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdowrappers.OfficeHoursWrapper;
import edu.uwm.owyh.jdowrappers.SectionWrapper;
import edu.uwm.owyh.model.Auth;

/**
 * Library class with miscellaneous, general, utility methods that are called by several classes.
 * All methods in this class will be Static methods. There is no such thing as a Library object.
 * @author Vince Maiuri
 *
 */
public class Library {
	
	private Library(){
		//Prevents instantiation of the Library class
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
	 * Buids an array of the 50 state abbreviations. Useful for building state select lists.
	 * @return an array of the 50 state abbreviations
	 */
	public static String[] getStates(){
		String stateString = "AL,AK,AZ,AR,CA,CO,CT,DE,FL,GA,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,"
				+ "MO,MS,MT,NE,NV,NH,NJ,NM,NY,NC,ND,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VT,VA,WA,WV,WI,WY";
		
		String delim = "[,]";
		
		String[] states = stateString.split(delim);
		
		return states;
	}
	
	/**
	 * <pre>Utility method to build a Person JDO primary key from the inputted userName. Will
	 * always return a Key, even if the Person does not exist in the Datastore. </pre>
	 * @param userName - uwm email address
	 * @return Key - Person JDO primary key
	 */
	public static Key generateIdFromUserName(String userName){
		if(userName == null) return null;
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(Person.getParentkey());

		return keyBuilder.addChild(Person.getKind(), userName.toLowerCase()).getKey();		
	}

	/**
	 * Utility Method to build a person's Property map. 
	 * @param user - WrapperObject<Person> object
	 * @return A map of properties.
	 */
	public static Map<String, Object> makeUserProperties(WrapperObject<Person> user) {
		if (user == null) return null;
		Map<String, Object> properties =
				Library.propertyMapBuilder("firstname",user.getProperty("firstname")
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
	public static List<Map<String, Object>> makeWrapperProperties(List<WrapperObject<OfficeHours>> hours) {
		if (hours == null) return null;
		List<Map<String, Object>> officeHours = new ArrayList<Map<String, Object>>();
		for (WrapperObject<OfficeHours> hour : hours) {
			Map<String, Object> properties =
					Library.propertyMapBuilder("starttime",hour.getProperty("starttime")
	                                           ,"endtime",hour.getProperty("endtime")
	                                           ,"days",hour.getProperty("days")
	                                           );
			for(String key : properties.keySet())
				if(properties.get(key) == null) properties.put(key, "");
			officeHours.add(properties);
		}
		return officeHours;
	}
	
	/**
	 * Utility method to convert a Time from a double value to a String. 
	 * @param time - time to convert to String
	 * @return a String time of format: dD:DDXM where d is for times >= 10 and X is A or P
	 */
	public static String timeToString(double time) {
		if(time == -1) return "";
		
		int hoursIn24Cycle = (int)time;
		double fractionalMinutes = time - hoursIn24Cycle;

		long minutes = Math.round(fractionalMinutes * 60);
		int hoursIn12Cycle;
		boolean isPm = false;
		if(hoursIn24Cycle > 12){
			hoursIn12Cycle = hoursIn24Cycle - 12;
			isPm = true;
		}else{
			hoursIn12Cycle = hoursIn24Cycle;
			if(hoursIn12Cycle == 12) isPm = true;
		}
		String AmPm = isPm ? "PM" : "AM";
		String minutesString = Long.toString(minutes);

		minutesString = minutesString.length() == 1 ? "0" + minutesString : minutesString;



		return "" + hoursIn12Cycle + ":" + minutesString + AmPm;
	}
	
	/**
	 * Utility method that parses a String time to a double
	 * @param time - Expected format is dD:DDXM where d is for times >= 10:DD and X is an A or P
	 * @return <pre>the parsed time as a double. Mantissa value is the hours converted to a 24 hour clock
	 *         1-24 where 24 is 12AM. Exponent value is the minutes divided by 60 (e.g. 15/60 = .25). </pre>
	 */
	public static double parseTimeToDouble(String time){
		if(time == null || time.trim().isEmpty()) return -1;
		if(!time.toUpperCase().matches(OfficeHoursWrapper.HOURS_PATTERN))
			throw new IllegalArgumentException("Time does not match dD:DDXM pattern!");
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
	
	/**
	 * <pre>Utility method to build a Course JDO primary key from the inputed courseNum. Will
	 * always return a Key, even if the Course does not exist in the Datastore. </pre>
	 * @param courseNum - 3 digit string identifier of course (COMPSCI-XXX) where XXX is courseNum
	 * @return Key - Course JDO primary key
	 */
	public static Key generateIdFromCourseNum(String courseNum) {
		if(courseNum == null) return null;
		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(Course.getParentkey());
		return keyBuilder.addChild(Course.getKind(), courseNum.toLowerCase()).getKey();
	}
	
	public static Key generateSectionIdFromSectionAndCourseNum(String sectionNum, String courseNum){
		if(courseNum == null || sectionNum == null) return null;
		
		Key parentKey = generateIdFromCourseNum(courseNum);
		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentKey);
		
		return keyBuilder.addChild(Section.getKind(), sectionNum.toLowerCase()).getKey();
	}
	
	public static String dateToString(Date date){
		if (date == null) return "";
		DateFormat dateFormat = new SimpleDateFormat("MM/dd");

		return dateFormat.format(date);
	}
	
	public static Date stringToDate(String date) throws ParseException{
		if(date == null) return null;

		if(!date.matches(SectionWrapper.SECTION_DATE_PATTERN)) 
			throw new IllegalArgumentException("Date does not match pattern: MM/DD");
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String year = "2014";
		
		date += "/" + year;
		
		return dateFormat.parse(date);
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
	
	private static final String passwordKey = "ABCEDFGHIJKLMNOPQRSTUVWXYZabcedfghijklmnopqrstuvwxyz1234567890";
	private static final int passwordKeySize = 6;
	/**
	 * Utility method that generate a temporary random password for user who forgot there password
	 * @return String password
	 */
	public static String genderateRandomPassword() {
		String result ="";
		Random rnd = new Random();	
		for (int i = 0; i < passwordKeySize; i++) {
			int pos = rnd.nextInt(passwordKey.length());
			result += passwordKey.substring(pos, pos + 1);
		}
		return result;
	}
	
	/**
	 * Utility method that restricts an action per user Session
	 * @return boolean base on if action limit has been reached
	 */
	public static boolean setSessionActionLimit(HttpServletRequest request, String sessionID, int max) {
		String limit = (String) Auth.getSessionVariable(request, sessionID);
		
		if (limit == null) {	
			limit = "0";
			Auth.setSessionVariable(request, sessionID, "1");
		}
		
		int limitCount = Integer.parseInt(limit);
		if (limitCount >= max) {
			return true;
		}
		
		Auth.setSessionVariable(request, sessionID, String.valueOf(limitCount + 1));
		return false;
	}
}
