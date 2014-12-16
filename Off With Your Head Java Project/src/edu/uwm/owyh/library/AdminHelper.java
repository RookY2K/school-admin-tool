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
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;

public final class AdminHelper {

	private AdminHelper() {
		// prevents instantiation
	}
	
	public static boolean assignInstructor(WrapperObject<Person> instructor, WrapperObject<Section> section, boolean setOverwrite){
//		String sectionNum = (String) section.getProperty("sectionnum");
		
		removeOldInstructorFromSection(section);		
		
		assignNewInstructorToSection(instructor, section, setOverwrite);
//		if(sectionNum.matches("lec*")) assignInstructorToCourse();
		
		addSectionToInstructor(instructor, section);
		
		return true;
	}
	
	
//	private static void assignInstructorToCourse() {
//				
//	}

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
			if(checkSectionConflicts(instructor, section)) continue;
			
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
		
		if(checkSectionConflicts(ta, section)) return null;
		
		return ta;
	}

	@SuppressWarnings("unchecked")
	private static void addSectionToInstructor(
			WrapperObject<Person> instructor, WrapperObject<Section> section) {
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>)instructor.getProperty("sections");
		sections.add(section);
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("sections", sections);
		
		instructor.editObject(properties);
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
	private static void removeOldInstructorFromSection(
			WrapperObject<Section> section) {
		Map<String, Object> properties;

		WrapperObject<Person> oldInstructor = (WrapperObject<Person>)section.getProperty("instructor");
		
		if(oldInstructor == null) return;
		String sectionNum = (String)section.getProperty("sectionnum");
		List<Key> courses = null;
		if(sectionNum.matches("lec*")){
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

	@SuppressWarnings("unchecked")
	public static boolean checkSectionConflicts(WrapperObject<Person> instructor,
			WrapperObject<Section> section) throws ParseException {
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) instructor.getProperty("sections");
		//1) Check date conflicts
		String startDate = (String)section.getProperty("startdate");
		String endDate = (String)section.getProperty("enddate");
		// A) If conflict, return true
		if(checkDateConflict(startDate, endDate, sections)) return true;

		// B) else, continue check
		//2) check day conflicts
		String days = (String)section.getProperty("days");
		// A) If conflict, return true
		if(checkDaysConflict(days, sections)) return true;
		// B) else, continue check
		//3) return timeConflicts
		String startTime = (String)section.getProperty("starttime");
		String endTime = (String)section.getProperty("endtime");
		
		return checkTimeConflict(startTime, endTime, sections);
	}

	private static boolean checkTimeConflict(String startTime, String endTime,
			List<WrapperObject<Section>> sections) {
		boolean isConflict = false;
		double compStart = StringHelper.parseTimeToDouble(startTime);
		double compEnd = StringHelper.parseTimeToDouble(endTime);
		
		for(WrapperObject<Section> section : sections){
			if(isConflict) break;
			
			double start = 
					StringHelper.parseTimeToDouble((String)section.getProperty("starttime"));
			double end =
					StringHelper.parseTimeToDouble((String)section.getProperty("endtime"));
			
			isConflict = (start >= compStart && start < compEnd) ||
					     (end >= compStart && end < compEnd);
		}
		
		return isConflict;
	}

	private static boolean checkDaysConflict(String days,
			List<WrapperObject<Section>> sections) {
		boolean isConflict = false;
		char[] aDays = days.toCharArray();
		
		for(WrapperObject<Section> section : sections){
			if(isConflict) break;
			String compDays = (String)section.getProperty("days"); 
			for(int i=0; i<aDays.length; ++i){
				isConflict = compDays.indexOf(aDays[i]) > -1;
				if(isConflict) break;
			}			
		}		
		return isConflict;
	}

	private static boolean checkDateConflict(String startDate, String endDate,
			List<WrapperObject<Section>> sections) throws ParseException {
		Date compStart = StringHelper.stringToDate(startDate);
		Date compEnd = StringHelper.stringToDate(endDate);
		boolean isConflict = false;
		
		for(WrapperObject<Section> section : sections){
			if(isConflict)break;
			Date start =
					StringHelper.stringToDate((String)section.getProperty("startdate"));
			Date end =
					StringHelper.stringToDate((String)section.getProperty("enddate"));
			
			isConflict = (!compStart.before(start) && !compStart.after(end)) ||
					     (!compEnd.before(start) && !compEnd.after(end));
			
		}
		return isConflict;
	}
	
	

}
