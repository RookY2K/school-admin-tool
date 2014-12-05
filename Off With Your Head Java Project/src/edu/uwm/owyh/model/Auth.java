package edu.uwm.owyh.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.Library;

/**
 * Class used to determine if users are authorized to view and/or interact with the 
 * Web application. Primary class to interact with, set, and remove session variables. 
 */
public class Auth {
	private AccessLevel _goodAccess;
	private String _goodUserName;
	
	//Private constructor that contructs an Auth object from the session variables
	private Auth(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		WrapperObject<Person> user = (WrapperObject<Person>)getSessionVariable(request, "user");
		if (user == null) return;
		_goodUserName = (String) user.getProperty("username");
		_goodAccess = (AccessLevel) user.getProperty("accesslevel");
	}
	
	//Private default constructor
	private Auth(){
		
	}	
	
	/**
	 * Returns the session variable (if it exists) given the property key.
	 * @param request - the HttpServletRequest object that contains the session info
	 * @param key - Key for the session variable
	 * @return the Session variable if it exists. Null otherwise.
	 */
	public static Object getSessionVariable(HttpServletRequest request, String key){
		if(request == null || key == null) return null;
		
		HttpSession session = request.getSession();
		
		return session.getAttribute(key);
	}
	
	/**
	 * Sets the session variable given the key and attribute
	 * @param request - the HttpServletRequest object that contains the session info
	 * @param key - Key for the session variable to set
	 * @param attribute - Value for the session variable to set
	 */
	public static void setSessionVariable(HttpServletRequest request, String key, Object attribute){
		if(request == null || key == null) return;
		
		HttpSession session = request.getSession();
		
		session.setAttribute(key, attribute);
	}
	
	/**
	 * Removes the session variable matching the key from the current session
	 * @param request - the HttpServletRequest object that contains the session info
	 * @param key - Key for the session variable to remove from the session
	 */
	public static void removeSessionVariable(HttpServletRequest request, String key){
		if(request == null || key == null) return;
		
		HttpSession session = request.getSession();
		
		session.removeAttribute(key);
	}
	
	/**
	 * <pre>Verifies that the inputted username and password matches a legitimate
	 * user in the datastore. This is used for login validation. If user is valid,
	 * then WrapperObject<Person> user is returned (primarily for putting into the session
	 * if the client so desires). </pre>
	 * @param userName
	 * @param password
	 * @return the WrapperObject<Person>
	 */
	public WrapperObject<Person> verifyLogin(String userName, String password){
		if(userName == null || password == null)return null;
				
		WrapperObject<Person> client = WrapperObjectFactory.getPerson();
		Key id = Library.generateIdFromUserName(userName);
		WrapperObject<Person> user = client.findObjectById(id);
		
		if(user == null) return null;
		
		if (user.getProperty("password") == null) return null;
		
		if(!user.getProperty("password").equals(password)) return null;
		
		
		return user;
	}
	
	/**
	 * <pre> Verifies a Login To a temporary Password requested from Forgot Password, If verified,
	 * replaces regular password with temporary Password. </pre>
	 * @param userName
	 * @param password
	 * @return the WrapperObject<Person>
	 */
	public WrapperObject<Person> verifyTempLogin(String userName, String password){
		if(userName == null || password == null)return null;
				
		WrapperObject<Person> client = WrapperObjectFactory.getPerson();
		Key id = Library.generateIdFromUserName(userName);
		WrapperObject<Person> user = client.findObjectById(id);
		
		if(user == null) return null;
		
		if (user.getProperty("temporarypassword") == null) return null;
		
		if(!user.getProperty("temporarypassword").equals(password)) return null;
		
		Map<String, Object> properties = Library.propertyMapBuilder("password", password, "temporarypassword", "");
		List<String >errors = WrapperObjectFactory.getPerson().editObject(userName, properties);
		
		if (!errors.isEmpty()) return null;
		
		return user;
	}
	
	/**
	 * <pre>Verifies that the username for the Auth object isn't null
	 * Would have been previously set through one of the constructors.
	 * This verifies that a user is logged in, but not which user.</pre>
	 * @return true is a user is logged in.
	 */
	public boolean verifyUser() {
		return (_goodUserName != null);
	}
	
	/**
	 * <pre>Verifies that the username for the Auth object isn't null
	 * and that the access level is set to Admin. This verifies that 
	 * an admin user is logged in, but not which admin.</pre>
	 * @return true is an admin is logged in.
	 */
	public boolean verifyAdmin() {
		if ((_goodUserName == null)) return false;
		return (_goodAccess == AccessLevel.ADMIN);
	}
	
	/**
	 * <pre>Verifies that the username for the Auth object isn't null
	 * This verifies that a user is logged in, but not which user.
	 * Sets the response object to redirect to "/" if a user is not logged in. Won't
	 * redirect until calling location "returns".</pre>
	 * @return true is a user is logged in.
	 */
	public boolean verifyUser(HttpServletResponse response) throws IOException {
		if (_goodUserName == null) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
	
	/**
	 * <pre>Verifies that the username for the Auth object isn't null
	 * and that the access level is set to Admin. This verifies that 
	 * an admin user is logged in, but not which admin.
	 * Sets the response object to redirect to "/" if an admin is not logged in. Won't
	 * redirect until calling location "returns".</pre>
	 * @return true is an admin is logged in.
	 */
	public boolean verifyAdmin(HttpServletResponse response) throws IOException {
		if (_goodUserName == null || _goodAccess != AccessLevel.ADMIN) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
	
	/**
	 * Invalidates the current session which as a side-effect, destroys
	 * the session variables.
	 * @param request
	 */
	public static void destroySession(HttpServletRequest request) {
		HttpSession session = request.getSession();

		session.invalidate();
	}
	
	/**
	 * Public accessor method for an Auth object.
	 * @param request
	 * @return <pre>an instantiated Auth object. Object is instantiated with
	 * the username and accesslevel of the currently logged in 
	 * user (WrapperObject<Person> object in the session variables). </pre>
	 */
	public static Auth getAuth(HttpServletRequest request){
		if(request != null)
			return new Auth(request);
		return new Auth();
	}
}
