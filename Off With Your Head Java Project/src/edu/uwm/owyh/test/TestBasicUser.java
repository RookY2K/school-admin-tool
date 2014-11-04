package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.model.Client;
import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;
import edu.uwm.owyh.model.Person.AccessLevel;

public class TestBasicUser {

	DataStore datastore;
	
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Test
	public <T> void testAddPerson() {
		Person user = UserFactory.getUser();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		user.addPerson("admin@uwm.edu", properties);	
		List<Entity> search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertFalse("User Was Not Saved!", (search.size() == 0));
	
		
		properties.clear();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		Person user2 = UserFactory.getUser();
		user2.addPerson("admin@uwm.edu", properties);
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));
		
		properties.clear();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		Person user3 = UserFactory.getUser();
		user3.addPerson("admin2@uwm.edu",properties);
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));
		
		properties.clear();
		properties.put("name", "Admin");
		properties.put("phone", "(414)123-1234");
		properties.put("address", "this is an address");
		Person user4 = UserFactory.getUser();
		assertFalse("User already exists!",user4.addPerson("admin@uwm.edu", properties).isEmpty());
		
		properties.clear();
		properties.put("name", "Admin3");
		properties.put("phone", "123.543.8787");
		properties.put("address", "this is still an address");
		
		Person user5 = UserFactory.getUser();
		assertFalse("User should not have been added without an accesslevel!"
				,user5.addPerson("admin3@uwm.edu", properties).isEmpty());
		properties.put("accesslevel", AccessLevel.ADMIN);
		assertTrue("User should have been added!"
				,user5.addPerson("admin3@uwm.edu", properties).isEmpty());
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertEquals("Three clients should have been added!", 3, search.size());
	}
	
	@Test
	public void testRemoveUser() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		Person user = UserFactory.getUser();
		user.addPerson("admin@uwm.edu",properties);	
		List<Entity> search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		user.removePerson("admin@uwm.edu");
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}
	
	@Test
	public void testFindUser() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		Person user = UserFactory.getUser();
		user.addPerson("admin@uwm.edu", properties);	
		Person foundUser = user.findPerson("admin@uwm.edu");
		assertTrue("User Was Not Found", (user.getUserName().equalsIgnoreCase(foundUser.getUserName())));	
	}
	
	public void testGetAllUser() {
		// Incomplete Test, Update Later On
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		Person user1 = UserFactory.getUser();
		user1.addPerson("admin1@uwm.edu", properties);
		Person user2 = UserFactory.getUser();
		user2.addPerson("admin2@uwm.edu", properties);
		Person user3 = UserFactory.getUser();
		user3.addPerson("admin3@uwm.edu", properties);
		Person user4 = UserFactory.getUser();
		user4.addPerson("admin4@uwm.edu", properties);
		
		List<Person> clients = user1.getAllPersons();
		
		boolean findUser1 = false, findUser2 = false, findUser3 = false, findUser4 = false;
		
		for (Person item : clients) {
			if (item.getUserName() == user1.getUserName()) findUser1 = true;
			else if (item.getUserName() != user2.getUserName()) findUser2 = true;
			else if (item.getUserName() != user3.getUserName()) findUser3 = true;
			else if (item.getUserName() != user4.getUserName()) findUser4 = true;	
		}
		
		if (!findUser1 || !findUser2 || !findUser3 || !findUser4)
			fail("did not find all USERS!");
	}
	
	@Test
	public void testEditUser(){
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("password", "owyh");
		properties.put("accesslevel", AccessLevel.ADMIN);
		Person user = UserFactory.getUser();
		user.addPerson("admin@uwm.edu",properties);	
		List<Entity> search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY );
		assertFalse("User Was Not Saved!", (search.size() == 0));
			
		//user.setUserName("newAdminName"); //Unsure if we actually want.
		properties.put("password", "newPassword");
		properties.put("accesslevel", AccessLevel.INSTRUCTOR);
		properties.put("name", "First M. Last");
		properties.put("phone", "(414)-555-4321");
		properties.put("address", "This is my address");
		assertTrue("User info was not editted!", user.editPerson("admin@uwm.edu", properties).isEmpty());
		
		//assertEquals(user.getUserName(), "newAdminName"); //Do we actually want to allow username to change?
		assertEquals("newPassword",user.getProperty("password"));
		assertEquals(Person.AccessLevel.INSTRUCTOR, user.getProperty("accesslevel"));
		assertEquals("First M. Last",user.getProperty("name"));
		assertEquals("(414)-555-4321", user.getProperty("phone"));
		assertEquals(user.getProperty("username"), user.getProperty("email"));
		assertEquals("This is my address",user.getProperty("address"));
		
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertTrue("User Was Saved Improperly", (search.size() == 1));
	}
	
	@Test
	public void testEmailCheck(){
		String userName1 = "vince@uwm.edu";
		String userName2 = "vinc3e@mw.ed";
		String userName3 = "vin@ce@uwm.edu";
		String userName4 = "vince@uwm.edu@";
		String userName5 = "vinc3@uwm.edu";
		
		assertTrue(Client.checkEmail(userName1));
		assertFalse(Client.checkEmail(userName2));
		assertFalse(Client.checkEmail(userName3));
		assertFalse(Client.checkEmail(userName4));
		assertTrue(Client.checkEmail(userName5));		
	}
	
	@Test
	public void testPhoneNumberCheck(){
		String phone1 = "(414)123-4545";
		String phone2 = "414-123-4545";
		String phone3 = "414 123 4545";
		String phone4 = "(414)-123 -4545";
		String phone5 = "[414]123-4545";
		String phone6 = "abc-1C3-45b5";
		
		assertTrue(Client.checkPhone(phone1));
		assertTrue(Client.checkPhone(phone2));
		assertTrue(Client.checkPhone(phone3));
		assertFalse(Client.checkPhone(phone4));
		assertFalse(Client.checkPhone(phone5));
		assertFalse(Client.checkPhone(phone6));				
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
