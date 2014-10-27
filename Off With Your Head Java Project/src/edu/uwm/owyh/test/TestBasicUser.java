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
			
	}
	
	@Test
	public void testRemoveUser() {
		User user = User.getUser("admin", "owyh", User.AccessLevel.ADMIN);
		
		user.saveUser();
		
		List<Entity> search = datastore.findEntities("users", null);

		System.out.print("test: " + search.size());
		
		assertFalse("User Was Not Saved!", (search.size() == 0));
		
		user.removeUser();
		
		search = datastore.findEntities("users", null);
		
		assertTrue("User Was Not Removed", (search.size() == 0));	
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
