package edu.uwm.owyh.factories;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdowrappers.OfficeHoursWrapper;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.SectionWrapper;
import edu.uwm.owyh.jdowrappers.CourseWrapper;

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
}
