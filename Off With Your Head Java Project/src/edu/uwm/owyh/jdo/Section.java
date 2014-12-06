package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


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
	private Course parentCourse;
	
	//TODO implement this as a Person object (Maybe?)
	@Persistent
	private String instructorName;
	
	@Persistent
	private String instructorFirstName;
	
	@Persistent
	private String instructorLastName;

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String sectionNum;
	
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


	//TODO split into start and end date
	//TODO set type as java.util.Date
	@Persistent
	private String dates;
	
	@Persistent 
	private Date startDate;
	
	@Persistent
	private Date endDate;
	
	//TODO split into start and end time
	//TODO set type as double
	@Persistent
	private String hours;
	
	@Persistent
	private double startTime;
	
	@Persistent
	private double endTime;
	
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


	@Persistent
	private String room;
	
	@Persistent
	private String credits;
	
	private Section(String sectionNum, Course parentCourse){		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentCourse.getId());
		
		setId(keyBuilder.addChild(KIND, sectionNum).getKey());
		setSectionNum(sectionNum);
	}

	private Section(){
		
	}

	/**
	 * Public accessor for Section JDO
	 * @return Section jdo
	 */
	public static Section getSection(String sectionNum, Course parentCourse){
		return new Section(sectionNum, parentCourse);
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
	 * @return the parentCourse
	 */
	public Course getParentCourse() {
		return parentCourse;
	}

	/**
	 * @return the dates
	 */
	public String getDates() {
		return dates;
	}

	/**
	 * @return the hours
	 */
	public String getHours() {
		return hours;
	}

	/**
	 * @return the credits
	 */
	public String getCredits() {
		return credits;
	}

	/**
	 * @return the teacher
	 */
	public String getInstructorName() {
		return instructorName;
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
	 * @param dates the dates to set
	 */
	public void setDates(String dates) {
		this.dates = dates;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(String hours) {
		this.hours = hours;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits(String credits) {
		this.credits = credits;
	}
	
	/**
	 * @param instructorName the teacher to set
	 */
	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
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

	//Private mutator to assign the course this section belongs to
	private void setParentCourse(Course parentCourse) {
		if(parentCourse != null) 
			throw new IllegalStateException("This section is already assigned to a course!");
		this.parentCourse = parentCourse;
	}

	
	private void setId(Key id) {
		this.id = id;
	}
	
	//Utility Methods
}
