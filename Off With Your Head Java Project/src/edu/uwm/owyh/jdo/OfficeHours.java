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
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private Person parentPerson;
	
	@Persistent
	private String days;
	
	@Persistent
	private double startTime;
	
	@Persistent
	private double endTime;
	
	//Private constructor
	
	private OfficeHours(){
		//Default constructor
		startTime = -1;
		endTime = -1;
	}
	
	/**
	 * Public accessor method for OfficeHours jdo
	 * @return OfficeHours jdo
	 */
	public OfficeHours getOfficeHours(){
		return new OfficeHours();
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
	public OfficeHours clone(){
		OfficeHours other = null;
		
		try{
			other = (OfficeHours)super.clone();
		}catch(CloneNotSupportedException e){
			//Won't happen
			e.printStackTrace();
		}
		
		
		return other;
	}
	
	
}
