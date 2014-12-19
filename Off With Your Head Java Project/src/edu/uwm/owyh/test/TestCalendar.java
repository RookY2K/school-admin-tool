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
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.CalendarHelper;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.DataStore;
import edu.uwm.owyh.model.CellObject;

public class TestCalendar {

	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private WrapperObject<Course> _c1;
	private WrapperObject<Section> _s1, _s2, _s3, _s4;
	private WrapperObject<TAClass> _tc;
	private WrapperObject<OfficeHours> _oh;
	private WrapperObject<Person> _t1;
	private CellObject[][] array;
	DataStore _store;
	
	@Before
	public void setup()
	{
		helper.setUp();
		_store  = DataStore.getDataStore();
		createCourse();
		createSections();
		createTAs();
		AddTaToCourse();
		createOfficeHour();
		createTAClass();
	}
	
	@Test
	public void testSetUp(){
		assertEquals(1, _store.findEntities(Course.class, null, null, null).size());
		assertEquals(4, _store.findEntities(Section.class, null, null, null).size());
		assertEquals(1, _store.findEntities(Person.class, null, null, null).size());
		int ta = AccessLevel.TA.getVal();
		
		String tFilter = "accessLevel == " + ta;

		assertEquals(1, _store.findEntities(Person.class, tFilter, null, null).size());
	}
	
	public void testArray()
	{
		array = CalendarHelper.getCellObjectArray(_t1);
		assertEquals("officehours", array[0][0].getType());
		assertEquals("taclass", array[1][0].getType());
		assertEquals("section", array[1][1].getType());
	}
	
	private void AddTaToCourse() {
		List<Key> taKeys = new ArrayList<Key>();
		taKeys.add(_t1.getId());
		
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("eligibletakeys", taKeys); 
		_c1.editObject(properties);		
	}


	private void createTAs() {
		Map<String, Object> propertiesT1 = PropertyHelper.propertyMapBuilder("firstname", "Mitchell"
				,"lastname", "Woloschek"
				,"accesslevel", AccessLevel.TA);
		
		_t1 = WrapperObjectFactory.getPerson();

		_t1.addObject("wolosch2@uwm.edu", propertiesT1);
	}

	private void createOfficeHour()
	{
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("days", "T", "starttime", "8:00AM", "endtime", "9:00AM");
		_oh = WrapperObjectFactory.getOfficeHours();
		_oh.addObject("wolosch2@uwm.edu", properties);
	}
	
	private void createTAClass()
	{
		Map<String, Object> properties = PropertyHelper.propertyMapBuilder("classnum", "COMPSCI 201", "classname", "Intro to Computers"
																			, "classtype", "LEC", "days", "R", "startdate"
																			, "9/12", "enddate", "12/11", "starttime", "8:00AM", "endtime", "9:00AM");
		_tc = WrapperObjectFactory.getTAClass();
		_tc.addObject("wolosch2@uwm.edu", properties);
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
