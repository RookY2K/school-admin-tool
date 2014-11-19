package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
	
	@Persistent(mappedBy = "parentCourse")
	private List<Section> sections;
	
	@Persistent
	private String courseNum;
	@Persistent
	private String courseName;
	@Persistent
	private int credits;
	
	
	private Course(String courseNum){
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
		
		id = keyBuilder.addChild(KIND, courseNum).getKey();
		
		setCourseNum(courseNum);
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
		
		/**
		 * @return the credit load for the course
		 */
		public int getCredits(){
			return credits;
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
		 * Sets the credit load field
		 * @param numCredits
		 */
		public void setCredits(int numCredits){
			credits = numCredits;
		}
		
		//Utility methods
		
		/**
		 * Provides a deep clone of a Course JDO. The returned JDO will
		 * not be persisted in the Datastore if changes are made to it's
		 * fields. 
		 * @return cloned Course JDO
		 */
		@Override
		public Course clone(){
			Course other = null;
			try {
				 other = (Course) super.clone();
			} catch (CloneNotSupportedException e) {
				//Will not happen as Course implements Cloneable
				e.printStackTrace();
			}
			
			KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
			other.id = keyBuilder.addChild(KIND, other.courseNum).getKey();
			
			other.parentKey = null;
			
			for(Section section : sections){
				Section otherSection = (Section) section.clone();
				other.sections.add(otherSection);
			}
			
			return other;			
		}
}
