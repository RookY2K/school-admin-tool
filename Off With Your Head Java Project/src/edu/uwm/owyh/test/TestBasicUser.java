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

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.DataStore;

public class TestBasicUser {

	DataStore datastore;
	
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Test
	public <T> void testAddPerson() {
		WrapperObject user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN);
		user.addObject("admin@uwm.edu", properties);	
		List<?> search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));	
		
		WrapperObject user2 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("password","owyh"
				                               ,"accesslevel",AccessLevel.ADMIN);
				
		user2.addObject("admin@uwm.edu", properties);
		search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));
		
		WrapperObject user3 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("password", "owyh"
				                               ,"accesslevel", AccessLevel.ADMIN);
				
		user3.addObject("admin2@uwm.edu",properties);
		search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));
		
		WrapperObject user4 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("firstname", "Admin"
				                               ,"phone", "(414)123-1234"
				                               ,"streetaddress", "123 Elm St"
				                               ,"city","Milwaukee"
				                               ,"state","WI"
				                               ,"zip","12345"
				                               );

		assertFalse("User already exists!",user4.addObject("admin@uwm.edu", properties).isEmpty());
		
		WrapperObject user5 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("firstname", "Admin3"
				                               ,"phone", "123.543.8787"
				                               ,"streetaddress","321 Maple Ln"
				                               ,"city","Brookfield"
				                               ,"state","WI"
				                               ,"zip","54321"
				                               );		
		
		assertFalse("User should not have been added without an accesslevel!"
				,user5.addObject("admin3@uwm.edu", properties).isEmpty());
		properties.put("accesslevel", AccessLevel.ADMIN);
		assertTrue("User should have been added!"
				,user5.addObject("admin3@uwm.edu", properties).isEmpty());
		search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertEquals("Three clients should have been added!", 3, search.size());
	}
	
	@Test
	public void testRemoveUser() {
		WrapperObject user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
																   ,"accesslevel", AccessLevel.ADMIN
				                                                   );
		user.addObject("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		
		user.removeObject("admin@uwm.edu");
		search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}
	
	@Test
	public void testFindUser() {
		WrapperObject user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password", "owyh"
				                                                   ,"accesslevel",AccessLevel.ADMIN
				                                                   );
		
		user.addObject("admin@uwm.edu", properties);	
		WrapperObject foundUser = user.findObject("admin@uwm.edu");
		assertTrue("User Was Not Found", (((String)user.getProperty("username"))
				                          .equalsIgnoreCase(
				                        		  (String)foundUser.getProperty("username"))));	
	}
	
	@Test
	public void testGetAllUser() {
		// Incomplete Test, Update Later On
		WrapperObject user1 = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN
				                                                   );
		user1.addObject("admin1@uwm.edu", properties);
		WrapperObject user2 = WrapperObjectFactory.getPerson();
		user2.addObject("admin2@uwm.edu", properties);
		WrapperObject user3 = WrapperObjectFactory.getPerson();
		user3.addObject("admin3@uwm.edu", properties);
		WrapperObject user4 = WrapperObjectFactory.getPerson();
		user4.addObject("admin4@uwm.edu", properties);
		
		List<WrapperObject> clients = user1.getAllObjects();
		
		boolean findUser1 = false, findUser2 = false, findUser3 = false, findUser4 = false;
		
		for (WrapperObject item : clients) {
			if (item.getId().equals(user1.getId())) findUser1 = true;
			else if (item.getId().equals(user2.getId())) findUser2 = true;
			else if (item.getId().equals(user3.getId())) findUser3 = true;
			else if (item.getId().equals(user4.getId())) findUser4 = true;	
		}
		
		if (!findUser1 || !findUser2 || !findUser3 || !findUser4)
			fail("did not find all USERS!");
	}
	
	@Test
	public void testEditUser(){
		WrapperObject user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel",AccessLevel.ADMIN
				                                                   );
		user.addObject("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
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
				                               ,"officehours", "TR 10:25AM-12:45PM"
				                               );
		
		assertTrue("User info was not editted!", user.editObject("admin@uwm.edu", properties).isEmpty());
		
		assertEquals("newPassword",user.getProperty("password"));
		assertEquals(WrapperObject.AccessLevel.INSTRUCTOR, user.getProperty("accesslevel"));
		assertEquals("First",user.getProperty("firstname"));
		assertEquals("Last",user.getProperty("lastname"));
		assertEquals("(414)-555-4321", user.getProperty("phone"));
		assertEquals(user.getProperty("username"), user.getProperty("email"));
		assertEquals("123 Elm St",user.getProperty("streetaddress"));
		assertEquals("Milwaukee", user.getProperty("city"));
		assertEquals("WI",user.getProperty("state"));
		assertEquals("12345",user.getProperty("zip"));
		
        List<String> hours = (List<String>)user.getProperty("officehours");
		
		assertEquals("TR 10:00AM-12:00PM",hours.get(0));
		
		search = datastore.findEntities(PersonWrapper.getPersonTable(), null);
		assertTrue("User Was Saved Improperly", (search.size() == 1));
	}
	
	@Test
	public void testEmailCheck(){
		String userName1 = "vince@uwm.edu";
		String userName2 = "vinc3e@mw.ed";
		String userName3 = "vin@ce@uwm.edu";
		String userName4 = "vince@uwm.edu@";
		String userName5 = "vinc3@uwm.edu";
		
		assertTrue(PersonWrapper.checkEmail(userName1));
		assertFalse(PersonWrapper.checkEmail(userName2));
		assertFalse(PersonWrapper.checkEmail(userName3));
		assertFalse(PersonWrapper.checkEmail(userName4));
		assertTrue(PersonWrapper.checkEmail(userName5));		
	}
	
	@Test
	public void testPhoneNumberCheck(){
		String phone1 = "(414)123-4545";
		String phone2 = "414-123-4545";
		String phone3 = "414 123 4545";
		String phone4 = "(414)-123 -4545";
		String phone5 = "[414]123-4545";
		String phone6 = "abc-1C3-45b5";
		
		assertTrue(PersonWrapper.checkPhone(phone1));
		assertTrue(PersonWrapper.checkPhone(phone2));
		assertTrue(PersonWrapper.checkPhone(phone3));
		assertFalse(PersonWrapper.checkPhone(phone4));
		assertFalse(PersonWrapper.checkPhone(phone5));
		assertFalse(PersonWrapper.checkPhone(phone6));				
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
