/**
 * 
 */
package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;

/**
 * @author Vince Maiuri
 *
 */
public class TAClass implements Serializable {

	private static final long serialVersionUID = -6686305816196655441L;
	private static final String KIND = TAClass.class.getSimpleName();
	private static final Class<TAClass> CLASSNAME = TAClass.class;
	
	@Persistent
	private Person parent;
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	private String classNum;
	
	@Persistent
	private String classType;
	
	@Persistent
	private String className;
	
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
	private boolean onMonday;
	
	@Persistent
	private boolean onTuesday;
	
	@Persistent
	private boolean onWednesday;
	
	@Persistent
	private boolean onThursday;
	
	@Persistent
	private boolean onFriday;
	

	private TAClass(String classNum, String classType, String taUserName){
		setClassNum(classNum);
		setClassType(classType);
		
		Key taClassKey = WrapperObjectFactory.generateIdFromClassNumAndType(classNum, classType, taUserName);
		setId(taClassKey);
		
		setClassName("");
		setDays("");
		setStartDate(new Date());
		setEndDate(new Date());
		setStartTime(-1);
		setEndTime(-1);
	}
	
	private TAClass() {
		
	}


	public static TAClass getTAClass(){
		return new TAClass();
	}
	
	public static TAClass getTAClass(String classNum, String classType, String taUserName){
		return new TAClass(classNum, classType, taUserName);
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
	public static Class<TAClass> getClassname() {
		return CLASSNAME;
	}


	/**
	 * @return the id
	 */
	public Key getId() {
		return id;
	}


	/**
	 * @return the classNum
	 */
	public String getClassNum() {
		return classNum;
	}
	
	
	/**
	 * @return the classType
	 */
	public String getClassType() {
		return classType;
	}


	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}


	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
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
	 * @param id the id to set
	 */
	private void setId(Key id) {
		this.id = id;
	}


	/**
	 * @param classNum the classNum to set
	 */
	private void setClassNum(String classNum) {
		this.classNum = classNum;
	}


	/**
	 * @param classType the classType to set
	 */
	private void setClassType(String classType) {
		this.classType = classType;
	}


	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}


	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
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

	//Utility Methods
	
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

	@Override
	public boolean equals(Object object){
		if(!(object instanceof TAClass)) return false;
		
		TAClass other = (TAClass)object;
		
		return other.getId().equals(this.getId());
	}
	
	@Override
	public int hashCode(){
		return this.getId().hashCode();
	}
	
	
}
