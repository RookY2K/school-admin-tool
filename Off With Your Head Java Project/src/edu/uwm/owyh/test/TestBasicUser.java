package edu.uwm.owyh.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.User;

public class TestBasicUser {

	DataStore datastore;
	
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Test
	public void testSaveUser() {
		User user = User.getUser("admin", "owyh", User.AccessLevel.ADMIN);
		user.saveUser();	
		List<Entity> search = datastore.findEntities("users", null);	
		assertFalse("User Was Not Saved!", (search.size() == 0));
		
		User user2 = User.getUser("admin", "owyh", User.AccessLevel.ADMIN);
		user2.saveUser();
		search = datastore.findEntities("users", null);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));
		
		User user3 = User.getUser("admin2", "owyh", User.AccessLevel.ADMIN);
		user3.saveUser();
		search = datastore.findEntities("users", null);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));
	}
	
	@Test
	public void testRemoveUser() {
		User user = User.getUser("admin", "owyh", User.AccessLevel.ADMIN);
		user.saveUser();	
		List<Entity> search = datastore.findEntities("users", null);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		user.removeUser();
		search = datastore.findEntities("users", null);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}
	
	@Test
	public void testEditUser(){
		User user = User.getUser("admin", "owyh", User.AccessLevel.ADMIN);
		user.saveUser();	
		List<Entity> search = datastore.findEntities("users", null);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		
		//user.setUserName("newAdminName"); //Unsure if we actually want.
		user.setPassword("newPassword");
		user.setAccessLevel(User.AccessLevel.INSTRUCTOR);
		user.saveUser();
		
		//assertEquals(user.getUserName(), "newAdminName"); //Do we actually want to allow username to change?
		assertEquals(user.getPassword(), "newPassword");
		assertEquals(user.getAccessLevel(), User.AccessLevel.INSTRUCTOR);
		
		search = datastore.findEntities("users", null);
		assertTrue("User Was Saved Improperly", (search.size() == 1));
	}
	
	@Before
	public void setUp() {
		helper.setUp();
		datastore = DataStore.getDataStore();
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
	}
}
