package edu.uwm.owyh.library;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;

public final class AdminHelper {

	private AdminHelper() {
		// prevents instantiation
	}
	
	public static boolean assignPersonToScrapedSection(WrapperObject<Course> course){
		
		
		
		return false;
	}
	
	public static boolean assignInstructor(WrapperObject<Person> instructor, WrapperObject<Section> section, boolean setOverwrite){
		String sectionNum = (String)section.getProperty("sectionnum");
		
		removeOldInstructorFromSection(section);		
		
		assignNewInstructorToSection(instructor, section, setOverwrite);
		
		if(sectionNum.toLowerCase().startsWith("lec")) {
			assignNewInstructorToCourse(instructor, section);
		}
		
		addSectionToInstructor(instructor, section);
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public static List<WrapperObject<Person>> getInstructorList(WrapperObject<Section> section){
		List<WrapperObject<Person>> instructorList = new ArrayList<WrapperObject<Person>>();
		
		WrapperObject<Person> currentInstructor = (WrapperObject<Person>) section.getProperty("instructor");
		if(currentInstructor != null) instructorList.add(currentInstructor);
		
		try{
			instructorList.addAll(getEligibleTAs(section, currentInstructor));
			instructorList.addAll(getEligibleInstructors(section, currentInstructor));
		}catch(ParseException pe){
			throw new IllegalArgumentException("Illegal Date string format: " + pe.getMessage());
		}
		
		return instructorList;
	}

	private static List<WrapperObject<Person>> getEligibleInstructors(
			WrapperObject<Section> section,WrapperObject<Person> currentInstructor) throws ParseException {
		
		List<WrapperObject<Person>> eligibleInstructors = new ArrayList<WrapperObject<Person>>();
		
		List<WrapperObject<Person>> instructors = getAllInstructors(currentInstructor);
		
		for(WrapperObject<Person> instructor : instructors){
			if(checkScheduleConflicts(instructor, section)) continue;
			
			eligibleInstructors.add(instructor);
		}
		
		return eligibleInstructors;
	}

	private static List<WrapperObject<Person>> getAllInstructors(
			WrapperObject<Person> currentInstructor) {
		String userName = " ";
		if(currentInstructor != null){
			userName = (String) currentInstructor.getProperty("username");
		}
		int accessLevel = AccessLevel.INSTRUCTOR.getVal();
		
		String filter = "accessLevel == " + accessLevel + " && userName != '" + userName + "'";
		
		return WrapperObjectFactory.getPerson().findObjects(filter, null, null);
	}

	@SuppressWarnings("unchecked")
	private static List<WrapperObject<Person>> getEligibleTAs(
			WrapperObject<Section> section,
			WrapperObject<Person> currentInstructor) throws ParseException {
		
		List<WrapperObject<Person>> eligibleTaList = new ArrayList<WrapperObject<Person>>();
		
		WrapperObject<Course> parentCourse = 
				WrapperObjectFactory.getCourse().findObjectById(section.getId().getParent());
		
		List<Key> taKeys = (List<Key>) parentCourse.getProperty("eligibletakeys");
		
		for(Key taKey : taKeys){
			WrapperObject<Person> ta = getEligibleTA(taKey, currentInstructor, section);
			
			if(ta == null) continue;
			
			eligibleTaList.add(ta);
		}	
		
		return eligibleTaList;
	}

	private static WrapperObject<Person> getEligibleTA(
			Key taKey, 
			WrapperObject<Person> currentInstructor,
			WrapperObject<Section> section) throws ParseException {
		
		if(currentInstructor != null){
			if(taKey.equals(currentInstructor.getId())) return null;
		}
		
		WrapperObject<Person> ta = 
				WrapperObjectFactory.getPerson().findObjectById(taKey);
		
		if(checkScheduleConflicts(ta, section)) return null;
		if(checkTAClassConflicts(ta)) return null;
		
		return ta;
	}

	@SuppressWarnings("unchecked")
	private static boolean checkTAClassConflicts(WrapperObject<Person> ta) throws ParseException {
		boolean isConflict = false;
		List<WrapperObject<TAClass>> taClasses = (List<WrapperObject<TAClass>>) ta.getProperty("taclasses");
		
		for(WrapperObject<TAClass> taClass : taClasses){
			if(isConflict) break;
			isConflict = checkScheduleConflicts(ta, taClass);
		}
		
		return isConflict;
	}

	@SuppressWarnings("unchecked")
	private static void addSectionToInstructor(
			WrapperObject<Person> instructor, WrapperObject<Section> section) {
		String sectionNum = (String)section.getProperty("sectionnum");
		List<Key> lectureCourses = null;
		if(sectionNum.toLowerCase().startsWith("lec")) lectureCourses = assignCourseToLecturer(instructor, section);
		
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>)instructor.getProperty("sections");
				
		sections.add(section);
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("sections", sections);
		if(lectureCourses != null)properties.put("lecturecourses", lectureCourses);
		
		instructor.editObject(properties);
	}

	@SuppressWarnings("unchecked")
	private static List<Key> assignCourseToLecturer(
			WrapperObject<Person> instructor, WrapperObject<Section> section) {
		Key courseKey = section.getId().getParent();
		List<Key> lectureCourses = (List<Key>) instructor.getProperty("lecturecourses");
		
		lectureCourses.add(courseKey);
		
		return lectureCourses;
	}

	private static void assignNewInstructorToSection(
			WrapperObject<Person> instructor, WrapperObject<Section> section, boolean setOverwrite) {
		
		String instructorFirstName = (String)instructor.getProperty("firstname");
		String instructorLastName = (String)instructor.getProperty("lastname");
				
		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("instructorfirstname", instructorFirstName
										 								 ,"instructorlastname", instructorLastName
										 								 ,"instructor", instructor
										 								 ,"overwriteinstructor", setOverwrite);
		section.editObject(properties);
	}

	@SuppressWarnings("unchecked")
	private static void assignNewInstructorToCourse(
			WrapperObject<Person> instructor, WrapperObject<Section> section) {
		
		WrapperObject<Course> course = WrapperObjectFactory.getCourse().findObjectById(section.getId().getParent());
		
		List<Key> lectureInstructors = (List<Key>) course.getProperty("lectureinstructors");
		
		lectureInstructors.add(instructor.getId());
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("lectureinstructors", lectureInstructors);
		
		course.editObject(properties);
	}

	@SuppressWarnings("unchecked")
	private static void removeOldInstructorFromSection(
			WrapperObject<Section> section) {
		Map<String, Object> properties;

		WrapperObject<Person> oldInstructor = (WrapperObject<Person>)section.getProperty("instructor");
		
		if(oldInstructor == null) return;
		String sectionNum = (String)section.getProperty("sectionnum");
		List<Key> courses = null;
		if(sectionNum.toLowerCase().startsWith("lec")){
			removeInstructorFromCourse(oldInstructor, section);
			courses = removeCourseFromInstructor(oldInstructor, section);
		}
		
	
		List<WrapperObject<Section>> oldInstructorSectionList = (List<WrapperObject<Section>>) oldInstructor.getProperty("sections");
		oldInstructorSectionList.remove(section);
		properties = PropertyHelper.propertyMapBuilder("sections", oldInstructorSectionList);
		if(courses != null) properties.put("lecturecourses", courses);
		oldInstructor.editObject(properties);
	}

	@SuppressWarnings("unchecked")
	private static List<Key> removeCourseFromInstructor(
			WrapperObject<Person> oldInstructor, WrapperObject<Section> section) {		
		List<Key> courses = (List<Key>) oldInstructor.getProperty("lecturecourses");
		
		courses.remove(section.getId().getParent());
		
		return courses;
	}

	@SuppressWarnings("unchecked")
	private static void removeInstructorFromCourse(
			WrapperObject<Person> oldInstructor, WrapperObject<Section> section) {
		Key courseKey = section.getId().getParent();
		WrapperObject<Course> course = WrapperObjectFactory.getCourse().findObjectById(courseKey);
		
		List<Key> instructorKeys = (List<Key>) course.getProperty("lectureinstructors");
		instructorKeys.remove(oldInstructor.getId());
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("lectureinstructors", instructorKeys);
		course.editObject(properties);
	}

	
	public static <T> boolean checkScheduleConflicts(WrapperObject<Person> instructor,
			WrapperObject<T> conflict) throws ParseException {
		
		List<WrapperObject<T>> conflicts = getConflictList(conflict, instructor);
		
		String startDate = (String)conflict.getProperty("startdate");
		String endDate = (String)conflict.getProperty("enddate");
		
		if(checkDateConflict(startDate, endDate, conflicts)) return true;

		String days = (String)conflict.getProperty("days");
		
		if(checkDaysConflict(days, conflicts)) return true;
		
		String startTime = (String)conflict.getProperty("starttime");
		String endTime = (String)conflict.getProperty("endtime");
		
		return checkTimeConflict(startTime, endTime, conflicts);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<WrapperObject<T>> getConflictList(WrapperObject<T> conflict, WrapperObject<Person> instructor){
		String kind = conflict.getTable().getSimpleName();
		
		switch(kind.toLowerCase()){
		case "section":
			return (List<WrapperObject<T>>) instructor.getProperty("sections");
		case "officehours":
			return (List<WrapperObject<T>>) instructor.getProperty("officehours");
		case "taclass":
			return (List<WrapperObject<T>>) instructor.getProperty("taclasses");
		default:
			throw new IllegalArgumentException(kind + " does not have date-time values to check conflicts against!");
		}
	}

	private static <T> boolean checkTimeConflict(String startTime, String endTime,
			List<WrapperObject<T>> conflicts) {
		boolean isConflict = false;
		double compStart = StringHelper.parseTimeToDouble(startTime);
		double compEnd = StringHelper.parseTimeToDouble(endTime);
		
		for(WrapperObject<T> conflict : conflicts){
			if(isConflict) break;
			
			double start = 
					StringHelper.parseTimeToDouble((String)conflict.getProperty("starttime"));
			double end =
					StringHelper.parseTimeToDouble((String)conflict.getProperty("endtime"));
			
			isConflict = (start >= compStart && start < compEnd) ||
					     (end >= compStart && end < compEnd);
		}
		
		return isConflict;
	}

	private static <T> boolean checkDaysConflict(String days,
			List<WrapperObject<T>> conflicts) {
		boolean isConflict = false;
		char[] aDays = days.toCharArray();
		
		for(WrapperObject<T> conflict : conflicts){
			if(isConflict) break;
			String compDays = (String)conflict.getProperty("days"); 
			for(int i=0; i<aDays.length; ++i){
				isConflict = compDays.indexOf(aDays[i]) > -1;
				if(isConflict) break;
			}			
		}		
		return isConflict;
	}

	private static <T> boolean checkDateConflict(String startDate, String endDate,
			List<WrapperObject<T>> conflicts) throws ParseException {
		Date compStart = StringHelper.stringToDate(startDate);
		Date compEnd = StringHelper.stringToDate(endDate);
		boolean isConflict = false;
		
		for(WrapperObject<T> conflict : conflicts){
			if(isConflict)break;
			Date start =
					StringHelper.stringToDate((String)conflict.getProperty("startdate"));
			Date end =
					StringHelper.stringToDate((String)conflict.getProperty("enddate"));
			
			isConflict = (!compStart.before(start) && !compStart.after(end)) ||
					     (!compEnd.before(start) && !compEnd.after(end));
			
		}
		return isConflict;
	}
}
