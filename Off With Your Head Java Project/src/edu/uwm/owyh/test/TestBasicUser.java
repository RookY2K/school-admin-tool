package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.ClientWrapper;
import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.Person.AccessLevel;
import edu.uwm.owyh.model.UserFactory;

public class TestBasicUser {

	DataStore datastore;
	
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Test
	public <T> void testAddPerson() {
		Person user = UserFactory.getUser();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN);
		user.addPerson("admin@uwm.edu", properties);	
		List<?> search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));	
		
		Person user2 = UserFactory.getUser();
		properties = Library.propertySetBuilder("password","owyh"
				                               ,"accesslevel",AccessLevel.ADMIN);
				
		user2.addPerson("admin@uwm.edu", properties);
		search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));
		
		Person user3 = UserFactory.getUser();
		properties = Library.propertySetBuilder("password", "owyh"
				                               ,"accesslevel", AccessLevel.ADMIN);
				
		user3.addPerson("admin2@uwm.edu",properties);
		search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));
		
		Person user4 = UserFactory.getUser();
		properties = Library.propertySetBuilder("firstname", "Admin"
				                               ,"phone", "(414)123-1234"
				                               ,"streetaddress", "123 Elm St"
				                               ,"city","Milwaukee"
				                               ,"state","WI"
				                               ,"zip","12345"
				                               );

		assertFalse("User already exists!",user4.addPerson("admin@uwm.edu", properties).isEmpty());
		
		Person user5 = UserFactory.getUser();
		properties = Library.propertySetBuilder("firstname", "Admin3"
				                               ,"phone", "123.543.8787"
				                               ,"streetaddress","321 Maple Ln"
				                               ,"city","Brookfield"
				                               ,"state","WI"
				                               ,"zip","54321"
				                               );		
		
		assertFalse("User should not have been added without an accesslevel!"
				,user5.addPerson("admin3@uwm.edu", properties).isEmpty());
		properties.put("accesslevel", AccessLevel.ADMIN);
		assertTrue("User should have been added!"
				,user5.addPerson("admin3@uwm.edu", properties).isEmpty());
		search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertEquals("Three clients should have been added!", 3, search.size());
	}
	
	@Test
	public void testRemoveUser() {
		Person user = UserFactory.getUser();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
																   ,"accesslevel", AccessLevel.ADMIN
				                                                   );
		user.addPerson("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		
		user.removePerson("admin@uwm.edu");
		search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}
	
	@Test
	public void testFindUser() {
		Person user = UserFactory.getUser();
		Map<String, Object> properties = Library.propertySetBuilder("password", "owyh"
				                                                   ,"accesslevel",AccessLevel.ADMIN
				                                                   );
		
		user.addPerson("admin@uwm.edu", properties);	
		Person foundUser = user.findPerson("admin@uwm.edu");
		assertTrue("User Was Not Found", (((String)user.getProperty("username"))
				                          .equalsIgnoreCase(
				                        		  (String)foundUser.getProperty("username"))));	
	}
	
	public void testGetAllUser() {
		// Incomplete Test, Update Later On
		Person user1 = UserFactory.getUser();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN
				                                                   );
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
		Person user = UserFactory.getUser();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel",AccessLevel.ADMIN
				                                                   );
		user.addPerson("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));
			
		properties = Library.propertySetBuilder("password","newPassword"
				                               ,"accesslevel", AccessLevel.INSTRUCTOR
				                               ,"firstname","First"
				                               ,"lastname","Last"
				                               ,"phone","(414)-555-4321"
				                               ,"streetaddress","123 Elm St"
				                               ,"city","Milwaukee"
				                               ,"state","WI"
				                               ,"zip","12345"
				                               );
		
		assertTrue("User info was not editted!", user.editPerson("admin@uwm.edu", properties).isEmpty());
		
		assertEquals("newPassword",user.getProperty("password"));
		assertEquals(Person.AccessLevel.INSTRUCTOR, user.getProperty("accesslevel"));
		assertEquals("First",user.getProperty("firstname"));
		assertEquals("Last",user.getProperty("lastname"));
		assertEquals("(414)-555-4321", user.getProperty("phone"));
		assertEquals(user.getProperty("username"), user.getProperty("email"));
		assertEquals("123 Elm St",user.getProperty("streetaddress"));
		assertEquals("Milwaukee", user.getProperty("city"));
		assertEquals("WI",user.getProperty("state"));
		assertEquals("12345",user.getProperty("zip"));
		
		search = datastore.findEntities(ClientWrapper.getClientTable(), null);
		assertTrue("User Was Saved Improperly", (search.size() == 1));
	}
	
	@Test
	public void testEmailCheck(){
		String userName1 = "vince@uwm.edu";
		String userName2 = "vinc3e@mw.ed";
		String userName3 = "vin@ce@uwm.edu";
		String userName4 = "vince@uwm.edu@";
		String userName5 = "vinc3@uwm.edu";
		
		assertTrue(ClientWrapper.checkEmail(userName1));
		assertFalse(ClientWrapper.checkEmail(userName2));
		assertFalse(ClientWrapper.checkEmail(userName3));
		assertFalse(ClientWrapper.checkEmail(userName4));
		assertTrue(ClientWrapper.checkEmail(userName5));		
	}
	
	@Test
	public void testPhoneNumberCheck(){
		String phone1 = "(414)123-4545";
		String phone2 = "414-123-4545";
		String phone3 = "414 123 4545";
		String phone4 = "(414)-123 -4545";
		String phone5 = "[414]123-4545";
		String phone6 = "abc-1C3-45b5";
		
		assertTrue(ClientWrapper.checkPhone(phone1));
		assertTrue(ClientWrapper.checkPhone(phone2));
		assertTrue(ClientWrapper.checkPhone(phone3));
		assertFalse(ClientWrapper.checkPhone(phone4));
		assertFalse(ClientWrapper.checkPhone(phone5));
		assertFalse(ClientWrapper.checkPhone(phone6));				
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
