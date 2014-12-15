package edu.uwm.owyh.library;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.utils.SystemProperty;

import edu.uwm.owyh.exceptions.BuildJDOException;
import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.NonPersistedWrapperObject;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Section;

public class ScrapeUtility {
	
	private ScrapeUtility(){
		
	}
	
	public static boolean isLocal(){
		boolean isLocal = false;
		if(SystemProperty.environment.value() == SystemProperty.Environment.Value.Development){
			isLocal = true;	
		}
		
		return isLocal;		
	}
	
	public static void readCoursePageIntoLocalDataStore(String fullFilePath)
			throws IOException, FileNotFoundException, BuildJDOException {
		
		List<WrapperObject<Course>> courseList = new ArrayList<WrapperObject<Course>>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(fullFilePath))){
			NonPersistedWrapperObject<Course> course = null;
			String line = reader.readLine();
			
			while(line != null){
				course = (NonPersistedWrapperObject<Course>) parseLineToCourseOrSectionJdo(line, course, courseList);	
				line = reader.readLine();
			}
			course.saveAllObjects(courseList);
		}	
	}
	
	private static WrapperObject<Course> parseLineToCourseOrSectionJdo(String line, WrapperObject<Course> course, 
			List<WrapperObject<Course>> courseList) throws BuildJDOException{
		String[] split = line.split(";");
		if(split[0].equals("COURSE")){
			course = setCourseJdo(split, courseList);
		}else{
			if(split.length == 7){ 
				WrapperObject<Section> section = setSectionJdo(split, course);
				if(section != null){
					((NonPersistedWrapperObject<Course>)course).addChild(section);
					if(!courseList.contains(course)) courseList.add(course);
				}
			}
		}
		return course;
	}
	
	public static WrapperObject<Course> setCourseJdo(String[] courseInfo, List<WrapperObject<Course>> courseList) throws BuildJDOException{
		String courseNum = courseInfo[1];
		String courseName = courseInfo[2];
		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("coursename",courseName);
		NonPersistedWrapperObject<Course> course = (NonPersistedWrapperObject<Course>) WrapperObjectFactory.getCourse();
		WrapperObject<Course> editCourse = null;
		Key courseKey = WrapperObjectFactory.generateIdFromCourseNum(courseNum);
		
		editCourse = course.findObjectById(courseKey);
		if(editCourse == null){
			course.buildObject(courseNum, properties);
			courseList.add(course);
			return course;
		}		
		else {
			editCourse.editObject(properties);
			return editCourse;
		}
	}
	
	public static WrapperObject<Section> setSectionJdo(String[] sectionInfo, WrapperObject<Course> course) throws BuildJDOException{
		String credits = sectionInfo[0];
		Map<String, String> dates = splitDates(sectionInfo[1]);
		String startDate = dates.get("startdate");
		String endDate = dates.get("enddate");
		String days = sectionInfo[2];
		Map<String, String> hours = splitHours(sectionInfo[3]);
		String startTime = hours.get("starttime");
		String endTime = hours.get("endtime");
		Map<String, String> instructorNames = splitInstructorName(sectionInfo[4]);
		String firstName = instructorNames.get("firstname");
		String lastName = instructorNames.get("lastname");
		String room = sectionInfo[5];
		String sectionNum = sectionInfo[6];
		NonPersistedWrapperObject<Section> section = (NonPersistedWrapperObject<Section>)WrapperObjectFactory.getSection();
		WrapperObject<Section> editSection = null;
		
		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("credits", credits
																  ,"startdate", startDate
																  ,"enddate", endDate
																  ,"days", days
																  ,"starttime", startTime
																  ,"endtime", endTime
																  ,"instructorfirstname", firstName
																  ,"instructorlastname", lastName
																  ,"room", room
																  ,"sectionnum", sectionNum
																  );
		
		String courseNum = (String)course.getProperty("coursenum");
		
		Key sectionKey = WrapperObjectFactory.generateSectionIdFromSectionAndCourseNum(sectionNum, courseNum);
		editSection = section.findObjectById(sectionKey);
		if(editSection == null){
			section.buildObject(courseNum, properties);
			return section;
		}
		else{
			boolean overwriteInstructor = (boolean)editSection.getProperty("overwriteinstructor");
			if(!overwriteInstructor){
				properties.remove("instructorfirstname");
				properties.remove("instructorlastname");
			}
			editSection.editObject(properties);
			return null; 
		}
	}

	private static Map<String, String> splitHours(String hours){
		Map<String, String> times = new HashMap<String, String>();
		times.put("starttime", "");
		times.put("endtime", "");
		if(hours.trim().length() == 1) return times;
		String[] aHours = hours.split("-");

		if(aHours.length != 2)
			throw new IllegalArgumentException("Hours did not split into start and end time as expected!");

		
		String startTime = aHours[0];
		String endTime = aHours[1];

		times.put("starttime", startTime);
		times.put("endtime", endTime);
		
		return times;
	}

	private static Map<String,String> splitDates(String dates){
		Map<String, String> mDates = new HashMap<String, String>();
		String[] aDates = dates.split("-");

		if(aDates.length != 2)
			throw new IllegalArgumentException("Dates did not split into start and end date as expected!");

		String startDate = aDates[0].trim();
		String endDate = aDates[1].trim();

		mDates.put("startdate", startDate);
		mDates.put("enddate", endDate);

		
		return mDates;
	}
	
	private static Map<String, String> splitInstructorName(String instructorName){
		String lastName = "";
		String firstName = "";
		
		Map<String, String> names = new HashMap<String,String>();
		
		if(!instructorName.trim().isEmpty()){
			String[] aNames = instructorName.split(",");
		
			if(aNames.length != 2)
				throw new IllegalArgumentException("Instructor names did not split into first and last as expected!");
		
			lastName = aNames[0].trim();
			firstName = aNames[1].trim();
		}
		
		names.put("firstname",firstName);
		names.put("lastname",lastName);
		
		return names;
	}
}
