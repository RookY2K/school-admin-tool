package edu.uwm.owyh.test;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.owyh.model.DataStore;

public class TestDataStore {
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private DataStore store;
	private DatastoreService service;
	private Entity e1,e2,e3,e4;
	private String table;
	
	
	@Before
	public void setUp(){
		helper.setUp();
		store = DataStore.getDataStore();
		service = DatastoreServiceFactory.getDatastoreService(); 
		table = "test";
		e1 = new Entity(table);
		e2 = new Entity(table);
		e3 = new Entity(table);
		e4 = new Entity(table);
		
		e1.setProperty("name", "Todd");
		e2.setProperty("name", "Nick");
		e3.setProperty("name", "Sarah");
		e4.setProperty("name", "Jason");
	}
	
	@After
	public void tearDown(){
		helper.tearDown();
		store = null;
	}
    
    @Test
    public void InsertOneEntity(){    	    	  	
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	store.insertEntity(e1);  
    	
    	assertEquals("Datastore table after 1 insert:", 
    			1, service.prepare(new Query(table)).countEntities(withLimit(10)));
    }
    
    @Test
    public void InsertNullEntity(){
        	assertFalse(store.insertEntity(null));
    }
    
    @Test
    public void FindOneEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	String key = "name";
    	String value = (String)e2.getProperty(key);
    	Filter filter;
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	filter = new Query.FilterPredicate(key, Query.FilterOperator.EQUAL, value);
    	
    	List<Entity> entities = store.findEntities(table, filter, null);
    	
    	assertEquals(1,entities.size());
    	assertEquals(entities.get(0), e2);
    }
    
    @Test
    public void FindTwoEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	ArrayList<Filter> filters = new ArrayList<Filter>();
    	Filter filter;
    	String key = "name";
    	String val1 = (String)e2.getProperty(key);
    	String val2 = (String)e4.getProperty(key);
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	filters.add(new Query.FilterPredicate(key, Query.FilterOperator.EQUAL, val1));
    	filters.add(new Query.FilterPredicate(key, Query.FilterOperator.EQUAL, val2));
    	
    	filter = Query.CompositeFilterOperator.or(filters);
    	
    	List<Entity> entities = store.findEntities(table, filter, null);
    	
    	assertEquals(2, entities.size());
    	assertTrue(entities.contains(e2));
    	assertTrue(entities.contains(e4));    	
    }
    
    @Test
    public void FindEntitywithNullFilter(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	List<Entity> entities = store.findEntities(table, null, null);
    	
    	assertEquals(4, entities.size());
    }
    
    @Test
    public void updateExistingEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	String property = "name";
    	
    	e1.setProperty("name", "Vince");
    	assertEquals("Vince",e1.getProperty(property).toString());
    	
    	assertTrue(store.updateEntity(e1));
    	
    	try {
			e1 = service.get(e1.getKey());
		} catch (EntityNotFoundException e) {
			//Not going to happen
		}
    	
    	assertEquals("Vince",e1.getProperty(property).toString());
    }
    
    @Test
    public void updateNullEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	assertFalse(store.updateEntity(null));
    	
    	assertEquals("Datastore table should be empty after attempting to update null entity",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));    	
    }
    
    @Test
    public void updateNonExistentEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	Entity e5 = new Entity(table);
    	
    	assertFalse(store.updateEntity(e5));
    	
    	assertEquals("Datastore table should be empty after attempting to update entity that doesn't exist in datastore",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));    
    }
    
    @Test
    public void deleteExistingEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	assertTrue("deleteEntity returned false, should return true",store.deleteEntity(e1));
    	assertEquals(3, service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	try{
    		service.get(e1.getKey());
    		fail("Entity e1 should not have been found");
    	}catch(EntityNotFoundException enf){
    		//Test passed
    	}
    }
    
    @Test
    public void deleteExistingEntityTwice(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	assertTrue("deleteEntity returned false, should return true",store.deleteEntity(e2));
    	assertEquals(3, service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	assertFalse("deleteEntity returned true, should return false",store.deleteEntity(e2));
    	assertEquals(3, service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	try{
    		service.get(e2.getKey());
    		fail("Entity e2 should not have been found");
    	}catch(EntityNotFoundException enf){
    		//Test passed
    	}
    }
    
    @Test
    public void deleteNullEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	assertFalse(store.deleteEntity(null));
    	assertEquals(4,service.prepare(new Query(table)).countEntities(withLimit(10)));
    }
    
    @Test
    public void deleteNonExistentEntity(){
    	assertEquals("Datastore table initially should be empty",
    			0,service.prepare(new Query(table)).countEntities(withLimit(10)));
    	
    	service.put(e1);
    	service.put(e2);
    	service.put(e3);
    	service.put(e4);
    	
    	Entity e5 = new Entity(table);
    	
    	assertFalse(store.deleteEntity(e5));
    	assertEquals(4,service.prepare(new Query(table)).countEntities(withLimit(10)));
    }
}
