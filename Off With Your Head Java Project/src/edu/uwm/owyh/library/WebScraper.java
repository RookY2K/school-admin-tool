package edu.uwm.owyh.library;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import edu.uwm.owyh.mockjdo.Course;
import edu.uwm.owyh.mockjdo.Section;

public class WebScraper {
	private List<Course> _courses;

	public static Document getPage(String targetUrl) {
		// TODO Auto-generated method stub
		return null;
	}
	
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
		String term = "fall";
		String baseUri = "http://www4.uwm.edu/schedule/";
		Document doc = Jsoup.connect(baseUri).get();
		
		Elements links = doc.getElementsByAttributeValueContaining("href", "term_descr=" + term);
		String base = doc.baseUri();
		String fullLink = links.get(0).absUrl("href");
		System.out.println(base);

		System.out.println(fullLink);
		System.out.println("I'm done!");
		
		doc = Jsoup.connect(fullLink).get();
		
		links = doc.getElementsByAttributeValueContaining("href","subject=compsci");
		fullLink = links.get(0).absUrl("href");
		System.out.println(fullLink);
		
//		doc = Jsoup.connect(fullLink).get();
		HtmlPage page = WEBCLIENT.getPage(fullLink);
		
//		System.out.println(page.asText());
		
		HtmlSpan target = page.getFirstByXPath("//span[@id = 'toggle_all_compsci']");
		System.out.println(target.asText());
		
		page = target.click();
//		synchronized (page) {
//			page.wait(30000);
//		}
		
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		
		while(manager.getJobCount() > 0){
			Thread.sleep(1000);
		}
		
//		List<HtmlDivision> divs = (List<HtmlDivision>)page.getByXPath("//div[@class = 'fake_link']");
		
//		System.out.println("htmlunit count = " + divs.size());
//		List<HtmlTableRow> rows = (List<HtmlTableRow>)page.getByXPath("//tr[@class = 'body copy bold white']");
//		for(int i=0; i<divs.size(); ++i){
//			DomNode table = rows.get(i).getParentNode().getParentNode();
//			System.out.println(table.asXml());
//			
//			System.out.println(div.asText());
//			System.out.println(divs.get(i).asXml());
//			DomNode row = rows.get(i).getNextSibling();
//			
//			System.out.println(row.getTextContent());
			
//			System.out.println(row.asText());
			
//		}
		
		doc = Jsoup.parse(page.asXml());
		
		WEBCLIENT.closeAllWindows();
		
		Elements courses = doc.getElementsByAttributeValue("class", "fake_link");
		Elements courseTables = new Elements();
		Elements tableAnchorRows = doc.getElementsByAttributeValue("class", "body copy bold white");
		
		for(Element tableAnchorRow : tableAnchorRows){
			courseTables.add(tableAnchorRow.parent().parent());
		}
		
		for(int i=0; i<courseTables.size(); ++i){
			Course course = new Course();
			Section section = new Section();
			course.setSection(section);
			String courseInfo = courses.get(i).text();
			//TODO Do we need both COMPSCI and number? Or is number enough?
			course.setCourseNum(courseInfo.substring(0, 7) + "-" + courseInfo.substring(8, 11));
			
			int startIndex = courseInfo.indexOf(":") + 1;
			int endIndex = courseInfo.indexOf("(", startIndex);
			
			course.setCourseName(courseInfo.substring(startIndex,endIndex).trim());
			//TODO there is more than one section per course
			Element sectionInfo = courseTables.get(i).getElementsByAttributeValue("class", "body copy").get(0);
			Iterator<Element> it = sectionInfo.children().iterator();
			
			while(it.hasNext()){
				System.out.println(it.next().text());
			}
		}
		
//		System.out.println("Course count = " + courseTables.size());
		
		
		
//		System.out.println("jsoup count = " + courses.size());
		
//		for(Element course : courses){
//			System.out.println(course.toString());
//		}
		
		
		
		
//		Elements divs = doc.getElementsByAttributeValueContaining("div", "")
		
//		String Xml = page.asXml();
		
		
		
//		doc = Jsoup.parse(Xml);
		
//		System.out.println(page.asXml());
		
		
//		links = doc.getElementsByAttributeValueContaining("value","Printer-Friendly Version");
//
//		fullLink = links.get(0).parent().absUrl("action");
//		System.out.println(fullLink);
		
//		doc = Jsoup.connect(fullLink).post();
		
//		System.out.println(doc.toString());
		
		
	}


	public static List<String> findLinks(Document doc, String target) {
		// TODO Auto-generated method stub
		return null;
	}
}
