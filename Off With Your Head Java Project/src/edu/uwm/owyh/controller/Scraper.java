package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import edu.uwm.owyh.exceptions.BuildJDOException;
import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.library.LocalDevLibrary;
import edu.uwm.owyh.library.NonPersistedWrapperObject;
import edu.uwm.owyh.library.WebScraper;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class Scraper extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)) return;

		if(LocalDevLibrary.isLocal()){
			request.getRequestDispatcher("/admin/localscraper").forward(request, response);
			return;
		}

		List<WrapperObject<Course>> courseList = null;	
		List<String> errors = new ArrayList<String>();
		int retries = 3;
		while(retries > 0){
			try {
				courseList = getCourseList();
				retries = 0;
			} catch (InterruptedException e) {
				retries--;

				if(retries == 0){
					errors.add("Course scraping was interrupted unexpectedly."
							+ " Maximum retries reached. Please try again!");
				}
			} catch (NumberFormatException nfe) {
				errors.add(nfe.getMessage());
				retries = 0;
			} catch(BuildJDOException bje){
				throw new IllegalArgumentException(bje.getLocalizedMessage());
			} catch(Exception e){
				throw e;
			}
		}
		if(courseList == null){
			errors.add("Error connecting to uwm website! Please try again later.");
		}
		if(errors.isEmpty()){			
			NonPersistedWrapperObject<Course> course = (NonPersistedWrapperObject<Course>) WrapperObjectFactory.getCourse();
			course.saveAllObjects(courseList);			
			courseList = WrapperObjectFactory.getCourse().getAllObjects();
			request.setAttribute("courselist", courseList);
		}else{
			request.setAttribute("errors", errors);
		}

		request.getRequestDispatcher("/classlist").forward(request, response);
	}

	@SuppressWarnings("unchecked")
	private static List<WrapperObject<Course>> getCourseList() throws InterruptedException, IOException, BuildJDOException{
		List<WrapperObject<Course>> courses = null;
		String term = "Fall";
		String baseUri = "http://www4.uwm.edu/schedule/";
		String findSpecificTermLink = "//a[@class = 'term_link' and contains(@href,'" + term + "')]";
		String findCompSciLink = "//a[contains(@href, 'COMPSCI')]";
		String findCourseHeader = "//span[@class = 'subhead']";
		String findCourseTables = "//table[@cellpadding = '2']";
		String findSectionRows = ".//tr[@class='body copy']";

		HtmlPage page = WebScraper.connectToPage(baseUri);	
		if(page == null) return null;

		page = WebScraper.findAndClickAnchor(page, findSpecificTermLink);
		page = WebScraper.findAndClickAnchor(page, findCompSciLink);

		List<HtmlSpan> courseSpans = (List<HtmlSpan>) WebScraper.findByXPath(page, findCourseHeader);
		try{
			courses = setCourseInfo(courseSpans);
		}catch(NumberFormatException nfe){
			throw new NumberFormatException(nfe.getMessage());
		}

		List<HtmlTable> courseTables = (List<HtmlTable>) WebScraper.findByXPath(page, findCourseTables);
		List<WrapperObject<Course>> tempCourses = courses; 
		if(courses.isEmpty()){
			courses = WrapperObjectFactory.getCourse().getAllObjects(); 
		}
		for(int i=0; i<courseTables.size(); ++i){
			HtmlTable courseTable = courseTables.get(i);
			
			WrapperObject<Course> course = courses.get(i);

			List<HtmlTableRow> sectionRows = (List<HtmlTableRow>)WebScraper.findByXPath(courseTable, findSectionRows);

			if(setAllSectionInfoForCourse(sectionRows, course)){
				if(!tempCourses.contains(course)){
					tempCourses.add(course);
				}
			}
		}		
		courses = tempCourses;
		return courses;
	}

	private static List<WrapperObject<Course>> setCourseInfo(List<HtmlSpan> courseSpans) throws BuildJDOException{
		List<WrapperObject<Course>> courses = new ArrayList<WrapperObject<Course>>();

		for(int i=0; i<courseSpans.size(); ++i){
			HtmlSpan courseSpan = courseSpans.get(i);

			String courseInfo = courseSpan.asText();

			int startIndex = courseInfo.indexOf('I', courseInfo.indexOf("COMPSCI")) + 2;
			int endIndex = startIndex + 3;

			String courseNum = courseInfo.substring(startIndex, endIndex);

			try{
				isParsableAsInt(courseNum);
			}catch(NumberFormatException nfe){
				throw new NumberFormatException("Parse Error! Please contact website adminstrator with this error");
			}			
			
			startIndex = courseInfo.indexOf(':') + 1;
			String courseName = courseInfo.substring(startIndex);
			
			String[] aCourseInfo = {"COURSE", courseNum, courseName};
			
			LocalDevLibrary.setCourseJdo(aCourseInfo, courses);
		}
		return courses;
	}

	private static void isParsableAsInt(String courseNum) throws NumberFormatException {
		Integer.parseInt(courseNum);		
	}

	private static boolean setAllSectionInfoForCourse(List<HtmlTableRow> sectionRows, WrapperObject<Course> parent) throws BuildJDOException{
		int creditIndex = 2;
		int sectionNumIndex = 3;
		int hoursIndex = 5;
		int daysIndex = 6;
		int datesIndex = 7;
		int instructorNameIndex = 8;
		int roomIndex = 9;
		boolean addParent = false;

		for(HtmlTableRow row : sectionRows){
			String sectionNum = row.getCell(sectionNumIndex).asText();

			if(sectionNum == null || sectionNum.trim().length() == 0) continue;

			String creditLoad = row.getCell(creditIndex).asText();
			String hours = row.getCell(hoursIndex).asText();
			String days = row.getCell(daysIndex).asText();
			String dates = row.getCell(datesIndex).asText();
			String instructorName = row.getCell(instructorNameIndex).asText();
			String room = row.getCell(roomIndex).asText();
			
			String[] sectionInfo = {creditLoad, dates, days, hours, instructorName, room, sectionNum};

			WrapperObject<Section> section = LocalDevLibrary.setSectionJdo(sectionInfo, parent);
			
			if(section != null){
				((NonPersistedWrapperObject<Course>)parent).addChild(section);
				addParent = true;
			}
		}
		
		return addParent;
	}

//	private static void setHours(String hours, Section section){
//		if(hours.trim().length() == 1) return;
//		String[] aHours = hours.split("-");
//
//		if(aHours.length != 2)
//			throw new IllegalArgumentException("Hours did not split into start and end time as expected!");
//
//		String startTime = aHours[0];
//		String endTime = aHours[1];
//
//		section.setStartTime(Library.parseTimeToDouble(startTime));
//		section.setEndTime(Library.parseTimeToDouble(endTime));
//	}
//
//	private static void setDates(String dates, Section section){
//		String[] aDates = dates.split("-");
//
//		if(aDates.length != 2)
//			throw new IllegalArgumentException("Dates did not split into start and end date as expected!");
//
//		String startDate = aDates[0].trim();
//		String endDate = aDates[1].trim();
//
//		try{
//			section.setStartDate(Library.stringToDate(startDate));
//			section.setEndDate(Library.stringToDate(endDate));
//		}catch(ParseException pe){
//			throw new IllegalArgumentException(pe.getMessage());
//		}
//	}
//
//	private static void setInstructorName(String instructorName, Section section){
//		String lastName = "";
//		String firstName = "";
//
//		if(!instructorName.trim().isEmpty()){
//			String[] aNames = instructorName.split(",");
//
//			if(aNames.length != 2)
//				throw new IllegalArgumentException("Instructor names did not split into first and last as expected!");
//
//			lastName = aNames[0].trim();
//			firstName = aNames[1].trim();
//		}
//
//		section.setInstructorFirstName(firstName);
//		section.setInstructorLastName(lastName);
//	}
}
