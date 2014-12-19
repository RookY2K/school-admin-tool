package edu.uwm.owyh.library;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;

public final class EmailHelper {

	private EmailHelper() {
	
	}
	
	/**
	 * Email Utility to get all user's email who is missing contact information
	 * @return List<String> - list of email
	 */
	public static List<String> getUsersWithMissingContactInfo(){
		List<WrapperObject<Person>> users = WrapperObjectFactory.getPerson().getAllObjects();
		List<String> emails = new ArrayList<String>();

		for(WrapperObject<Person> user : users){
			String email = checkContactInfo(user);
			if(email != null)emails.add(email);
		}		
		
		return emails;
	}
	
	/**
	 * Email Utility to get TA who has no TAClass
	 * @return List<String> - list of email
	 */
	public static List<String> getTAsWithMissingClassSchedules(){
		String filter = "accessLevel == " + AccessLevel.TA.getVal();
		List<WrapperObject<Person>> tas = WrapperObjectFactory.getPerson().findObjects(filter, null, null);
		List<String> emails = new ArrayList<String>();
		
		for(WrapperObject<Person> ta : tas){
			String email = checkClassSchedules(ta);
			if(email != null) emails.add(email);
		}
		
		return emails;
	}
	
	/**
	 * Email Utility to get all user's who have no OFfice Hours
	 * @return List<String> - list of email
	 */
	public static List<String> getUsersWithMissingOfficeHours(){
		List<WrapperObject<Person>> users = WrapperObjectFactory.getPerson().getAllObjects();
		List<String> emails = new ArrayList<String>();
		
		for(WrapperObject<Person> user : users){
			String email = checkOfficeHours(user);
			if(email != null) emails.add(email);
		}
		
		return emails;
	}
	
	/**
	 * Email Utility to get all instructor who have unassigned TA in courses they lecture
	 * @return List<String> - list of email
	 */
	public static List<String> getLectureInstructorsWithoutTAsAssigned(){
		List<WrapperObject<Person>> users = WrapperObjectFactory.getPerson().getAllObjects();
		List<String> emails = new ArrayList<String>();
		
		for(WrapperObject<Person> user : users){
			String email = checkTAsAssigned(user);
			if(email != null) emails.add(email);
		}
		
		return emails;
	}

	private static String checkContactInfo(WrapperObject<Person> user) {
		String phone  = (String) user.getProperty("phone");
		String streetAddress = (String) user.getProperty("streetaddress");
		String city = (String) user.getProperty("city");
		String state = (String) user.getProperty("state");
		String zip = (String) user.getProperty("zip");
		
		boolean missingPhone = phone == null || phone.isEmpty();
		boolean missingStreet = streetAddress == null || streetAddress.isEmpty();
		boolean missingCity = city == null || city.isEmpty();
		boolean missingState = state == null || state.isEmpty();
		boolean missingZip = zip == null || zip.isEmpty();
		
		if(missingPhone || missingStreet || missingCity || missingState || missingZip)
			return (String) user.getProperty("username");
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private static String checkClassSchedules(WrapperObject<Person> ta) {
		List<WrapperObject<TAClass>> classes = (List<WrapperObject<TAClass>>) ta.getProperty("taclasses");
		
		if(classes == null || classes.isEmpty()) 
			return (String) ta.getProperty("username");
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private static String checkOfficeHours(WrapperObject<Person> user) {
		List<WrapperObject<OfficeHours>> officeHours = 
				(List<WrapperObject<OfficeHours>>) user.getProperty("officehours");
		
		if(officeHours == null || officeHours.isEmpty()) 
			return (String) user.getProperty("username");
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private static String checkTAsAssigned(WrapperObject<Person> user) {
		List<Key> lectureCourses = (List<Key>) user.getProperty("lecturecourses");
		boolean missingTA = false;
		
		if(lectureCourses == null || lectureCourses.isEmpty())
			return null;
		
		for(Key lectureCourseKey : lectureCourses){
			if(missingTA) break;
			missingTA = checkMissingTA(lectureCourseKey);
		}
		
		if(missingTA) return (String) user.getProperty("username");
		return null;
	}

	@SuppressWarnings("unchecked")
	private static boolean checkMissingTA(Key lectureCourseKey) {
		boolean missingTA = false;
		
		WrapperObject<Course> course = 
				WrapperObjectFactory.getCourse().findObjectById(lectureCourseKey);
		
		String filter = "sectionType != 'LEC'";
		
		List<WrapperObject<Section>> sections = WrapperObjectFactory.getSection().findObjects(filter, course, null);
		
		for(WrapperObject<Section> section : sections){
			if(missingTA) break;
			WrapperObject<Person> ta = (WrapperObject<Person>) section.getProperty("instructor");
			
			if(ta == null) missingTA = true;
		}
		return missingTA;
	}

}
