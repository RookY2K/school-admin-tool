package edu.uwm.owyh.library;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import edu.uwm.owyh.mockjdo.Course;
import edu.uwm.owyh.mockjdo.Section;

public class WebScraper {
	private final static WebClient WEBCLIENT;
	
	static{
		WEBCLIENT = new WebClient();
		WEBCLIENT.getOptions().setJavaScriptEnabled(true);  
        WEBCLIENT.getOptions().setRedirectEnabled(true);  
        WEBCLIENT.getOptions().setCssEnabled(true);  
        WEBCLIENT.getOptions().setThrowExceptionOnFailingStatusCode(false);  
        WEBCLIENT.getOptions().setThrowExceptionOnScriptError(true);  
        WEBCLIENT.getOptions().setUseInsecureSSL(true);  
        WEBCLIENT.setAjaxController(new NicelyResynchronizingAjaxController());  
        WEBCLIENT.waitForBackgroundJavaScriptStartingBefore(10000);  
        WEBCLIENT.setJavaScriptTimeout(2147483647);  
        WEBCLIENT.setCssErrorHandler(new SilentCssErrorHandler());  
        WEBCLIENT.waitForBackgroundJavaScript(5000);  
        WEBCLIENT.setRefreshHandler(new ThreadedRefreshHandler());  
        WEBCLIENT.getCookieManager().setCookiesEnabled(true);  
	}
	
	/**
	 * Connects to a page given the url
	 * @param baseUrl - Url to connect to
	 * @return the HtmlPage object or null if an error occurs
	 */
	public static HtmlPage connectToPage(String baseUrl){
		HtmlPage page = null;
		try {
			page = WEBCLIENT.getPage(baseUrl);
		} catch (FailingHttpStatusCodeException e) {
			page = null;
		} catch (MalformedURLException e) {
			page = null;
		} catch (IOException e) {
			page = null;
		}
		return page;
	}
	
	public static void closeConnection(){
		WEBCLIENT.closeAllWindows();
	}
	
	public static DomNode findFirstByXPath(HtmlPage page, String xPathExpr){
		return page.getFirstByXPath(xPathExpr);
	}
	
	public static List<?> findByXPath(DomNode page, String xPathExpr){
		return page.getByXPath(xPathExpr);
	}
	
	public static HtmlPage findAndClickAnchor(HtmlPage page, String xPathExpr) throws IOException{
		HtmlPage newPage = null;
		
		HtmlAnchor link = (HtmlAnchor)findFirstByXPath(page, xPathExpr);
		
		newPage = link.click();
		
		return newPage;
	}
	
	public static HtmlPage ClickSpanAndGetResultingPage(HtmlPage page, String xPathExpr) throws IOException, InterruptedException{
		HtmlPage newPage = null;
		
		HtmlSpan link = (HtmlSpan)findFirstByXPath(page, xPathExpr);
		
		newPage = link.click();
		
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		
		while(manager.getJobCount() > 0){
			Thread.sleep(1000);
		}
		
		return newPage;
	}
	
	private static List<Course> setCourseInfo(List<HtmlSpan> courseSpans){
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
	
	private static List<Section> setAllSectionInfoForCourse(List<HtmlTableRow> sectionRows){
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
	
	/*
	 * TODO 
	 * 1) Connect to a website
	 * 2) Be able to find pertinent links
	 * 3) Navigate through links to desired page
	 * 4) Find elements of interest
	 * 5) Get attributes from elements
	 * 6) Get information from attributes
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args)throws IOException, InterruptedException{
		final long startTime = System.currentTimeMillis();
		List<Course> _courses = null;
		String term = "Fall";
		String baseUri = "http://www4.uwm.edu/schedule/";
		String findSpecificTermLink = "//a[@class = 'term_link' and contains(@href,'" + term + "')]";
		String findCompSciLink = "//a[contains(@href, 'COMPSCI')]";
		String findToggleAllSpan = "//span[@id = 'toggle_all_compsci']";
		String findCourseHeader = "//span[@class = 'subhead']";
		String findCourseTables = "//table[@cellpadding = '2']";
		String findSectionRows = ".//tr[@class='body copy']";
		
		HtmlPage page = connectToPage(baseUri);		
		
		page = findAndClickAnchor(page, findSpecificTermLink);
		page = findAndClickAnchor(page, findCompSciLink);
		page = ClickSpanAndGetResultingPage(page, findToggleAllSpan);
		
		List<HtmlSpan> courseSpans = (List<HtmlSpan>) findByXPath(page, findCourseHeader);
		_courses = 	setCourseInfo(courseSpans);
		
		List<HtmlTable> courseTables = (List<HtmlTable>) findByXPath(page, findCourseTables);
		
		for(int i=0; i<courseTables.size(); ++i){
			HtmlTable courseTable = courseTables.get(i);
			Course course = _courses.get(i);
			
			List<HtmlTableRow> sectionRows = (List<HtmlTableRow>)findByXPath(courseTable, findSectionRows);
			
			course.setSection(setAllSectionInfoForCourse(sectionRows));
		}
		
		for(Course course : _courses){
			System.out.println(course.getCourseNum() + ": " + course.getCourseName());
			System.out.println("------------------------------------------");
			List<Section> sections = course.getSections();
			
			for(Section section : sections){
				System.out.println("Section Number: " + section.getSectionNum());
				System.out.println("Credit Load: " + section.getCreditLoad());
				System.out.println("Days: " + section.getDays());
				System.out.println("Hours: " + section.getHours());
				System.out.println("Dates: " + section.getDates());
				System.out.println("Instructor: " + section.getInstructorName());
				System.out.println("Room: " + section.getRoom() + "\n");				
			}
		}
		final long endTime = System.currentTimeMillis();
		
		System.out.println("This took this long: " + (endTime - startTime));		
		
	}
}
