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
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.library.StringHelper;
import edu.uwm.owyh.model.DataStore;

public class TestWrapperObjectInterface {

	private DataStore datastore;
	private WrapperObject<Person> parent;
	private WrapperObject<Person> parent2;
	private WrapperObject<Course> course1;
	private WrapperObject<Course> course2;
	

	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Test
	public <T> void testAddPerson() {
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("password","owyh"
				,"accesslevel", AccessLevel.ADMIN);
		user.addObject("admin@uwm.edu", properties);	
		List<?> search = datastore.findEntities(user.getTable(), null, null, null);
		assertFalse("User Was Not Saved!", (search.size() == 0));	

		WrapperObject<Person> user2 = WrapperObjectFactory.getPerson();
		properties = PropertyHelper.propertyMapBuilder("password","owyh"
				,"accesslevel",AccessLevel.ADMIN);

		user2.addObject("admin@uwm.edu", properties);
		search = datastore.findEntities(user2.getTable(), null, null, null);
		assertFalse("Two User of the same name was SAVED!", (search.size() == 2));

		WrapperObject<Person> user3 = WrapperObjectFactory.getPerson();
		properties = PropertyHelper.propertyMapBuilder("password", "owyh"
				,"accesslevel", AccessLevel.ADMIN);

		user3.addObject("admin2@uwm.edu",properties);
		search = datastore.findEntities(user3.getTable(), null, null, null);
		assertTrue("Two User of different name was NOT SAVED!", (search.size() == 2));

		WrapperObject<Person> user4 = WrapperObjectFactory.getPerson();
		properties = PropertyHelper.propertyMapBuilder("firstname", "Admin"
				,"phone", "(414)123-1234"
				,"streetaddress", "123 Elm St"
				,"city","Milwaukee"
				,"state","WI"
				,"zip","12345"
				);

		assertFalse("User already exists!",user4.addObject("admin@uwm.edu", properties).isEmpty());

		WrapperObject<Person> user5 = WrapperObjectFactory.getPerson();
		properties = PropertyHelper.propertyMapBuilder("firstname", "Admin3"
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
		search = datastore.findEntities(user5.getTable(), null, null, null);
		assertEquals("Three clients should have been added!", 3, search.size());
	}

	@Test
	public void testRemoveUser() {
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("password","owyh"
				,"accesslevel", AccessLevel.ADMIN
				);
		user.addObject("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(user.getTable(), null, null, null);
		assertFalse("User Was Not Saved!", (search.size() == 0));

		user.removeObject();
		search = datastore.findEntities(user.getTable(), null, null, null);
		assertTrue("User Was Not Removed", (search.size() == 0));	
	}

	@Test
	public void testFindUser() {
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("password", "owyh"
				,"accesslevel",AccessLevel.ADMIN
				);

		user.addObject("admin@uwm.edu", properties);	
		Key id = WrapperObjectFactory.generateIdFromUserName("admin@uwm.edu");
		WrapperObject<Person> foundUser = user.findObjectById(id);
		assertTrue("User Was Not Found", (((String)user.getProperty("username"))
				.equalsIgnoreCase(
						(String)foundUser.getProperty("username"))));	
	}

	@Test
	public void testGetAllUser() {
		// Incomplete Test, Update Later On
		WrapperObject<Person> user1 = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("password","owyh"
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

	@Test
	public void testEditUser(){
		WrapperObject<Person> user = WrapperObjectFactory.getPerson();
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("password","owyh"
				,"accesslevel",AccessLevel.ADMIN
				);
		user.addObject("admin@uwm.edu",properties);	
		List<?> search = datastore.findEntities(user.getTable(), null, null, null);
		assertFalse("User Was Not Saved!", (search.size() == 0));

		properties = PropertyHelper.propertyMapBuilder("password","newPassword"
				,"accesslevel", AccessLevel.INSTRUCTOR
				,"firstname","First"
				,"lastname","Last"
				,"phone","(414)-555-4321"
				,"streetaddress","123 Elm St"
				,"city","Milwaukee"
				,"state","WI"
				,"zip","12345"				                               
				);

		assertTrue("User info was not editted!", user.editObject(properties).isEmpty());

		assertEquals("newPassword",user.getProperty("password"));
		assertEquals(AccessLevel.INSTRUCTOR, user.getProperty("accesslevel"));
		assertEquals("First",user.getProperty("firstname"));
		assertEquals("Last",user.getProperty("lastname"));
		assertEquals("(414)-555-4321", user.getProperty("phone"));
		assertEquals(user.getProperty("username"), user.getProperty("email"));
		assertEquals("123 Elm St",user.getProperty("streetaddress"));
		assertEquals("Milwaukee", user.getProperty("city"));
		assertEquals("WI",user.getProperty("state"));
		assertEquals("12345",user.getProperty("zip"));

		search = datastore.findEntities(user.getTable(), null, null, null);
		assertTrue("User Was Saved Improperly", (search.size() == 1));
	}

	@Test
	public void testEmailCheck(){
		String userName1 = "vince@uwm.edu";
		String userName2 = "vinc3e@mw.ed";
		String userName3 = "vin@ce@uwm.edu";
		String userName4 = "vince@uwm.edu@";
		String userName5 = "vinc3@uwm.edu";

		assertTrue(userName1.matches(PersonWrapper.EMAILPATTERN));
		assertFalse(userName2.matches(PersonWrapper.EMAILPATTERN));
		assertFalse(userName3.matches(PersonWrapper.EMAILPATTERN));
		assertFalse(userName4.matches(PersonWrapper.EMAILPATTERN));
		assertTrue(userName5.matches(PersonWrapper.EMAILPATTERN));		
	}

	@Test
	public void testPhoneNumberCheck(){
		String phone1 = "(414)123-4545";
		String phone2 = "414-123-4545";
		String phone3 = "414 123 4545";
		String phone4 = "(414)-123 -4545";
		String phone5 = "[414]123-4545";
		String phone6 = "abc-1C3-45b5";

		assertTrue(phone1.matches(PersonWrapper.PHONEPATTERN));
		assertTrue(phone2.matches(PersonWrapper.PHONEPATTERN));
		assertTrue(phone3.matches(PersonWrapper.PHONEPATTERN));
		assertFalse(phone4.matches(PersonWrapper.PHONEPATTERN));
		assertFalse(phone5.matches(PersonWrapper.PHONEPATTERN));
		assertFalse(phone6.matches(PersonWrapper.PHONEPATTERN));				
	}

	@Test
	public void testAddOfficeHoursNoConflict(){
		WrapperObject<Person> user = getPerson("admin@uwm.edu");
		WrapperObject<OfficeHours> officeHours = WrapperObjectFactory.getOfficeHours();
		List<String> errors;

		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "11:15AM"
				,"endtime", "2:35PM");

		errors = officeHours.addObject((String)user.getProperty("username"), properties);
		assertTrue(errors.isEmpty());

		assertEquals("11:15AM", officeHours.getProperty("starttime"));
		assertEquals("2:35PM", officeHours.getProperty("endtime"));
		assertEquals("MTR", officeHours.getProperty("days"));

		properties = PropertyHelper.propertyMapBuilder("days", "MWF"
				,"starttime", "9:05AM"
				,"endtime", "11:14AM");

		WrapperObject<OfficeHours> officeHours2 = WrapperObjectFactory.getOfficeHours();
		errors = officeHours2.addObject((String)user.getProperty("username"), properties);
		assertTrue(errors.isEmpty());

		assertEquals("9:05AM", officeHours2.getProperty("starttime"));
		assertEquals("11:14AM", officeHours2.getProperty("endtime"));
		assertEquals("MWF", officeHours2.getProperty("days"));

		assertEquals(2, ((List<?>)user.getProperty("officehours")).size());	
		assertTrue(!officeHours.equals(officeHours2));

		properties = PropertyHelper.propertyMapBuilder("days", "TRF"
				,"starttime", "2:36PM"
				,"endtime", "4:14PM");

		WrapperObject<OfficeHours> officeHours3 = WrapperObjectFactory.getOfficeHours();
		errors = officeHours3.addObject((String)user.getProperty("username"), properties);
		assertTrue(errors.isEmpty());

		assertEquals("2:36PM", officeHours3.getProperty("starttime"));
		assertEquals("4:14PM", officeHours3.getProperty("endtime"));
		assertEquals("TRF", officeHours3.getProperty("days"));

		assertEquals(3, ((List<?>)user.getProperty("officehours")).size());	
		assertTrue(!officeHours3.equals(officeHours) && !officeHours3.equals(officeHours2));
	}

	@Test
	public void TestAddOfficeHoursWithBadStringFormat(){
		WrapperObject<Person> user = getPerson("admin@uwm.edu");

		WrapperObject<OfficeHours> officeHours = WrapperObjectFactory.getOfficeHours();

		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("days", "MTSF"
				,"starttime", "3:45PM"
				,"endtime", "5:45PM");

		try{
			officeHours.addObject((String)user.getProperty("username"), properties);
			fail("IllegalArgumentException expected!");
		}catch(IllegalArgumentException iae){
			//pass test
		}

		properties = PropertyHelper.propertyMapBuilder("days", "MTF"
				,"starttime", "01:65AM"
				,"endtime", "12:30PM");

		try{
			officeHours.addObject((String)user.getProperty("username"), properties);
			fail("IllegalArgumentException expected!");
		}catch(IllegalArgumentException iae){
			//pass test
		}

		properties = PropertyHelper.propertyMapBuilder("days", "MTF"
				,"starttime", "01:45AM"
				,"endtime", "12:30PW");

		try{
			officeHours.addObject((String)user.getProperty("username"), properties);
			fail("IllegalArgumentException expected!");
		}catch(IllegalArgumentException iae){
			//pass test
		}
		
		properties = PropertyHelper.propertyMapBuilder("days", ""
				,"starttime", "1:45AM"
				,"endtime", "3:50AM");
		
		assertFalse(officeHours.addObject((String)user.getProperty("username"), properties).isEmpty());
		assertEquals(0, officeHours.getAllObjects().size());
		
	}
	
	@Test
	public void TestAddOfficeHoursWithConflicts(){
		WrapperObject<Person> user = getPerson("admin@uwm.edu");
		WrapperObject<OfficeHours> officeHours = WrapperObjectFactory.getOfficeHours();
		List<String> errors;

		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "11:15AM"
				,"endtime", "2:35PM");

		errors = officeHours.addObject((String)user.getProperty("username"), properties);
		assertTrue(errors.isEmpty());

		assertEquals("11:15AM", officeHours.getProperty("starttime"));
		assertEquals("2:35PM", officeHours.getProperty("endtime"));
		assertEquals("MTR", officeHours.getProperty("days"));

		properties = PropertyHelper.propertyMapBuilder("days", "MWF"
				,"starttime", "9:05AM"
				,"endtime", "11:16AM");

		WrapperObject<OfficeHours> officeHours2 = WrapperObjectFactory.getOfficeHours();
		errors = officeHours2.addObject((String)user.getProperty("username"), properties);
		assertTrue("Conflict was not caught!",!errors.isEmpty());

		assertEquals(1, ((List<?>)user.getProperty("officehours")).size());	

		properties = PropertyHelper.propertyMapBuilder("days", "TRF"
				,"starttime", "2:34PM"
				,"endtime", "4:14PM");

		WrapperObject<OfficeHours> officeHours3 = WrapperObjectFactory.getOfficeHours();
		errors = officeHours3.addObject((String)user.getProperty("username"), properties);
		assertTrue("Conflict wasn't caught!", !errors.isEmpty());

		assertEquals(1, ((List<?>)user.getProperty("officehours")).size());	
		
		properties = PropertyHelper.propertyMapBuilder("days", "TRF"
				,"starttime", "9:05AM"
				,"endtime", "7:30AM");
		
		WrapperObject<OfficeHours> officeHours4 = WrapperObjectFactory.getOfficeHours();
		errors  = officeHours4.addObject((String)user.getProperty("username"), properties);
		assertTrue("Start time was before endtime!", !errors.isEmpty());
		
		assertEquals(1, ((List<?>)user.getProperty("officehours")).size());
	}
	
	@Test
	public void TestFindSpecificOfficeHours(){
		addFourOfficeHours();
		
		String days = "MTR";
		String startTime = "11:15AM";
		String endTime = "2:35PM";
		
		String filter = "days == " + days
				      + " && startTime == " + StringHelper.parseTimeToDouble(startTime)
				      + " && endTime == " + StringHelper.parseTimeToDouble(endTime);
		
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertFalse(officeHours.isEmpty());
		assertEquals(1, officeHours.size());
		
		assertEquals("MTR", officeHours.get(0).getProperty("days"));
		assertEquals("11:15AM", officeHours.get(0).getProperty("starttime"));
		assertEquals("2:35PM", officeHours.get(0).getProperty("endTime"));
	}
	
	@Test
	public void TestFindOfficeHoursInRange(){
		addFourOfficeHours();		
		boolean onM = true;
		String startTime = "2:36PM";
		String endTime = "11:14AM";
		
		String filter = "onMonday == " + onM;
				      
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertFalse(officeHours.isEmpty());
		assertEquals(2, officeHours.size());
		
		filter = "startTime < " + StringHelper.parseTimeToDouble(startTime);
		officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent,null);
		assertFalse(officeHours.isEmpty());
		assertEquals(2, officeHours.size());
		
		filter = "startTime > " + StringHelper.parseTimeToDouble(endTime)
				+" && startTime < " + StringHelper.parseTimeToDouble(startTime);
		
		officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertFalse(officeHours.isEmpty());
		assertEquals(1, officeHours.size());		
		
		filter = "endTime > " + StringHelper.parseTimeToDouble(endTime)
				+" && onThursday == " + true
				+" && onTuesday == " + true 
				+" && onFriday == " + false;
		officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertFalse(officeHours.isEmpty());
		assertEquals(1, officeHours.size());
	}
	
	@Test
	public void TestFindAllOfficeHours(){
		addFourOfficeHours();
		
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().getAllObjects();
		
		assertEquals(4, officeHours.size());
	}
	
	@Test
	public void findOfficeHoursByParent(){
		addFourOfficeHours();
		
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(null, parent, null);
		
		assertEquals(3, officeHours.size());
	}
	
	@Test
	public void TestEditOfficeHoursWithNoConflict(){
		addFourOfficeHours();
		
		String filter = "days == 'MTR'";
		
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertEquals(1, officeHours.size());
		
		WrapperObject<OfficeHours> hours = officeHours.get(0);
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("days", "MF"
																   ,"starttime","12:30PM"
																   ,"endtime","2:00PM");		
		
		hours.editObject(properties);
		
		assertEquals("MF", hours.getProperty("days"));
		assertEquals("12:30PM", hours.getProperty("starttime"));
		assertEquals("2:00PM", hours.getProperty("endtime"));
	}
	
	@Test
	public void TestEditOfficeHoursWithConflicts(){
		addFourOfficeHours();
		
		String filter = "days == 'MTR'";
		
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertEquals(1, officeHours.size());
		
		WrapperObject<OfficeHours> hours = officeHours.get(0);
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime","9:06AM"
				,"endtime", "2:30PM");
		
		assertFalse(hours.editObject(properties).isEmpty());
		
		assertFalse("9:06AM".equals(hours.getProperty("starttime")));
		assertFalse("2:30PM".equals(hours.getProperty("endtime")));
		
		properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "8:30AM"
				,"endtime", "9:06AM");
		
		assertFalse(hours.editObject(properties).isEmpty());
		
		assertFalse("8:30AM".equals(hours.getProperty("starttime")));
		assertFalse("9:06AM".equals(hours.getProperty("endtime")));
		
	}
	
	@Test
	public void TestEditOfficeHoursWithBadStringFormat(){
		addFourOfficeHours();
		
		String filter = "days == 'MTR'";		
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(filter, parent, null);
		assertEquals(1, officeHours.size());
		
		WrapperObject<OfficeHours> hours = officeHours.get(0);
		
		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("days", "MTSF"
				,"starttime", "11:15AM"
				,"endtime", "2:35PM");		

		try{
			hours.editObject(properties);
			fail("IllegalArgumentException expected!");
		}catch(IllegalArgumentException iae){
			assertFalse("MTSF".equals(hours.getProperty("days")));
		}

		properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "11:65AM"
				,"endtime", "12:30PM");

		try{
			hours.editObject(properties);
			fail("IllegalArgumentException expected!");
		}catch(IllegalArgumentException iae){
			assertFalse("11:65AM".equals(hours.getProperty("starttime")));
		}

		properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "11:15AM"
				,"endtime", "12:30PW");

		try{
			hours.editObject(properties);
			fail("IllegalArgumentException expected!");
		}catch(IllegalArgumentException iae){
			assertFalse("12:30PW".equals(hours.getProperty("endtime")));
		}
		
		properties = PropertyHelper.propertyMapBuilder("days", ""
				,"starttime", "11:15AM"
				,"endtime", "2:35PM");
		
		assertFalse(hours.editObject(properties).isEmpty());
		assertFalse("".equals(hours.getProperty("days")));
	}
	
	@Test
	public void testAddCourseUnique(){
		WrapperObject<Course> course = WrapperObjectFactory.getCourse();
		String courseNum = "101";
		String courseName = "Introduction to Programming";
		Map<String,Object> properties = PropertyHelper.propertyMapBuilder("coursename", courseName);
		
		assertTrue(course.addObject(courseNum, properties).isEmpty());
		assertEquals(courseNum,course.getProperty("coursenum"));
		assertEquals(courseName, course.getProperty("coursename"));
	}
	
	@Test
	public void testAddMultipleCourseUnique(){
		WrapperObject<Course> course1 = WrapperObjectFactory.getCourse();
		WrapperObject<Course> course2 = WrapperObjectFactory.getCourse();
		
		String courseNum1 = "101";
		String courseName1 = "Introduction to Programming";
		String courseNum2 = "251";
		String courseName2 = "Intermediate Programming";
		
		Map<String, Object> properties1 = PropertyHelper.propertyMapBuilder("coursename", courseName1);
		Map<String, Object> properties2 = PropertyHelper.propertyMapBuilder("coursename", courseName2);
		
		assertTrue(course1.addObject(courseNum1, properties1).isEmpty());
		assertTrue(course2.addObject(courseNum2, properties2).isEmpty());
		
		assertEquals(2,DataStore.getDataStore().findEntities(course1.getTable(), null, null, null).size());
	}
	
	@Test
	public void testAddCourseWithBadProperties(){
		WrapperObject<Course> course = WrapperObjectFactory.getCourse();
		DataStore store = DataStore.getDataStore();
		assertTrue(store.findEntities(course.getTable(), null, null, null).isEmpty());
		String badNum1 = "1b1";
		String badNum2 = "97";
		String badNum3 = "1004";
		String badNum4 = " ";
		String badNum5 = null;
		
		String courseName = "This is a course";
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("coursename", courseName);
		
		for(int i=0; i<5; ++i){
			try{
				switch(i){
				case 1:
					course.addObject(badNum1, properties);
					fail("An exception should have been thrown for a non digit string");
				case 2:
					course.addObject(badNum2, properties);
					fail("An exception should have been thrown for a string less than 3 characters long");
				case 3:
					course.addObject(badNum3, properties);
					fail("An exception should have been thrown for a string greater than 3 characters long");
				case 4: 
					course.addObject(badNum4, properties);
					fail("An exception should have been thrown for an empty string");
				case 5: 
					course.addObject(badNum5, properties);
					fail("an exception should have been thrown for a null pointer");				
				}
			}catch(NumberFormatException nfe){
				//pass test
			}catch(IllegalArgumentException iae){
				//pass test
			}catch(NullPointerException npe){
				//pass test
			}catch(Exception e){
				fail("Unexpected exception occurred: " + e.getMessage());
			}
			assertTrue(store.findEntities(course.getTable(), null, null, null).isEmpty());
		}
		
		
		
		properties = PropertyHelper.propertyMapBuilder("coursename"," ");
		
		try{
			course.addObject("101", properties);
			fail("An exception should have been thrown for an empty string");
		}catch(IllegalArgumentException e){
			//pass test
		}catch(Exception e){
			fail("Unexpected exception occurred: " + e.getMessage());
		}
		
		assertTrue(store.findEntities(course.getTable(), null, null, null).isEmpty());
		
		properties = PropertyHelper.propertyMapBuilder("coursename", null);
		
		try{
			course.addObject("101", properties);
			fail("An exception should have been thrown for a null pointer");
		}catch(NullPointerException e){
			//pass test
		}catch(Exception e){
			fail("Unexpected exception occurred: " + e.getMessage());
		}
		
		assertTrue(store.findEntities(course.getTable(), null, null, null).isEmpty());
	}
	
	@Test
	public void testEditCourse(){
		addTwoCourses();
		assertEquals(2, DataStore.getDataStore().findEntities(course1.getTable(), null, null, null).size());
		String courseName = "This is a different course";
		Map<String,Object>properties = PropertyHelper.propertyMapBuilder("coursename",courseName);
		assertEquals("This is a course",course1.getProperty("coursename"));
		
		assertTrue(course1.editObject(properties).isEmpty());
		
		assertEquals(courseName, course1.getProperty("coursename"));
		assertEquals(2, DataStore.getDataStore().findEntities(course1.getTable(), null, null, null).size());		
	}
	
	private void addTwoCourses(){
		Map<String,Object> properties1 = PropertyHelper.propertyMapBuilder("coursename","This is a course");
		Map<String,Object> properties2 = PropertyHelper.propertyMapBuilder("coursename", "This is another course");
		course1 = WrapperObjectFactory.getCourse();
		course1.addObject("101", properties1);
		course2 = WrapperObjectFactory.getCourse();
		course2.addObject("102", properties2);
	}
	
	private void addFourOfficeHours(){
		parent = getPerson("admin@uwm.edu");
		parent2 = getPerson("admin2@uwm.edu");
		WrapperObject<OfficeHours> officeHours = WrapperObjectFactory.getOfficeHours();

		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "11:15AM"
				,"endtime", "2:35PM");

		officeHours.addObject((String)parent.getProperty("username"), properties);

		properties = PropertyHelper.propertyMapBuilder("days", "MWF"
				,"starttime", "9:05AM"
				,"endtime", "11:14AM");

		WrapperObject<OfficeHours> officeHours2 = WrapperObjectFactory.getOfficeHours();
		officeHours2.addObject((String)parent.getProperty("username"), properties);

		properties = PropertyHelper.propertyMapBuilder("days", "TRF"
				,"starttime", "2:36PM"
				,"endtime", "4:14PM");

		WrapperObject<OfficeHours> officeHours3 = WrapperObjectFactory.getOfficeHours();
		officeHours3.addObject((String)parent.getProperty("username"), properties);
		
		properties = PropertyHelper.propertyMapBuilder("days", "MTR"
				,"starttime", "11:15AM"
				,"endtime", "2:35PM");

		WrapperObject<OfficeHours> officeHours4 = WrapperObjectFactory.getOfficeHours();
		officeHours4.addObject((String)parent2.getProperty("username"), properties);
	}
	
	private WrapperObject<Person> getPerson(String userName){
		WrapperObject<Person> person = WrapperObjectFactory.getPerson();

		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("password", "owyh"
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
