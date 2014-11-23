package edu.uwm.owyh.model;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.library.Library;

public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	
	private Auth(HttpServletRequest request){
		WrapperObject<Person> user = (WrapperObject<Person>)getSessionVariable(request, "user");
		if (user == null) return;
		_goodUserName = (String) user.getProperty("username");
		_goodAccess = (AccessLevel) user.getProperty("accesslevel");
	}
	
	private Auth(){
		_goodAccess = null;
		_goodUserName = null;
	}	
	
	public static Object getSessionVariable(HttpServletRequest request, String key){
		if(request == null || key == null) return null;
		
		HttpSession session = request.getSession();
		
		return session.getAttribute(key);
	}
	
	public static void setSessionVariable(HttpServletRequest request, String key, Object attribute){
		if(request == null || key == null) return;
		
		HttpSession session = request.getSession();
		
		session.setAttribute(key, attribute);
	}
	
	public static void removeSessionVariable(HttpServletRequest request, String key){
		if(request == null || key == null) return;
		
		HttpSession session = request.getSession();
		
		session.removeAttribute(key);
	}
	
	public WrapperObject<Person> verifyLogin(String userName, String password){
		if(userName == null || password == null)return null;
				
		WrapperObject<Person> client = WrapperObjectFactory.getPerson();
		Key id = Library.generateIdFromUserName(userName);
		WrapperObject<Person> user = client.findObjectById(id);
		
		if(user == null) return null;
		
		/* TODO: user password should never be null. and yet it was, so temporary fix */
		if (user.getProperty("password") == null) return null;
		
		if(!user.getProperty("password").equals(password)) return null;
		
		
		return user;
	}
	
	public boolean verifyUser() {
		return (_goodUserName != null);
	}
	
	public boolean verifyAdmin() {
		if ((_goodUserName == null)) return false;
		return (_goodAccess == AccessLevel.ADMIN);
	}
	
	public boolean verifyUser(HttpServletResponse response) throws IOException {
		if (_goodUserName == null) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
	
	public boolean verifyAdmin(HttpServletResponse response) throws IOException {
		if (_goodUserName == null || _goodAccess != AccessLevel.ADMIN) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static void destroySession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Enumeration<String> attributeNames = session.getAttributeNames();
		
		while(attributeNames.hasMoreElements()){
			removeSessionVariable(request, attributeNames.nextElement());
		}
	}
	
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
}
