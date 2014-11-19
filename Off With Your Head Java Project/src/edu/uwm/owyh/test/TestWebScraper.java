package edu.uwm.owyh.test;

import static org.junit.Assert.*;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uwm.owyh.library.WebScraper;

public class TestWebScraper {
	private String baseUri;
	private String targetUrl;
	private String html;
	
	@Before
	public void setUp(){
		
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void testGetPage(){
		Document htmlPage = null;
		try{
			htmlPage = WebScraper.getPage(targetUrl);
		}catch(Exception e){
			fail("Exception should have been caught in method and null returned!");
		}
		
		assertFalse(htmlPage == null);
		assertTrue(htmlPage.hasText());
	}
	
	public void testFindLink(){
		String target = ""; //TODO fill in target, searchStr, and html string values
		String searchStr = "";
		Document doc = Jsoup.parse(html);
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
		
		Document doc = Jsoup.parse(html);
		
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
