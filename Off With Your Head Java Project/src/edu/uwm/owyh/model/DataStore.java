package edu.uwm.owyh.model;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

public final class DataStore {
	private DatastoreService _service;
	private static DataStore _store = null;
	
	private DataStore(){
		_service = DatastoreServiceFactory.getDatastoreService();
	}
	
	private DatastoreService getService(){
		return _service;
	}
	
	public static DataStore getDataStore(){
		if(_store == null)
			_store = new DataStore();
		return _store;
	}
	
	/*
	public  Entity findEntity(String table, Map<String,String> filter){
		//TODO Finish stub
		return null;
	}*/
	
	/**
	 * Returns a collection of entities based on filter from specified table
	 * @param table
	 * @param filter
	 * @return Collection<Entity> matching filter
	 * @throws IllegalArgumentException if the filter is illegally constructed.
	 */
	public List<Entity> findEntities(String table,Filter filter) throws IllegalArgumentException{
		DatastoreService service = getService();
		Query query = new Query(table).setFilter(filter);
		
		return service.prepare(query).asList(FetchOptions.Builder.withDefaults());
	}
	
	/**
	 * Inserts one entity into the datastore.
	 * @param ent
	 * @return true if successful
	 */
	public boolean insertEntity(Entity ent){
		if(ent == null) return false;
		getService().put(ent);		
		return true;
	}
	
	/**
	 * updates an existing entity with new propertys
	 * @param ent - entity being updated
	 * @return true if an entity is successfully updated
	 */
	public boolean updateEntity(Entity ent){
		if(ent == null) return false;
		try{
			getService().get(ent.getKey());
		}catch(EntityNotFoundException enf){
			return false;
		}catch(IllegalArgumentException iae){
			return false;
		}
		
		getService().put(ent);
		
		return true;		
	}
	
	/**
	 * deletes an entity if the entity exists
	 * @param ent - entity being deleted
	 * @return true if an entity is deleted
	 */
	public boolean deleteEntity(Entity ent){
		if(ent == null) return false;
		try{
			getService().get(ent.getKey());
		}catch(EntityNotFoundException enf){
			return false;
		}catch(IllegalArgumentException iae){
			return false;
		}
		
		getService().delete(ent.getKey());
		return true;
	}
}
