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

import edu.uwm.owyh.library.WebScraper;
import edu.uwm.owyh.mockjdo.Course;
import edu.uwm.owyh.mockjdo.Section;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class ClassList extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyUser(response)) return;
		
		List<Course> courses = null;	
		List<String> errors = new ArrayList<String>();
		int retries = 3;
		while(retries > 0){
			try {
				courses = getCourseList();
				retries = 0;
			} catch (InterruptedException e) {
				retries--;

				if(retries == 0){
					errors.add("Course scraping was interrupted unexpectedly."
							+ " Maximum retries reached. Please try again!");
				}
			} catch(Exception e){
				errors.add(e.getMessage());
				retries = 0;
			}
		}
		if(courses == null){
			errors.add("Error connecting to uwm website! Please try again later.");
		}
		if(errors.isEmpty()){
			request.setAttribute("courses", courses);
		}else{
			request.setAttribute("errors", errors);
		}
		
		request.getRequestDispatcher(request.getContextPath() + "/classlist.jsp").forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request,response);		
	}

	@SuppressWarnings("unchecked")
	private static List<Course> getCourseList() throws IOException, InterruptedException{
		List<Course> courses = null;
		String term = "Fall";
		String baseUri = "http://www4.uwm.edu/schedule/";
		String findSpecificTermLink = "//a[@class = 'term_link' and contains(@href,'" + term + "')]";
		String findCompSciLink = "//a[contains(@href, 'COMPSCI')]";
		String findToggleAllSpan = "//span[@id = 'toggle_all_compsci']";
		String findCourseHeader = "//span[@class = 'subhead']";
		String findCourseTables = "//table[@cellpadding = '2']";
		String findSectionRows = ".//tr[@class='body copy']";

		HtmlPage page = WebScraper.connectToPage(baseUri);	
		if(page == null) return null;
		page = WebScraper.findAndClickAnchor(page, findSpecificTermLink);
		page = WebScraper.findAndClickAnchor(page, findCompSciLink);
		page = WebScraper.ClickSpanAndGetResultingPage(page, findToggleAllSpan);

		List<HtmlSpan> courseSpans = (List<HtmlSpan>) WebScraper.findByXPath(page, findCourseHeader);
		courses = 	setCourseInfo(courseSpans);

		List<HtmlTable> courseTables = (List<HtmlTable>) WebScraper.findByXPath(page, findCourseTables);

		for(int i=0; i<courseTables.size(); ++i){
			HtmlTable courseTable = courseTables.get(i);
			Course course = courses.get(i);

			List<HtmlTableRow> sectionRows = (List<HtmlTableRow>)WebScraper.findByXPath(courseTable, findSectionRows);

			course.setSection(setAllSectionInfoForCourse(sectionRows));
		}		
		return courses;
	}

	public static List<Course> setCourseInfo(List<HtmlSpan> courseSpans){
		List<Course> courses = new ArrayList<Course>();

		for(int i=0; i<courseSpans.size(); ++i){

			HtmlSpan courseSpan = courseSpans.get(i);
			Course course = new Course();
			String courseInfo = courseSpan.asText();

			int startIndex = 8;
			int endIndex = 11;

			String courseNum = "COMPSCI-" + courseInfo.substring(startIndex, endIndex);
			course.setCourseNum(courseNum);

			startIndex = courseInfo.indexOf(':') + 1;

			String courseName = courseInfo.substring(startIndex);
			course.setCourseName(courseName);

			courses.add(course);
		}
		return courses;
	}

	public static List<Section> setAllSectionInfoForCourse(List<HtmlTableRow> sectionRows){
		List<Section> sections = new ArrayList<Section>();
		int creditIndex = 2;
		int sectionNumIndex = 3;
		int hoursIndex = 5;
		int daysIndex = 6;
		int datesIndex = 7;
		int instructorNameIndex = 8;
		int roomIndex = 9;

		for(HtmlTableRow row : sectionRows){
			Section section = new Section();
			String creditLoad = row.getCell(creditIndex).asText();
			String sectionNum = row.getCell(sectionNumIndex).asText();
			String hours = row.getCell(hoursIndex).asText();
			String days = row.getCell(daysIndex).asText();
			String dates = row.getCell(datesIndex).asText();
			String instructorName = row.getCell(instructorNameIndex).asText();
			String room = row.getCell(roomIndex).asText();

			section.setCreditLoad(creditLoad);
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
