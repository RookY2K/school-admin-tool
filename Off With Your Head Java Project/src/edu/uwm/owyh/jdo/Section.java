package edu.uwm.owyh.jdo;

import java.io.Serializable;

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
	private String instructor;

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String sectionNum;
	
	@Persistent
	private String days;
	
	//TODO split into start and end date
	//TODO set type as java.util.Date
	@Persistent
	private String dates;
	
	//TODO split into start and end time
	//TODO set type as double
	@Persistent
	private String hours;
	
	@Persistent
	private String room;
	
	@Persistent
	private String credits;
	
	private Section(String sectionNum, Course parentCourse){		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentCourse.getId());
		
		setId(keyBuilder.addChild(KIND, sectionNum).getKey());
		setSectionNum(sectionNum);
		setParentCourse(parentCourse);
	}

	private Section(){
		
	}

	/**
	 * Public accessor for Section JDO
	 * @return Section jdo
	 */
	public static Section getSection(){
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
	public String getInstructor() {
		return instructor;
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
	 * @param instructor the teacher to set
	 */
	public void setInstructor(String instructor) {
		this.instructor = instructor;
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
