package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.library.Library;

/**
 * The Course JDO class
 * @author Vince Maiuri
 *
 */
@PersistenceCapable
public class Course implements Serializable, Cloneable{
	private static final Key PARENTKEY = KeyFactory.createKey("Courses", "RootCourses");
	private static final String KIND = Course.class.getSimpleName();
	private static final Class<Course> CLASSNAME = Course.class;

	private static final long serialVersionUID = -2471383256139472451L;

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	@Extension(vendorName="datanucleus", key="gae.parent-pk", value="true")
	private String parentKey;

	@Persistent(mappedBy = "parent")
	@Element(dependent="true")
	private List<Section> sections;

	@Persistent
	private String courseNum;

	@Persistent
	private String courseName;
	
	@Persistent
	private List<Key> eligibleTAKeys;



	private Course(String courseNum){
		id = Library.generateIdFromCourseNum(courseNum);

		sections = new ArrayList<Section>();
		eligibleTAKeys = new ArrayList<Key>();

		setCourseNum(courseNum);
	}

	/**
	 * Public accessor for the Course JDO
	 * @param courseNum - course number to instantiate the Course JDO
	 * @return an instantiated Course JDO
	 */
	public static Course getCourse(String courseNum){
		return new Course(courseNum);
	}

	//Accessors	

	/**
	 * Access for the Primary key
	 * @return Primary key
	 */
	public Key getId(){
		return id;
	}

	/**
	 * @return the classname
	 */
	public static Class<Course> getClassname() {
		return CLASSNAME;
	}

	/**
	 * @return the parentkey
	 */
	public static Key getParentkey() {
		return PARENTKEY;
	}

	/**
	 * @return the kind
	 */
	public static String getKind() {
		return KIND;
	}

	/**
	 * @return the parentKey
	 */
	public String getParentKey() {
		return parentKey;
	}

	/**
	 * @return the course number
	 */
	public String getCourseNum(){
		return courseNum;
	}

	/**
	 * @return the course name
	 */
	public String getCourseName(){
		return courseName;
	}

	public List<Section> getSections(){
		return sections;
	}

	//Mutators

	//private mutator to set the course number
	private void setCourseNum(String courseNum){
		this.courseNum = courseNum;
	}

	/**
	 * Sets the course name field
	 * @param courseName
	 */
	public void setCourseName(String courseName){
		this.courseName = courseName;
	}

	/**
	 * @return the eligibleTAKeys
	 */
	public List<Key> getEligibleTAKeys() {
		return eligibleTAKeys;
	}

	//Utility methods
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Course)) return false;
		
		Course other = (Course) obj;
		
		String courseNum = this.getCourseNum();
		String otherCourseNum = other.getCourseNum();
		
		return courseNum == otherCourseNum;
	}

	/**
	 * Adds a section jdo to the Sections List field
	 * @param section
	 */
	public void addSection(Section section) {
		if(section == null) 
			throw new NullPointerException("Section cannot be null!");
		if(getSections().contains(section))
			throw new IllegalArgumentException("Duplicate section entry!");
		this.sections.add(section);
	}
	
	public void removeSection(Section section) {
		if(section == null)
			throw new NullPointerException("Section cannot be null!");
		this.getSections().remove(section);
	}
	
	public void setEligibleTAKeys(List<Key> eligibleTAKeys){
		this.eligibleTAKeys = eligibleTAKeys;
	}
}
