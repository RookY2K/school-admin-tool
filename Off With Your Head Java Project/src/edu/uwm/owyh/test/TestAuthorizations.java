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

public class TestAuthorizations extends Auth{
	private AccessLevel _level;
	private String _userName;
	private String _password;
	Auth a1;

	@Before
	public void setUp() throws Exception {
		_level = AccessLevel.ADMIN;
		_userName = "vamaiuri@uwm.edu";
		_password = "paSsw0rd$";
		a1 = Auth.getAuth();
	}

	@Test
	public void testGoodUser() {
		assertTrue("Basic user verification failed!",  a1.verifyUser(_userName, _password));
		String captitalUName = _userName.toUpperCase();
		assertTrue("User verification failed with user name in all caps!", a1.verifyUser(captitalUName, _password));
		String lowerUName = _userName.toLowerCase();
		assertTrue( "Verification failed with user name in all lowercase!", a1.verifyUser(lowerUName, _password));
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
