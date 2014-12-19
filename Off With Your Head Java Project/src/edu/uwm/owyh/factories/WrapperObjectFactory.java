package edu.uwm.owyh.factories;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.ContactInfo;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.jdowrappers.ContactInfoWrapper;
import edu.uwm.owyh.jdowrappers.CourseWrapper;
import edu.uwm.owyh.jdowrappers.OfficeHoursWrapper;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.SectionWrapper;
import edu.uwm.owyh.jdowrappers.TAClassWrapper;

/**
 * Factories for instantiation of WrapperObjects.
 * @author Vince Maiuri 
 */
public class WrapperObjectFactory {
	
	/**
	 * <pre>
	 * Instantiates and returns a Person Wrapper Object
	 * Person will have all fields set to default values (NULL)
	 * </pre>
	 * @return WrapperObject<Person>
	 */
	public static WrapperObject<Person> getPerson(){
		return PersonWrapper.getPersonWrapper();
	}
	
	/**
	 * <pre>
	 * Instantiates and returns an OfficeHours Wrapper Object
	 * OfficeHours will have all fields set to default values:
	 *  Strings will be null
	 *  doubles will be -1
	 *  Objects will be null
	 * </pre>
	 * @return WrapperObject<OfficeHours>
	 */
	public static WrapperObject<OfficeHours> getOfficeHours(){
		return OfficeHoursWrapper.getOfficeHoursWrapper();
	}

	/**
	 * <pre>
	 * Instantiates and returns a Course Wrapper Object
	 * Course will have all fields set to default values (NULL)
	 * @return WrapperObject<Course>
	 */
	public static WrapperObject<Course> getCourse() {
		return CourseWrapper.getCourseWrapper();
	}

	public static WrapperObject<Section> getSection() {
		return SectionWrapper.getSectionWrapper();
	}
	
	public static WrapperObject<TAClass> getTAClass(){
		return TAClassWrapper.getTAClassWrapper();
	}
	
	public static WrapperObject<ContactInfo> getContactInfo(){
		return ContactInfoWrapper.getContactInfoWrapper();
	}

	/**
	 * <pre>Utility method to build a Person JDO primary key from the inputed userName. Will
	 * always return a Key, even if the Person does not exist in the Datastore. </pre>
	 * @param userName - uwm email address
	 * @return Key - Person JDO primary key
	 */
	public static Key generateIdFromUserName(String userName){
		if(userName == null) return null;
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(Person.getParentkey());
	
		return keyBuilder.addChild(Person.getKind(), userName.toLowerCase()).getKey();		
	}

	/**
	 * <pre>Utility method to build a Course JDO primary key from the inputed courseNum. Will
	 * always return a Key, even if the Course does not exist in the Datastore. </pre>
	 * @param courseNum - 3 digit string identifier of course (COMPSCI-XXX) where XXX is courseNum
	 * @return Key - Course JDO primary key
	 */
	public static Key generateIdFromCourseNum(String courseNum) {

		if(courseNum == null) return null;
		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(Course.getParentkey());
		return keyBuilder.addChild(Course.getKind(), courseNum.toLowerCase()).getKey();
	}

	public static Key generateSectionIdFromSectionAndCourseNum(String sectionNum, String courseNum){

		if(courseNum == null || sectionNum == null) return null;
		
		Key parentKey = generateIdFromCourseNum(courseNum);
		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentKey);
		
		return keyBuilder.addChild(Section.getKind(), sectionNum.toLowerCase()).getKey();
	}

	public static Key generateIdFromClassNumAndType(String classNum,
			String classType, String taUserName) {
		if(classNum == null || classType == null || taUserName == null) return null;
		
		Key parentKey = generateIdFromUserName(taUserName);
		
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(parentKey);
		
		return keyBuilder.addChild(TAClass.getKind(), classNum.toLowerCase() + ":" + classType.toLowerCase()).getKey();
	}
}
