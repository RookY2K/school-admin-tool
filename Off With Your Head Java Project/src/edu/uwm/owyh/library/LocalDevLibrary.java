package edu.uwm.owyh.library;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.utils.SystemProperty;

import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.model.DataStore;

public class LocalDevLibrary {
	
	private LocalDevLibrary(){
		
	}
	
	public static boolean isLocal(){
		boolean isLocal = false;
		if(SystemProperty.environment.value() == SystemProperty.Environment.Value.Development){
			isLocal = true;	
		}
		
		return isLocal;		
	}
	
	public static void readCoursePageIntoLocalDataStore(String fullFilePath)
			throws IOException, FileNotFoundException {
		List<Course> courseList = new ArrayList<Course>();
		try(BufferedReader reader = new BufferedReader(new FileReader(fullFilePath))){
			Course course = null;
			String line = reader.readLine();
			
			while(line != null){
				course = parseLineToCourseOrSectionJdo(line, course);
				if(!courseList.contains(course)) courseList.add(course);					
				line = reader.readLine();
			}
		}
		DataStore.getDataStore().insertEntities(courseList);
	}
	
	private static Course parseLineToCourseOrSectionJdo(String line, Course course){
		String[] split = line.split(";");
		if(split[0].equals("COURSE")){
			course = setCourseJdo(split);
		}else{
			if(split.length == 7){ 
				Section section = setSectionJdo(split, course);
				course.addSection(section);
			}
		}
		return course;
	}
	
	private static Course setCourseJdo(String[] courseInfo){
		String courseNum = courseInfo[1];
		String courseName = courseInfo[2];
		Course course = Course.getCourse(courseNum);
		course.setCourseName(courseName);
		
		return course;
	}
	
	private static Section setSectionJdo(String[] sectionInfo, Course course){
		String credits = sectionInfo[0];
		String dates = sectionInfo[1];
		String days = sectionInfo[2];
		String hours = sectionInfo[3];
		String instructor = sectionInfo[4];
		String room = sectionInfo[5];
		String sectionNum = sectionInfo[6];
		Section section = Section.getSection(sectionNum, course);
		section.setCredits(credits);
		section.setDates(dates);
		section.setDays(days);
		section.setHours(hours);
		section.setInstructorName(instructor);
		section.setRoom(room);

		return section;
	}

}
