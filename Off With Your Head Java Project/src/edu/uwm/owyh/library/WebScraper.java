package edu.uwm.owyh.library;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScraper {
	private static final String HOME_URL = "http://www4.uwm.edu/schedule";
	
	public static Document getSiteDoc() throws IOException{
		return Jsoup.connect(HOME_URL).get();
	}
	
	
	public static void main(String[] args) throws IOException{
		
	}
			
}
