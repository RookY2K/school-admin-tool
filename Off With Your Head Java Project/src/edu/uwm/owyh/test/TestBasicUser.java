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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.DataStore;

public class TestBasicUser {

	DataStore datastore;
	
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@SuppressWarnings("unchecked")
	@Test
	public <T> void testAddPerson() {
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN);
		user.addObject("admin@uwm.edu", properties);	
		List<?> search = datastore.findEntities(user.getTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));	
		
		WrapperObject<Person> user2 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("password","owyh"
				                               ,"accesslevel",AccessLevel.ADMIN);
				
		user2.addObject("admin@uwm.edu", properties);
		search = datastore.findEntities(user2.getTable(), null);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));
		
		WrapperObject<Person> user3 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("password", "owyh"
				                               ,"accesslevel", AccessLevel.ADMIN);
				
		user3.addObject("admin2@uwm.edu",properties);
		search = datastore.findEntities(user3.getTable(), null);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));
		
		WrapperObject<Person> user4 = WrapperObjectFactory.getPerson();
		properties = Library.propertySetBuilder("firstname", "Admin"
				                               ,"phone", "(414)123-1234"
				                               ,"streetaddress", "123 Elm St"
				                               ,"city","Milwaukee"
				                               ,"state","WI"
				                               ,"zip","12345"
				                               );

		assertFalse("User already exists!",user4.addObject("admin@uwm.edu", properties).isEmpty());
		
		WrapperObject<Person> user5 = WrapperObjectFactory.getPerson();
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
		search = datastore.findEntities(user5.getTable(), null);
		assertEquals("Three clients should have been added!", 3, search.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveUser() {
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
																   ,"accesslevel", AccessLevel.ADMIN
				                                                   );
		user.addObject("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(user.getTable(), null);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		
		user.removeObject("admin@uwm.edu");
		search = datastore.findEntities(user.getTable(), null);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindUser() {
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password", "owyh"
				                                                   ,"accesslevel",AccessLevel.ADMIN
				                                                   );
		
		user.addObject("admin@uwm.edu", properties);	
		Key id = Library.generateIdFromUserName("admin@uwm.edu");
		WrapperObject<Person> foundUser = user.findObjectById(id);
		assertTrue("User Was Not Found", (((String)user.getProperty("username"))
				                          .equalsIgnoreCase(
				                        		  (String)foundUser.getProperty("username"))));	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllUser() {
		// Incomplete Test, Update Later On
		WrapperObject<Person> user1 = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN
				                                                   );
		user1.addObject("admin1@uwm.edu", properties);
		WrapperObject<Person> user2 = WrapperObjectFactory.getPerson();
		user2.addObject("admin2@uwm.edu", properties);
		WrapperObject<Person> user3 = WrapperObjectFactory.getPerson();
		user3.addObject("admin3@uwm.edu", properties);
		WrapperObject<Person> user4 = WrapperObjectFactory.getPerson();
		user4.addObject("admin4@uwm.edu", properties);
		
		List<WrapperObject<Person>> clients = user1.getAllObjects();
		
		boolean findUser1 = false, findUser2 = false, findUser3 = false, findUser4 = false;
		
		for (WrapperObject<Person> item : clients) {
			if (item.getId().equals(user1.getId())) findUser1 = true;
			else if (item.getId().equals(user2.getId())) findUser2 = true;
			else if (item.getId().equals(user3.getId())) findUser3 = true;
			else if (item.getId().equals(user4.getId())) findUser4 = true;	
		}
		
		if (!findUser1 || !findUser2 || !findUser3 || !findUser4)
			fail("did not find all USERS!");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEditUser(){
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = Library.propertySetBuilder("password","owyh"
				                                                   ,"accesslevel",AccessLevel.ADMIN
				                                                   );
		user.addObject("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(user.getTable(), null);
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
				
		search = datastore.findEntities(user.getTable(), null);
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAddOfficeHoursNoConflict(){
		WrapperObject<Person> user = getPerson("admin@uwm.edu");
		WrapperObject<OfficeHours> officeHours = WrapperObjectFactory.getOfficeHours();
		List<String> errors;
		
		Map<String, Object> properties = Library.propertySetBuilder("days", "MTR"
				                                                   ,"starttime", "11:15AM"
				                                                   ,"endtime", "2:35PM");
		
		errors = officeHours.addObject("admin@uwm.edu", properties);
		assertTrue(errors.isEmpty());
		
		assertEquals("11:15AM", officeHours.getProperty("starttime"));
		assertEquals("2:35PM", officeHours.getProperty("endtime"));
		assertEquals("MTR", officeHours.getProperty("days"));
		
		properties = Library.propertySetBuilder("days", "MWF"
				                               );
		
	}
	
	@SuppressWarnings("unchecked")
	private WrapperObject<Person> getPerson(String userName){
		WrapperObject<Person> person = WrapperObjectFactory.getPerson();
		
		Map<String, Object> properties = Library.propertySetBuilder("password", "owyh"
				                                                   ,"accesslevel", AccessLevel.ADMIN);
		
		person.addObject(userName, properties);
		
		return person;
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
