package edu.uwm.owyh.jdo;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.library.Library;

/**
 * OfficeHOurs jdo class
 * @author Vince Maiuri
 *
 */
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
	
	@Persistent
	private double startTime;
	
	@Persistent
	private double endTime;
	
	/**
	 * @return true if this officehours are on a Monday
	 */
	public boolean isOnMonday() {
		return onMonday;
	}

	/**
	 * @return true if this officehours are on a Tuesday
	 */
	public boolean isOnTuesday() {
		return onTuesday;
	}

	/**
	 * @return true if this officehours are on a Wednesday
	 */
	public boolean isOnWednesday() {
		return onWednesday;
	}

	/**
	 * @return true if this officehours are on a Thursday
	 */
	public boolean isOnThursday() {
		return onThursday;
	}

	/**
	 * @return true if this officehours are on a Friday
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
	 * @param onMonday Set to true if officehours are on a Monday 
	 */
	public void setOnMonday(boolean onMonday) {
		this.onMonday = onMonday;
	}

	/**
	 * @param onTuesday Set to true if officehours are on a Tuesday
	 */
	public void setOnTuesday(boolean onTuesday) {
		this.onTuesday = onTuesday;
	}

	/**
	 * @param onWednesday Set to true if officehours are on a Wednesday
	 */
	public void setOnWednesday(boolean onWednesday) {
		this.onWednesday = onWednesday;
	}

	/**
	 * @param onThursday Set to true if officehours are on a Thursday
	 */
	public void setOnThursday(boolean onThursday) {
		this.onThursday = onThursday;
	}

	/**
	 * @param onFriday Set to true if officehours are on a Friday
	 */
	public void setOnFriday(boolean onFriday) {
		this.onFriday = onFriday;
	}

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
	
	/**
	 * <pre>
	 * The number of properties that are "settable" from outside jdo
	 * -Days (String)
	 * -StartTime (String)
	 * -EndTime(String)
	 * </pre>
	 * @return the number of properties that should be in any PropertySetBuilder
	 */
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
	
	/**
	 * <pre>
	 * Override object equals method to provide functionality for List Contains() method.
	 * If days, starttime, and endtime are equal between two officehours jdos, then the
	 * jdos are considered equal.
	 * </pre> 
	 * @param other (the officehours jdo being compared with "this")
	 * @return True if the two jdos are equal
	 */
	@Override 
	public boolean equals(Object other){
		if(!(other instanceof OfficeHours)) return false;
		OfficeHours officeHours = (OfficeHours)other;
		boolean equalDays = this.getDays().equals(officeHours.getDays());
		boolean equalStart = this.getStartTime() == officeHours.getStartTime();
		boolean equalEnd = this.getEndTime() == officeHours.getEndTime();
		return equalDays && equalStart && equalEnd;
	}
	
	/**
	 * <pre>
	 * Override the object hashcode method because the equals method was overridden.
	 * Uses days, starttime, and endtime to generate hashcode.
	 * </pre>
	 */
	@Override
	public int hashCode(){
		return (int)Double.doubleToLongBits(getDays().hashCode() *
			   Math.round(getStartTime()) * 
			   Math.round(getEndTime()));
	}
	
	/**
	 * <pre>
	 * Provides Officehours in a standard string format
	 * e.g. MWF 11:30AM-2:45PM
	 * </pre>
	 */
	@Override
	public String toString()
    {
		String displayStartTime = Library.timeToString(startTime);
		String displayEndTime = Library.timeToString(endTime);
		
	    return days+" " + displayStartTime + "-" + displayEndTime;
	}
}
