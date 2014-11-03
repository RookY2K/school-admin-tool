package edu.uwm.owyh.model;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

public final class DataStore implements Serializable{
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
	
	/**
	 * Returns a collection of entities based on filter from specified table
	 * @param table
	 * @param filter
	 * @param ancestor
	 * @return Collection<Entity> matching filter
	 * @throws IllegalArgumentException if the filter is illegally constructed.
	 */
	public List<Entity> findEntities(String table,Filter filter, Key ancestor) throws IllegalArgumentException{
		DatastoreService service = getService();
		Query query = new Query(table, ancestor).setFilter(filter);
		
		if(ancestor != null) query.setAncestor(ancestor);
		
		return service.prepare(query).asList(FetchOptions.Builder.withDefaults());
	}
	
	/**
	 * Inserts one entity into the datastore.
	 * @param ent
	 * @return true if successful
	 */
	public boolean insertEntity(Entity ent){
		if(ent == null) return false;
		if(entityExists(ent.getKey())) return false;
		getService().put(ent);		
		return true;
	}
	
	/**
	 * updates an existing entity with new properties
	 * @param ent - entity being updated
	 * @return true if an entity is successfully updated
	 */
	public boolean updateEntity(Entity ent){
		if(ent == null) return false;
		if(!entityExists(ent.getKey())) return false;
		
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
		if(!entityExists(ent.getKey())) return false;
		
		getService().delete(ent.getKey());
		return true;
	}
	
	private boolean entityExists(Key entKey){
		Filter filter = new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, entKey);
		Query query = new Query().setFilter(filter).setKeysOnly();
		
		Entity result = getService().prepare(query).asSingleEntity();
		
		return result != null;		
	}
}
