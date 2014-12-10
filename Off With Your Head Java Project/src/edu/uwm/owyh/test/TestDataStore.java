package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.library.PMF;
import edu.uwm.owyh.model.DataStore;

public class TestDataStore {
	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private DataStore store;
	private PersistenceManager service;
	private TestObject e1,e2,e3,e4;
	private static final Class<TestObject> TABLE = TestObject.class;


	@Before
	public void setUp(){
		helper.setUp();
		store = DataStore.getDataStore();
		service = PMF.get(); 
		e1 = new TestObject("Todd");
		e2 = new TestObject("Nick");
		e3 = new TestObject("Sarah");
		e4 = new TestObject("Jason");

		e1.setMajor("CS");

		e2.setMajor("Math");

		e3.setMajor("Art History");

		e4.setMajor("History");
	}

	@After
	public void tearDown(){
		helper.tearDown();
		store = null;
	}

	@Test
	public void InsertOneEntity(){    	    	  	
		try{
			assertEquals("Datastore table initially should be empty"
					,0 , dataStoreClassQuery().size());

			store.insertEntity(e1, e1.getId());  

			assertEquals("Datastore table after 1 insert:", 
					1, dataStoreClassQuery().size());
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void InsertSameEntity(){
		try{
			assertEquals("Datastore table initially should be empty"
					,0, dataStoreClassQuery().size());

			assertTrue(store.insertEntity(e1, e1.getId()));

			assertEquals(1, dataStoreClassQuery().size());

			assertFalse(store.insertEntity(e1, e1.getId()));

			TestObject e5 = new TestObject(e1.getName());

			e5.setMajor("Undecided");

			assertFalse(store.insertEntity(e5, e5.getId()));
			List<TestObject> tests = dataStoreClassQuery();

			assertEquals(1,tests.size());
			assertEquals(e1, tests.get(0));
			assertFalse(e5.equals(tests.get(0)));
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void InsertNullEntity(){
		try{
			assertFalse(store.insertEntity(null, null));
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void FindOneEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			String key = "name";
			String value = e2.getName();
			String filter;

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);
			String parent = KeyFactory.keyToString(TestObject.PARENTKEY);
			filter = key + "== '" + value + "' && parentKey == '" + parent + "'";

			List<TestObject> entities = store.findEntities(TABLE, filter,null, null);

			assertEquals(1,entities.size());
			assertEquals(entities.get(0), e2);
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void FindTwoEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			String filter;
			String key = "name";
			String val1 = e2.getName();
			String val2 = e4.getName();

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);

			String parent = KeyFactory.keyToString(TestObject.PARENTKEY);
			
			filter = "(" + key + " == '" + val1 + "' || " 
					+ key + " == '" + val2 + "') && parentKey == '" + parent + "'";

			List<TestObject> entities = store.findEntities(TABLE, filter, null, null);

			assertEquals(2, entities.size());
			assertFalse(entities.get(0).equals(entities.get(1)));

			for(TestObject test : entities){
				assertTrue(test.equals(e2) || test.equals(e4));
			}  	
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void FindEntitiesByParent(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);
			
			TestObject e5 = new TestObject();
			e5.setMajor("testing");
			
			service.makePersistent(e5);
			
			String parent = KeyFactory.keyToString(TestObject.PARENTKEY);
			String filter = "parentKey == '" + parent + "'";
			List<TestObject> entities = store.findEntities(TABLE, filter, null, null);

			assertEquals(4, entities.size());
		}finally{
			store.closeDataStore();
		}
	}
	
	@Test
	public void FindEntitiesWithNullFilter(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);
			
			TestObject e5 = new TestObject();
			e5.setMajor("testing");
			
			service.makePersistent(e5);
			
			List<TestObject> entities = store.findEntities(TABLE, null, null, null);

			assertEquals(5, entities.size());
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void updateExistingEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);

			e1.setMajor("Accounting");
			assertEquals("Accounting",e1.getMajor());

			assertTrue(store.updateEntity(e1, e1.getId()));

			try {
				if(service.isClosed()) service = PMF.get();
				e1 = service.getObjectById(TABLE, e1.getId());
			} catch (JDOObjectNotFoundException e) {
				fail("Object should have been found!");
			}

			assertEquals("Todd",e1.getName());
			assertEquals("Accounting",e1.getMajor());
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void updateNullEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			assertFalse(store.updateEntity(null, null));

			assertEquals("Datastore table should be empty after attempting to update null entity",
					0,dataStoreClassQuery().size());
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void updateNonExistentEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			TestObject e5 = new TestObject("Charles");

			e5.setMajor("Leisure Studies");

			assertFalse(store.updateEntity(e5, e5.getId()));

			assertEquals("Datastore table should be empty after attempting to update entity that doesn't exist in datastore",
					0,dataStoreClassQuery().size());
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void deleteExistingEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);

			assertTrue("deleteEntity returned false, should return true",store.deleteEntity(e1, e1.getId()));
			assertEquals(3, dataStoreClassQuery().size());

			try{
				service.getObjectById(TABLE, e1.getId());
				fail("Entity e1 should not have been found");
			}catch(JDOObjectNotFoundException e){
				//Test passed
			}
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void deleteExistingEntityTwice(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);

			assertTrue("deleteEntity returned false, should return true",store.deleteEntity(e2, e2.getId()));
			assertEquals(3, dataStoreClassQuery().size());

			assertFalse("deleteEntity returned true, should return false",store.deleteEntity(e2, e2.getId()));
			assertEquals(3, dataStoreClassQuery().size());

			try{
				service.getObjectById(TABLE, e2.getId());
				fail("Entity e2 should not have been found");
			}catch(JDOObjectNotFoundException e){
				//Test passed
			}
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void deleteNullEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);

			assertFalse(store.deleteEntity(null, null));
			assertEquals(4,dataStoreClassQuery().size());
		}finally{
			store.closeDataStore();
		}
	}

	@Test
	public void deleteNonExistentEntity(){
		try{
			assertEquals("Datastore table initially should be empty",
					0,dataStoreClassQuery().size());

			service.makePersistent(e1);
			service.makePersistent(e2);
			service.makePersistent(e3);
			service.makePersistent(e4);

			TestObject e5 = new TestObject("Charles");

			e5.setMajor("Leisure Studies");

			assertFalse(store.deleteEntity(e5, e5.getId()));
			assertEquals(4,dataStoreClassQuery().size());
		}finally{
			store.closeDataStore();
		}
	}

	@SuppressWarnings("unchecked")
	private List<TestObject> dataStoreClassQuery(){
		if(service.isClosed()) service = PMF.get();
		List<TestObject> tests = (List<TestObject>)service.newQuery(TABLE).execute();
		tests.size(); //workaround for lazy loading "bug"
		return tests;
	}

	@PersistenceCapable
	private static class TestObject{
		
		private static final Key PARENTKEY = KeyFactory.createKey("Object", "RootKey");
		private static final String KIND = "TestDataStore$" + TABLE.getSimpleName();
		
		@PrimaryKey
		@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
		private Key id;
		
		@Persistent
		@Extension(vendorName="datanucleus", key="gae.parent-pk", value="true")
		private String parentKey;
		
		@Persistent
		private String name;

		@Persistent 
		private String major;
		
		private TestObject(String name){
			KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
			
			id = keyBuilder.addChild(KIND, name).getKey();
			this.name = name;
		}
		
		private TestObject(){
			parentKey = KeyFactory.createKeyString("AnotherObject", "AnotherRootKey");
			name = "testNonEntityGroup";
		}
		
		private Key getId(){
			return id;
		}


		private String getName(){
			return name;
		}	

		private void setMajor(String maj){
			major = maj;
		}

		private String getMajor(){
			return major;
		}

	}	
}
