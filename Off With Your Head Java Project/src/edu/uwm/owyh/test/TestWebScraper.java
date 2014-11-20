package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.uwm.owyh.library.WebScraper;

public class TestWebScraper {
	private String _baseUri;
	private String _notASite;
	
	/*
	 * connectToPage(String)
	 * closeConnection()
	 * findFirstByXPath(HtmlPage, String)
	 * findByXPath(HtmlPage, String)
	 * findAndClickAnchor(HtmlPage, String)
	 * ClickSpanAndGetResultingPage(HtmlPage, String)
	 */
	
	@Before
	public void setUp(){
		_baseUri = "http://1-dot-owyh14.appspot.com";
		_notASite = "http://www.probablynotasite.com";
	}
	
	@After
	public void tearDown(){
		WebScraper.closeConnection();
	}
	
	@Test
	public void testConnect(){
		HtmlPage page = null;
		try{
			page = WebScraper.connectToPage(_notASite);
		}catch(Exception e){
			fail("Exception should have been caught in method and null returned!");
		}
		assertTrue(page == null);
		
		page = WebScraper.connectToPage(_baseUri);
		assertFalse(page == null);
		assertEquals("Login",page.getTitleText());
	}
	
	public void testFindLink(){
		String xPath = "//a[contains(@href,'vamaiuri@uwm.edu')]";
		
		HtmlPage page = WebScraper.connectToPage(_baseUri);
		
		page = WebScraper.findAndClickAnchor(page, xPath);
		boolean isFound = false;
		
		List<String> links = WebScraper.findLinks(doc,searchStr);
		
		assertFalse("Return should never be null",links == null);
		
		for(String link : links){
			if(link.equalsIgnoreCase(target)){
				isFound = true;
				break;
			}			
		}
		assertTrue("Link, " + target + ", was not found!", isFound);
	}
	
	public void TestNavigateToNewPageFromOldPage(){
		String target = ""; //TODO fill in target, searchStr
		String searchStr = "";
		
		Document doc = Jsoup.parse(_html);
		
		List<String> links = WebScraper.findLinks(doc, searchStr);
		
		assertFalse("findLinks returned null!", links == null);
		
		String url = null;
		for(String link : links){
			if(link.equalsIgnoreCase(target)){
				url = link;
				break;
			}
		}
		
		assertFalse("Link wasn't found",url==null);
		
		try{
			doc = WebScraper.getPage(url);
		}catch(Exception e){
			fail("Exception should have been caught and null returned");
		}
		
		assertFalse("No Document object returned!",doc == null);
		
		assertTrue(doc.title().equalsIgnoreCase("google"));
	}
	
	public void TestFindElementOnPage(){
		
	}
	
	public void TestGetAttributeFromElement(){
		
	}
	
	public void TestGetInfoFromAttribute(){
		
	}
}
