package edu.uwm.owyh.model;

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
}
