package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.uwm.owyh.library.WebScraper;

public class TestWebScraper {
	private static final String BASE_URI = "http://1-dot-owyh14.appspot.com";
	private HtmlPage _testPage;
	
	/*
	 * connectToPage(String)
	 * closeConnection()
	 * findFirstByXPath(HtmlPage, String)
	 * findByXPath(HtmlPage, String)
	 * findAndClickAnchor(HtmlPage, String)
	 * ClickSpanAndGetResultingPage(HtmlPage, String)
	 */
	
	@Before
	public void setUp() throws IOException{
		String adminName = "vamaiuri@uwm.edu";
		String pw = "123";
		HtmlInput login = null;
		HtmlForm form = null;
		HtmlInput userName = null;
		HtmlInput password = null;
		_testPage = WebScraper.connectToPage(BASE_URI);
		if(_testPage.getTitleText().equalsIgnoreCase("initial login")){
			fail("No Admin set up for testPage!");
		}else if(!_testPage.getTitleText().equalsIgnoreCase("login")){
			fail("Unexpected Index page for testPage!");
		}else{
			form = _testPage.getFirstByXPath("//form[@id = 'login']");
			login = form.getFirstByXPath(".//input[@id = 'login_button']");
			userName = form.getInputByName("username");
			password = form.getInputByName("password");
			
			userName.setValueAttribute(adminName);
			password.setValueAttribute(pw);
			
			_testPage = login.click();
		}
		
		
	}
	
	@After
	public void tearDown() throws IOException{
		String logoutPath = "//a[@href = '/login?login=logout']";
		
		WebScraper.findAndClickAnchor(_testPage, logoutPath);
		WebScraper.closeConnection();
	}
	
	@Test
	public void testConnect(){
		HtmlPage page = null;
//		String notASite = "http://www.probablynotasite.com";
//		try{
//			page = WebScraper.connectToPage(notASite);
//		}catch(Exception e){
//			fail("Exception should have been caught in method and null returned!");
//		}
//		assertTrue(page == null);
		
		page = WebScraper.connectToPage("http://www.google.com");
		assertFalse(page == null);
		assertEquals("Google",page.getTitleText());
	}
	
	@Test
	public void TestNavigateToNewPageFromOldPage() throws IOException{
		String xPath = "//a[@href = '/userlist']";
		
		assertEquals("Admin", _testPage.getTitleText());
		
		_testPage = WebScraper.findAndClickAnchor(_testPage, xPath);
				
		assertFalse("Return should never be null",_testPage == null);
		
		assertEquals("User List",_testPage.getTitleText());
	}
	
	@Test
	public void TestFindElementOnPage(){
		String xPath = "//a[@href = '/userlist']";
		
		HtmlAnchor link = (HtmlAnchor) WebScraper.findFirstByXPath(_testPage, xPath);
		
		assertFalse("No link was returned!",link == null);
		assertEquals("Users", link.asText());
	}
}
