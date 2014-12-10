package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.library.Library;


/**
 * Section JDO class
 * @author Vince Maiuri
 */
@PersistenceCapable
public class Section implements Serializable, Cloneable{	
	private static final long serialVersionUID = -6125753884855098249L;
	
	private static final String KIND = Section.class.getSimpleName();
	private static final Class<Section> CLASSNAME = Section.class;

	@Persistent
	private Course parent;
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String sectionNum;
	
	@Persistent
	private String sectionType;

	/**
	 * @return the sectionType
	 */
	public String getSectionType() {
		return sectionType;
	}

	/**
	 * @param sectionType the sectionType to set
	 */
	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}

	@Persistent
	private String instructorFirstName;

	@Persistent
	private String instructorLastName;

	@Persistent
	private String days;

	@Persistent 
	private Date startDate;

	@Persistent
	private Date endDate;

	@Persistent
	private double startTime;

	@Persistent
	private double endTime;

	@Persistent
	private String room;

	@Persistent
	private String credits;

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
	private boolean overwriteNames;
	
	/**
	 * @return the overwriteNames
	 */
	public boolean isOverwriteNames() {
		return overwriteNames;
	}

	/**
	 * @param overwriteNames the overwriteNames to set
	 */
	public void setOverwriteNames(boolean overwriteNames) {
		this.overwriteNames = overwriteNames;
	}

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


	/**
	 * @return the instructorFirstName
	 */
	public String getInstructorFirstName() {
		return instructorFirstName;
	}

	/**
	 * @return the instructorLastName
	 */
	public String getInstructorLastName() {
		return instructorLastName;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
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
	 * @param instructorFirstName the instructorFirstName to set
	 */
	public void setInstructorFirstName(String instructorFirstName) {
		this.instructorFirstName = instructorFirstName;
	}

	/**
	 * @param instructorLastName the instructorLastName to set
	 */
	public void setInstructorLastName(String instructorLastName) {
		this.instructorLastName = instructorLastName;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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


	private Section(String sectionNum, String courseNum){		
		Key sectionKey = Library.generateSectionIdFromSectionAndCourseNum(sectionNum, courseNum);
		String sectionType = sectionNum.trim().substring(0, 3);
		String num = sectionNum.trim().substring(3);
		setStartTime(-1);
		setEndTime(-1);
		setOverwriteNames(false);
		
		setId(sectionKey);
		
		setSectionType(sectionType);
		setSectionNum(num.trim());		
	}

	private Section(){
		
	}

	/**
	 * Public accessor for Section JDO
	 * @return Section jdo
	 */
	public static Section getSection(String sectionNum, String courseNum){
		return new Section(sectionNum, courseNum);
	}

	public static Section getSection() {
		return new Section();
	}

	/**
	 * @return the classname
	 */
	public static Class<Section> getClassname() {
		return CLASSNAME;
	}

	/**
	 * @return the kind
	 */
	public static String getKind() {
		return KIND;
	}

	/**
	 * @return the id
	 */
	public Key getId() {
		return id;
	}

	/**
	 * @return the sectionNum
	 */
	public String getSectionNum() {
		return sectionNum;
	}

	/**
	 * @return the parent
	 */
	public Course getParentCourse() {
		return parent;
	}

	/**
	 * @return the credits
	 */
	public String getCredits() {
		return credits;
	}

	/**
	 * @return the startDay
	 */
	public String getDays() {
		return days;
	}

	/**
	 * @return the room
	 */
	public String getRoom(){
		return room;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits(String credits) {
		this.credits = credits;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}
	
	/**
	 * @param room the room to set
	 */
	public void setRoom(String room){
		this.room = room;
	}
	
	/**
	 * @param sectionNum Sets the section number
	 */
	public void setSectionNum(String sectionNum) {
		this.sectionNum = sectionNum;
	}

	
	private void setId(Key id) {
		this.id = id;
	}
	
	//Utility Methods
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Section)) return false;
		Section other = (Section) obj;
		if(!this.getId().equals(other.getId())) return false;
		
		boolean isSameInstructor = !this.isOverwriteNames() || 
				(this.getInstructorFirstName().equalsIgnoreCase(other.getInstructorFirstName())
				&& this.getInstructorLastName().equalsIgnoreCase(other.getInstructorLastName()));
		
		boolean isSameDays = this.getDays().equalsIgnoreCase(other.getDays());
		
		boolean isSameDateRange = this.getStartDate().equals(other.getStartDate()) 
				&& this.getEndDate().equals(other.getEndDate());
		
		boolean isSameTimes = this.getStartTime() == other.getStartTime()
				&& this.getEndTime() == other.getEndTime();
		
		boolean isSameRoom = this.getRoom().equalsIgnoreCase(other.getRoom());
		
		boolean isSameCredits = this.getCredits().equals(other.getCredits());
		
		return isSameInstructor && isSameDays && isSameDateRange && isSameTimes && isSameRoom && isSameCredits;
	}
}
