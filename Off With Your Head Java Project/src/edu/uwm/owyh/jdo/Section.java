package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Section implements Serializable, Cloneable{
	private static final long serialVersionUID = -6125753884855098249L;
	
	private static final String KIND = Section.class.getSimpleName();
	private static final Class<Section> CLASSNAME = Section.class;

	@Persistent
	private Course parentCourse;
	
	@Persistent
	private Person teacher;

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String sectionNum;
	
	@Persistent
	private String days;
	
	@Persistent
	private double startTime;
	
	@Persistent
	private double endTime;
	
	@Persistent
	private Date startDay;
	
	@Persistent
	private Date endDay;
	
	@Persistent
	private String room;
	
	private Section(String sectionNum, Course parentCourse){		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentCourse.getId());
		
		setId(keyBuilder.addChild(KIND, sectionNum).getKey());
		setSectionNum(sectionNum);
		setParentCourse(parentCourse);
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
	public static Class<Section> getClassname() {
		return CLASSNAME;
	}

	/**
	 * @return the parentCourse
	 */
	public Course getParentCourse() {
		return parentCourse;
	}

	/**
	 * @return the teacher
	 */
	public Person getTeacher() {
		return teacher;
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
	 * @return the days
	 */
	public String getDaysHeld() {
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
	 * @return the startDay
	 */
	public Date getStartDay() {
		return startDay;
	}

	/**
	 * @return the endDay
	 */
	public Date getEndDay() {
		return endDay;
	}
	
	/**
	 * @return the room
	 */
	public String getRoom(){
		return room;
	}

	/**
	 * @param teacher the teacher to set
	 */
	public void setTeacher(Person teacher) {
		this.teacher = teacher;
	}

	/**
	 * @param days the days to set
	 */
	public void setDaysHeld(String daysHeld) {
		this.days = daysHeld;
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

	/**
	 * @param startDay the startDay to set
	 */
	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	/**
	 * @param endDay the endDay to set
	 */
	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}
	
	/**
	 * @param room the room to set
	 */
	public void setRoom(String room){
		this.room = room;
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

	
	private void setSectionNum(String sectionNum) {
		this.sectionNum = sectionNum;
	}
	
	@Override
	public Section clone(){
		Section other = null;
		try {
			 other = (Section) super.clone();
		} catch (CloneNotSupportedException e) {
			//Will not happen as Section implements Cloneable
			e.printStackTrace();
		}
		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(other.getParentCourse().getId());
		other.id = keyBuilder.addChild(KIND, other.sectionNum).getKey();		
		
		return other;
	}
	
}
