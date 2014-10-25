package edu.uwm.owyh.model;

import java.util.Collection;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public class DataStore {
	private DatastoreService _service;
	private static DataStore _store = new DataStore();
	
	private DataStore(){
		_service = DatastoreServiceFactory.getDatastoreService();
	}
	
	private DatastoreService getService(){
		return _service;
	}
	
	public static DataStore getDataStore(){
		return _store;
	}
	
	public  Entity findEntity(Map<String,String> filter, String table){
		//TODO Finish stub
		return null;
	}
	
	public Collection<Entity> findEntities(Map<String, String> filter, String table){
		//TODO Finish stub
		return null;
	}
	
	
	public boolean insertEntity(String table, Entity ent){
		//TODO Finish stub
		return false;
	}
	
	public boolean updateEntity(Entity ent){
		//TODO Finish stub
		return false;		
	}
	
	public Entity deleteEntity(Entity ent){
		//TODO Finish stub
		return null;
	}
}
