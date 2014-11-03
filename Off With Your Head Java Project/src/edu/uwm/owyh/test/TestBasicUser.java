package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.model.Client;
import edu.uwm.owyh.model.ContactCard;
import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.Person;
import edu.uwm.owyh.model.UserFactory;

public class TestBasicUser {

	DataStore datastore;
	
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Test
	public void testAddPerson() {
		Person user = UserFactory.getUser("admin@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user.addPerson();	
		List<Entity> search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		List<Entity> search2 = datastore.findEntities(ContactCard.getContactCardTable(), null, Person.USERKEY);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		assertFalse("No contact card was created!", (search2.size() == 0));
		
		
		Person user2 = UserFactory.getUser("admin@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user2.addPerson();
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));
		
		Person user3 = UserFactory.getUser("admin2@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user3.addPerson();
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));
		
		Person user4 = UserFactory.getUser("Admin","(414)123-1234", "this is an address", "admin@uwm.edu");
		assertFalse("User already exists!",user4.addPerson());
		
		Person user5 = UserFactory.getUser("Admin3", "123.543.8787", "this is still an address", "admin3@uwm.edu");
		assertTrue("User should have been added!",user5.addPerson());
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		search2 = datastore.findEntities(ContactCard.getContactCardTable(), null, Person.USERKEY);
		assertEquals("Three clients should have been added!", 3, search.size());
		assertEquals("Three contact cards should have been added!", 3, search2.size());
	}
	
	@Test
	public void testRemoveUser() {
		Person user = UserFactory.getUser("admin@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user.addPerson();	
		List<Entity> search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertFalse("User Was Not Saved!", (search.size() == 0));
		user.removePerson();
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}
	
	@Test
	public void testFindUser() {
		Person user = UserFactory.getUser("admin@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user.addPerson();	
		Person foundUser = user.findPerson("admin@uwm.edu");
		assertTrue("User Was Not Found", (user.getUserName().equals(foundUser.getUserName())));	
	}
	
	public void testGetAllUser() {
		// Incomplete Test, Update Later On
		Person user1 = UserFactory.getUser("admin1@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user1.addPerson();
		Person user2 = UserFactory.getUser("admin2@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user2.addPerson();
		Person user3 = UserFactory.getUser("admin3@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user3.addPerson();
		Person user4 = UserFactory.getUser("admin4@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user4.addPerson();
		
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
		Person user = UserFactory.getUser("admin@uwm.edu", "owyh", Person.AccessLevel.ADMIN);
		user.addPerson();	
		List<Entity> search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY );
		List<Entity> search2 = datastore.findEntities(ContactCard.getContactCardTable(), null, Person.USERKEY );
		assertFalse("User Was Not Saved!", (search.size() == 0));
		assertFalse("Contact card was not saved!", search2.size() == 0);
		
		//user.setUserName("newAdminName"); //Unsure if we actually want.
		user.setPassword("newPassword");
		user.setAccessLevel(Person.AccessLevel.INSTRUCTOR);
		user.setName("First M. Last");
		user.setPhone("(414)-555-4321");
		user.setAddress("This is my address");
		assertTrue("User info was not editted!", user.editPerson());
		
		//assertEquals(user.getUserName(), "newAdminName"); //Do we actually want to allow username to change?
		assertEquals("newPassword",user.getPassword());
		assertEquals(Person.AccessLevel.INSTRUCTOR, user.getAccessLevel());
		assertEquals("First M. Last",user.getName());
		assertEquals("(414)-555-4321", user.getPhone());
		assertEquals(user.getEmail(), user.getUserName());
		assertEquals("This is my address",user.getAddress());
		
		search = datastore.findEntities(Client.getClientTable(), null, Person.USERKEY);
		search2 = datastore.findEntities(ContactCard.getContactCardTable(), null, Person.USERKEY);
		assertTrue("User Was Saved Improperly", (search.size() == 1));
		assertEquals("Contact card was not saved!", 1, search2.size());
	}
	
	@Test
	public void testUserNameCheck(){
		String userName1 = "vince@uwm.edu";
		String userName2 = "vinc3e@mw.ed";
		String userName3 = "vin@ce@uwm.edu";
		String userName4 = "vince@uwm.edu@";
		String userName5 = "vinc3@uwm.edu";
		
		assertTrue(Client.checkUserName(userName1));
		assertFalse(Client.checkUserName(userName2));
		assertFalse(Client.checkUserName(userName3));
		assertFalse(Client.checkUserName(userName4));
		assertTrue(Client.checkUserName(userName5));		
	}
	
	@Test
	public void testPhoneNumberCheck(){
		String phone1 = "(414)123-4545";
		String phone2 = "414-123-4545";
		String phone3 = "414 123 4545";
		String phone4 = "(414)-123 -4545";
		String phone5 = "[414]123-4545";
		String phone6 = "abc-1C3-45b5";
		
		assertTrue(ContactCard.checkPhone(phone1));
		assertTrue(ContactCard.checkPhone(phone2));
		assertTrue(ContactCard.checkPhone(phone3));
		assertFalse(ContactCard.checkPhone(phone4));
		assertFalse(ContactCard.checkPhone(phone5));
		assertFalse(ContactCard.checkPhone(phone6));				
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
