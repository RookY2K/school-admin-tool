package edu.uwm.owyh.jdo;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class OfficeHours implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -5764789184332986969L;
	private static final String KIND = OfficeHours.class.getSimpleName();
	private static final Class<OfficeHours> CLASSNAME = OfficeHours.class;
	private static final int NUM_PROPERTIES = 3; 

	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private Person parentPerson;
	
	@Persistent
	private String days;
	
	@Persistent
	private boolean onMonday;
	
	@Persistent
	private boolean onTuesday;
	
	@Persistent
	private boolean onWednesday;
	
	@Persistent
	private boolean onThursday;
	
	@Persistent
	private boolean onFriday;
	
	/**
	 * @return the onMonday
	 */
	public boolean isOnMonday() {
		return onMonday;
	}

	/**
	 * @return the onTuesday
	 */
	public boolean isOnTuesday() {
		return onTuesday;
	}

	/**
	 * @return the onWednesday
	 */
	public boolean isOnWednesday() {
		return onWednesday;
	}

	/**
	 * @return the onThursday
	 */
	public boolean isOnThursday() {
		return onThursday;
	}

	/**
	 * @return the onFriday
	 */
	public boolean isOnFriday() {
		return onFriday;
	}

	/**
	 * @param parentPerson the parentPerson to set
	 */
	public void setParentPerson(Person parentPerson) {
		this.parentPerson = parentPerson;
	}

	/**
	 * @param onMonday the onMonday to set
	 */
	public void setOnMonday(boolean onMonday) {
		this.onMonday = onMonday;
	}

	/**
	 * @param onTuesday the onTuesday to set
	 */
	public void setOnTuesday(boolean onTuesday) {
		this.onTuesday = onTuesday;
	}

	/**
	 * @param onWednesday the onWednesday to set
	 */
	public void setOnWednesday(boolean onWednesday) {
		this.onWednesday = onWednesday;
	}

	/**
	 * @param onThursday the onThursday to set
	 */
	public void setOnThursday(boolean onThursday) {
		this.onThursday = onThursday;
	}

	/**
	 * @param onFriday the onFriday to set
	 */
	public void setOnFriday(boolean onFriday) {
		this.onFriday = onFriday;
	}

	@Persistent
	private double startTime;
	
	@Persistent
	private double endTime;
	
//	private OfficeHours(){		
//		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentPerson.getId());
//		
//		setId(keyBuilder.addChild(KIND, formatString).getKey());
//		
//		/* Parses the string and loads the values into this object. */
//		parseFormatString(formatString);
//		setParentPerson(parentPerson);
//	}

	
	private OfficeHours(){
		//Default constructor
		startTime = -1;
		endTime = -1;
		onMonday = false;
		onTuesday = false;
		onWednesday = false;
		onThursday = false;
		onFriday = false;
	}
	
	/**
	 * Public accessor method for OfficeHours jdo
	 * @return OfficeHours jdo
	 */
	public static OfficeHours getOfficeHours(){
		return new OfficeHours();
	}
	
	/**
	 * @return the kind
	 */
	public static String getKind() {
		return KIND;
	}

	/**
	 * @return the classname
	 */
	public static Class<OfficeHours> getClassname() {
		return CLASSNAME;
	}
	
	//Accessors
	
	/**
	 * Accessor for primary key of OfficeHours jdo
	 * @return Key id
	 */
	public Key getId(){
		return id;
	}

	/**
	 * @return the parentPerson
	 */
	public Person getParentPerson() {
		return parentPerson;
	}
	
	public static int numProperties(){
		return NUM_PROPERTIES;
	}

	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
	}

	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public double getEndTime() {
		return endTime;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
	
	//Utility Methods
	
	@Override 
	public boolean equals(Object other){
		if(!(other instanceof OfficeHours)) return false;
		OfficeHours officeHours = (OfficeHours)other;
		boolean equalDays = this.getDays().equals(officeHours.getDays());
		boolean equalStart = this.getStartTime() == officeHours.getStartTime();
		boolean equalEnd = this.getEndTime() == officeHours.getEndTime();
		return equalDays && equalStart && equalEnd;
	}
	
	@Override
	public int hashCode(){
		return (int)Double.doubleToLongBits(getDays().hashCode() *
			   Math.round(getStartTime()) * 
			   Math.round(getEndTime()));
	}
	
//	public void parseFormatString(String formatString)
//	{
//		String newStartHour, newStartMinute, newEndHour, newEndMinute, AmPm;
//
//		double newStartTime, newEndTime;
//
//		// Set up for first parse, Days. 
//		int startIndex = 0;
//		int endIndex = formatString.indexOf(" ");				   
//		days = formatString.substring(startIndex, endIndex);
//
//		// Set up for next parse, Start Hour
//		startIndex = endIndex+1;
//		endIndex = formatString.indexOf(":", startIndex);
//		newStartHour = formatString.substring(startIndex, endIndex);
//		newStartTime = Double.parseDouble(newStartHour);
//
//		// Next Parse, start Minute. 
//		startIndex = endIndex+1;
//		endIndex = formatString.indexOf("M", startIndex);  //Of AM/PM 
//		newStartMinute = formatString.substring(startIndex, endIndex-1);
//
//		/* Add the conversion of our minutes section. */
//		newStartTime += ((double)Double.parseDouble(newStartMinute)/((double)60));
//
//		/* Next Parse, AM/PM for start hours. Special case. We
//		 * already know where the string we want is. Just pull it. */
//		startIndex = endIndex-1;
//		AmPm = formatString.substring(startIndex, endIndex);
//
//		// Check for AM or PM and modify startHours accordingly. 
//		if(AmPm.charAt(0) == 'P')
//		{
//			/* Account for the 1 wierd case, 12PM. */
//			if(newStartTime < 12 || newStartTime >= 13)
//			{
//			    // PM start time. Add 12 to start Hours. 
//			    newStartTime+=12;
//			}
//		}
//
//		// End Hours. 
//		startIndex = endIndex+2; //+2 to account for '-' character. 
//		endIndex = formatString.indexOf(":", startIndex);
//		newEndHour = formatString.substring(startIndex, endIndex);
//		newEndTime = Double.parseDouble(newEndHour);
//
//		// Next Parse, end Minute. 
//		startIndex = endIndex+1;
//		endIndex = formatString.indexOf("M", startIndex);  //Of AM/PM 
//		newEndMinute = formatString.substring(startIndex, endIndex-1);
//
//		/* Add the conversion of our minutes section. */
//		newEndTime += ((double)Double.parseDouble(newEndMinute)/((double)60));
//
//		/* Next Parse, AM/PM for start hours. Special case. We
//		 * already know where the string we want is. Just pull it. */
//		startIndex = endIndex-1;
//		AmPm = formatString.substring(startIndex, endIndex);
//
//		// Check for AM or PM and modify startHours accordingly. 
//		if(AmPm.charAt(0) == 'P')
//		{
//			/* Account for the 1 weird time case, 12PM. */
//			if(newEndTime < 12 || newEndTime >= 13)
//			{
//			    // PM start time. Add 12 to start Hours. 
//				newEndTime+=12;
//			}			   
//		}
//		
//		this.startTime = newStartTime;
//		this.endTime = newEndTime;
//	}
	
	@Override
	public String toString()
    {
		double displayStartTime;
		double displayEndTime;
		
		/* Assume AM to start. */
		String startAmPm = "AM";
		String endAmPm = "AM";
		String displayStartHours;
		String displayEndHours;
		int displayStartMinutes;
		int displayEndMinutes;
		int truncatedValue;

		displayStartTime = startTime;
		displayEndTime = endTime;
		   
		if(displayStartTime >= 12)
		{
			/* Account for 1 wierd time, 12PM. */
			if(displayStartTime >= 13)
				displayStartTime-=12;
			
			startAmPm = "PM";
		}
		/* Account for midnight showing 12. */
		else if(displayStartTime >=0 && displayStartTime < 1)
			displayStartTime+=12;
		   
		if(displayEndTime > 12)
		{
			/* Account for 1 wierd time, 12PM. */
			if(displayEndTime >= 13)
				displayEndTime-=12;
			
			endAmPm = "PM";
		}
		/* Account for midnight showing 12. */
		else if(displayEndTime >=0 && displayEndTime < 1)
			displayEndTime+=12;
		
		/* casting to int truncates. Then convert to String. */
		truncatedValue = (int)displayStartTime;
		displayStartHours = Integer.toString(truncatedValue);
		
		truncatedValue = (int)displayEndTime;
		displayEndHours = Integer.toString(truncatedValue);
		
		/* Subtract the a truncated value from itself, leaving the decimal portion behind. */
		truncatedValue = (int)displayStartTime;
		
		/* Now, multiply decimal portion by 60 to convert to minutes, round, and convert to String. */
		displayStartMinutes = (int)Math.round((displayStartTime - truncatedValue)*60);
		
		/* Subtract the a truncated value from itself, leaving the decimal portion behind. */
		truncatedValue = (int)displayEndTime;
		
		/* Now, multiply decimal portion by 60 to convert to minutes, round, and convert to String. */
		displayEndMinutes = (int)Math.round((displayEndTime - truncatedValue)*60);

	    return(days+" "+displayStartHours+":"+String.format("%02d", displayStartMinutes)+startAmPm+"-"+displayEndHours+":"+String.format("%02d", displayEndMinutes)+endAmPm);
	}
}
