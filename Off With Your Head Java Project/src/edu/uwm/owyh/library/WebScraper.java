package edu.uwm.owyh.library;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
/**
 * Utility class for Scraping HTML websites using HTMLUnit.
 * @author Vince Maiuri
 *
 */
public class WebScraper {
	private final static WebClient WEBCLIENT;
	
	static{
		WEBCLIENT = new WebClient();
		WEBCLIENT.getOptions().setJavaScriptEnabled(true);  
        WEBCLIENT.getOptions().setRedirectEnabled(true);  
        WEBCLIENT.getOptions().setCssEnabled(true);  
        WEBCLIENT.getOptions().setThrowExceptionOnFailingStatusCode(false);  
        WEBCLIENT.getOptions().setThrowExceptionOnScriptError(false);  
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
		}catch (ScriptException e){
			page = null;
		}
		return page;
	}
	
	/**
	 * Closes the connection
	 */
	public static void closeConnection(){
		WEBCLIENT.closeAllWindows();
	}
	
	/**
	 * Returns a the first DOM node found given the xPath expression
	 * @param page - page from which DOM node is being searched for
	 * @param xPathExpr - xPath expression
	 * @return DomNode being searched for (null if not found).
	 */
	public static DomNode findFirstByXPath(HtmlPage page, String xPathExpr){
		return page.getFirstByXPath(xPathExpr);
	}
	
	/**
	 * Returns a list of DOM nodes found from a given xPath expression
	 * @param page - root DOM node being searched from
	 * @param xPathExpr - xPath expression 
	 * @return List of objects found from xPathExpr
	 */
	@SuppressWarnings("unchecked")
	public static List<?> findByXPath(DomNode page, String xPathExpr){
		return (List<DomNode>) page.getByXPath(xPathExpr);
	}
	
	/**
	 * Find a specific anchor, then click that anchor. Return the resultant page
	 * @param page
	 * @param xPathExpr
	 * @return the Page navigated to
	 * @throws IOException
	 */
	public static HtmlPage findAndClickAnchor(HtmlPage page, String xPathExpr) throws IOException{
		HtmlPage newPage = null;
		
		HtmlAnchor link = (HtmlAnchor)findFirstByXPath(page, xPathExpr);
		
		newPage = link.click();
		
		return newPage;
	}
	
	/**
	 * Find and click specific span. 
	 * @param page
	 * @param xPathExpr
	 * @return Resultant page from clicking on span
	 * @throws IOException
	 * @throws InterruptedException
	 */
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
}
