package edu.uwm.owyh.library;

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;

public final class AdminHelper {

	private AdminHelper() {
		// prevents instantiation
	}
	
	public static boolean assignInstructor(WrapperObject<Person> instructor, WrapperObject<Section> section, boolean setOverwrite){
		//TODO write test
		//1) Check conflict with Instructors other sections (throw exception)
		if(checkSectionConflicts(instructor, section)) return false;
		
		//2) Remove section from old instructors section list		
		
		removeOldInstructorFromSection(section);
		
		//3) Assign instructor to section if no conflicts
		
		assignNewInstructorToSection(instructor, section);

		//4) Assign section to instructor section list
		
		addSectionToInstructor(instructor, section);
		
		//5) return true;
		
		return true;
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
			WrapperObject<Person> instructor, WrapperObject<Section> section) {
		
		String instructorFirstName = (String)instructor.getProperty("firstname");
		String instructorLastName = (String)instructor.getProperty("lastname");
		
		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("instructorfirstname", instructorFirstName
										 								 ,"instructorlastname", instructorLastName
										 								 ,"instructor", instructor);
		
		section.editObject(properties);
	}

	@SuppressWarnings("unchecked")
	private static void removeOldInstructorFromSection(
			WrapperObject<Section> section) {
		Map<String, Object> properties;

		WrapperObject<Person> oldInstructor = (WrapperObject<Person>)section.getProperty("instructor");
		
		if(oldInstructor == null) return; 
	
		List<WrapperObject<Section>> oldInstructorSectionList = (List<WrapperObject<Section>>) oldInstructor.getProperty("sections");
		oldInstructorSectionList.remove(section);
		properties = PropertyHelper.propertyMapBuilder("sections", oldInstructorSectionList);
		oldInstructor.editObject(properties);
	}

	@SuppressWarnings("unchecked")
	public static boolean checkSectionConflicts(WrapperObject<Person> instructor,
			WrapperObject<Section> section) {
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) instructor.getProperty("sections");
		// TODO Auto-generated method stub
		//1) Check date conflicts
		String startDate = (String)section.getProperty("startdate");
		String endDate = (String)section.getProperty("enddate");
		// A) If no conflict, return true
		if(checkDateConflict(startDate, endDate, sections)) return true;

		// B) else, continue check
		//2) check day conflicts
		String days = (String)section.getProperty("days");
		// A) If no conflict, return true
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
		double newStart = StringHelper.parseTimeToDouble(startTime);
		double newEnd = StringHelper.parseTimeToDouble(endTime);
		
		
		
		
		
		
		return isConflict;
	}

	private static boolean checkDaysConflict(String days,
			List<WrapperObject<Section>> sections) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean checkDateConflict(String startDate, String endDate,
			List<WrapperObject<Section>> sections) {
		// TODO Auto-generated method stub
		return false;
	}

}
