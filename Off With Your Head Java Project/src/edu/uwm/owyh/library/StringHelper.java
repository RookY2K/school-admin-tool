package edu.uwm.owyh.library;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uwm.owyh.jdowrappers.OfficeHoursWrapper;
import edu.uwm.owyh.jdowrappers.SectionWrapper;

public final class StringHelper {

	private StringHelper() {
		// prevents instantiation
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
		
		time = time.trim();
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

}
