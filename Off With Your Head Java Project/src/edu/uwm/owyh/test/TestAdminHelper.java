package edu.uwm.owyh.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.AdminHelper;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.DataStore;

public class TestAdminHelper {

	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private WrapperObject<Course> _c1;
	private WrapperObject<Section> _s1, _s2, _s3, _s4;
	private WrapperObject<Person> _i1, _i2, _t1, _t2;
	DataStore _store;

	@Before
	public void setUp(){
		helper.setUp();
		_store  = DataStore.getDataStore();
		createCourse();
		createSections();
		createInstructors();
		createTAs();
		AddTaToCourse();
	}
	
	@Test
	public void testSetUp(){
		assertEquals(1, _store.findEntities(Course.class, null, null, null).size());
		assertEquals(4, _store.findEntities(Section.class, null, null, null).size());
		assertEquals(4, _store.findEntities(Person.class, null, null, null).size());
		int instructor = AccessLevel.INSTRUCTOR.getVal();
		int ta = AccessLevel.TA.getVal();
		
		String iFilter = "accessLevel == " + instructor;
		String tFilter = "accessLevel == " + ta;
		
		assertEquals(2, _store.findEntities(Person.class, iFilter, null, null).size());
		assertEquals(2, _store.findEntities(Person.class, tFilter, null, null).size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAssignNewInstructor(){
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) _i1.getProperty("sections");
		assertTrue(sections.isEmpty());
		
		assertTrue(AdminHelper.assignInstructor(_i1, _s1, false));
		
		assertTrue(sections.isEmpty());
		
		sections = (List<WrapperObject<Section>>)_i1.getProperty("sections");
		
		assertEquals(1, sections.size());
		assertEquals("LEC 201", sections.get(0).getProperty("sectionnum"));
	}



	private void AddTaToCourse() {
		List<Key> taKeys = new ArrayList<Key>();
		taKeys.add(_t1.getId());
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("eligibletakeys", taKeys); 
		_c1.editObject(properties);		
	}



	private void createTAs() {
		Map<String, Object> propertiesT1 = PropertyHelper.propertyMapBuilder("firstname", "Jenni"
				,"lastname", "Pluster"
				,"accesslevel", AccessLevel.TA);
		Map<String, Object> propertiesT2 = PropertyHelper.propertyMapBuilder("firstname", "Luna"
				,"lastname", "Moon"
				,"accesslevel", AccessLevel.TA);
		
		_t1 = WrapperObjectFactory.getPerson();
		_t2 = WrapperObjectFactory.getPerson();

		_t1.addObject("jpluster@uwm.edu", propertiesT1);
		_t2.addObject("lmoon@uwm.edu", propertiesT2);
	}



	private void createInstructors() {
		Map<String, Object> propertiesI1 = PropertyHelper.propertyMapBuilder("firstname", "Vince"
				,"lastname", "Maiuri"
				,"accesslevel", AccessLevel.INSTRUCTOR);
		Map<String, Object> propertiesI2 = PropertyHelper.propertyMapBuilder("firstname", "Trevor"
				,"lastname", "Massman"
				,"accesslevel", AccessLevel.INSTRUCTOR);
		
		_i1 = WrapperObjectFactory.getPerson();
		_i2 = WrapperObjectFactory.getPerson();

		_i1.addObject("vmaiuri@uwm.edu", propertiesI1);
		_i2.addObject("tmassman@uwm.edu", propertiesI2);
	}



	private void createCourse() {
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("coursename", "Introduction to Programming");
		
		_c1 = WrapperObjectFactory.getCourse();
		_c1.addObject("101", properties);
	}	

	private void createSections(){
		Map<String, Object> propertiesS1 = PropertyHelper.propertyMapBuilder("sectionnum", "LEC 201"
																			,"days", "MW"
																			,"startdate", "09/01"
																			,"enddate", "12/20"
																			,"starttime","8:00AM"
																			,"endtime", "8:50AM");
		Map<String, Object> propertiesS2 = PropertyHelper.propertyMapBuilder("sectionnum","LEC 202"
																			,"days", "TR"
																			,"startdate", "09/01"
																			,"enddate", "12/20"
																			,"starttime", "9:00AM"
																			,"endtime", "9:50AM");
		Map<String, Object> propertiesS3 = PropertyHelper.propertyMapBuilder("sectionnum", "LAB 801"
																			,"days", "W"
																			,"startdate", "09/01"
																			,"enddate", "12/20"
																			,"starttime", "1:00PM"
																			,"endtime", "2:40PM");
		Map<String, Object> propertiesS4 = PropertyHelper.propertyMapBuilder("sectionnum", "LAB 802"
																			,"days", "R"
																			,"startdate", "09/01"
																			,"enddate","12/20"
																			,"starttime", "9:30AM"
																			,"endtime", "11:10AM");
		
		_s1 = WrapperObjectFactory.getSection();
		_s2 = WrapperObjectFactory.getSection();
		_s3 = WrapperObjectFactory.getSection();
		_s4 = WrapperObjectFactory.getSection();
		
		_s1.addObject("101", propertiesS1);
		_s2.addObject("101", propertiesS2);
		_s3.addObject("101", propertiesS3);
		_s4.addObject("101", propertiesS4);
	}
	
	@After
	public void tearDown(){
		helper.tearDown();
		_store.closeDataStore();
	}
}
