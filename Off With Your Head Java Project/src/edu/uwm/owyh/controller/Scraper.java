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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.library.LocalDevLibrary;
import edu.uwm.owyh.library.WebScraper;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;

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

		List<Course> courseList = null;	
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
			} catch(Exception e){
				errors.add(e.getMessage());
				retries = 0;
			}
		}
		if(courseList == null){
			errors.add("Error connecting to uwm website! Please try again later.");
		}
		if(errors.isEmpty()){			
			request.setAttribute("courselist", courseList);
			DataStore.getDataStore().insertEntities(courseList);
		}else{
			request.setAttribute("errors", errors);
		}

		request.getRequestDispatcher("/classlist").forward(request, response);
	}
	
	@SuppressWarnings("unchecked")
	private static List<Course> getCourseList() throws InterruptedException, IOException{
		List<Course> courses = null;
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

		for(int i=0; i<courseTables.size(); ++i){
			HtmlTable courseTable = courseTables.get(i);
			Course course = courses.get(i);

			List<HtmlTableRow> sectionRows = (List<HtmlTableRow>)WebScraper.findByXPath(courseTable, findSectionRows);

			course.setSections(setAllSectionInfoForCourse(sectionRows, course));
		}		
		return courses;
	}

	private static List<Course> setCourseInfo(List<HtmlSpan> courseSpans){
		List<Course> courses = new ArrayList<Course>();

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

			Course course = Course.getCourse(courseNum);
			startIndex = courseInfo.indexOf(':') + 1;

			String courseName = courseInfo.substring(startIndex);
			course.setCourseName(courseName);

			courses.add(course);
		}
		return courses;
	}

	private static void isParsableAsInt(String courseNum) throws NumberFormatException {
		Integer.parseInt(courseNum);		
	}

	private static List<Section> setAllSectionInfoForCourse(List<HtmlTableRow> sectionRows, Course parent){
		List<Section> sections = new ArrayList<Section>();
		int creditIndex = 2;
		int sectionNumIndex = 3;
		int hoursIndex = 5;
		int daysIndex = 6;
		int datesIndex = 7;
		int instructorNameIndex = 8;
		int roomIndex = 9;

		for(HtmlTableRow row : sectionRows){
			String sectionNum = row.getCell(sectionNumIndex).asText();
			
			if(sectionNum == null || sectionNum.trim().length() == 0) continue;
			
			String creditLoad = row.getCell(creditIndex).asText();
			String hours = row.getCell(hoursIndex).asText();
			String days = row.getCell(daysIndex).asText();
			String dates = row.getCell(datesIndex).asText();
			String instructorName = row.getCell(instructorNameIndex).asText();
			String room = row.getCell(roomIndex).asText();
			Section section = Section.getSection(sectionNum, parent);

			section.setCredits(creditLoad);
			section.setSectionNum(sectionNum);
			section.setHours(hours);
			section.setDays(days);
			section.setDates(dates);
			section.setInstructorName(instructorName);
			section.setRoom(room);
			sections.add(section);				
		}		
		return sections;
	}
	
	
}
