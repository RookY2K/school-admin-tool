package edu.uwm.owyh.test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uwm.owyh.model.*;
import edu.uwm.owyh.model.User.AccessLevel;

public class TestAuthorizations{
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private AccessLevel _level;
	private String _userName;
	private String _password;
	private DatastoreService _service;
	private Auth _a1;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		_level = AccessLevel.ADMIN;
		_userName = "vamaiuri@uwm.edu";
		_password = "paSsw0rd$";
		_service = DatastoreServiceFactory.getDatastoreService();
		
		Entity user = new Entity("users");
		user.setProperty("username", _userName);
		user.setProperty("toupperusername", _userName.toUpperCase());
		user.setProperty("password", _password);
		user.setProperty("accesslevel", _level.getVal());
		_service.put(user);
		
		_a1 = Auth.getAuth(null);
	}

	@Test
	public void testGoodLogin() {
		assertTrue("Basic user verification failed!",  _a1.verifyLogin(_userName, _password) != null);
		String captitalUName = _userName.toUpperCase();
		assertTrue("User verification failed with user name in all caps!", _a1.verifyLogin(captitalUName, _password) != null);
		String lowerUName = _userName.toLowerCase();
		assertTrue( "Verification failed with user name in all lowercase!", _a1.verifyLogin(lowerUName, _password) != null);
	}
	
	@Test
	public void testBadLoginName() {
		assertEquals("Login verification should have failed",null, _a1.verifyLogin("vamauiri@uwm.edu", _password));
	}
	
	@Test
	public void testBadPassword(){
		assertEquals("Login should fail due to password being wrong password",null, _a1.verifyLogin(_userName, "notThePassword!"));
		assertEquals("Login should fail due to password being case sensitive",null, _a1.verifyLogin(_userName, _password.toLowerCase()));
		assertEquals("Login should fail due to password being case sensitive",null, _a1.verifyLogin(_userName, _password.toUpperCase()));
	}
	
	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

}
