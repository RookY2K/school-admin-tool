package edu.uwm.owyh.model;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import edu.uwm.owyh.library.PMF;

public final class DataStore{
	
	private static PersistenceManager _service;
	private static DataStore _store = null;
	
	private DataStore(){
		_service =  PMF.get();
	}
		
	public static DataStore getDataStore(){
		if(_store == null || _service.isClosed()){
			_store = new DataStore();
		}
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
	@SuppressWarnings("unchecked")
	public <E> List<E> findEntities(Class<E> table,String filter) throws IllegalArgumentException{
		if(_service.isClosed()) getDataStore();
		Query query = _service.newQuery(table);
			
		List<E> results = null;
		query.setFilter(filter);
			
		results = (List<E>) query.execute();
		results.size(); //workaround for lazy loading "bug"
			
		return results;
	}
	
	/**
	 * Inserts one entity into the datastore.
	 * @param <T>
	 * @param entity
	 * @return true if successful
	 */
	public boolean insertEntity(Object entity, Object id){
		if(entity == null) return false;
		if(_service.isClosed()) getDataStore();
//		int retry = 3;
		boolean success = false;
//		Transaction tnx = _service.currentTransaction();
//		tnx.begin();
//		while(true){
//			try{
				Object ent = findEntityById(entity.getClass(),id);
				if(ent != null) return false;
				_service.makePersistent(entity);
//				tnx.commit();
				success = true;
//				break;
//			}catch(ConcurrentModificationException cme){
//				if(retry == 0) throw cme;
				
//				--retry;
//			}finally{
//				if(tnx.isActive()){
//					tnx.rollback();
//				}
//			}			
//		}		
		return success;
	}
	
	/**
	 * updates an existing entity with new properties
	 * @param <T>
	 * @param ent - entity being updated
	 * @return true if an entity is successfully updated
	 */
	public boolean updateEntity(Object ent, Object id){
		if(ent == null) return false;
		if(_service.isClosed()) getDataStore();
//		int retry = 3;
		boolean success = false;
//		Transaction tnx = _service.currentTransaction();
//		tnx.begin();
//		while(true){
//			try{
				Object entity = findEntityById(ent.getClass(),id);
				if(entity == null) return false;
				_service.makePersistent(ent);
//				tnx.commit();
				success = true;
//				break;
//			}catch(ConcurrentModificationException cme){
//				if(retry == 0) throw cme;
				
//				--retry;
//			}finally{
//				if(tnx.isActive()){
//					tnx.rollback();
//				}
//			}			
//		}		
		return success;	
	}
	
	/**
	 * deletes an entity if the entity exists
	 * @param <T>
	 * @param ent - entity being deleted
	 * @return true if an entity is deleted
	 */
	public  boolean deleteEntity(Object ent, Object id){
		if(ent == null) return false;
		if(_service.isClosed()) getDataStore();
		boolean success = false;
//		int retry = 3;
//		Transaction tnx = _service.currentTransaction();
//		tnx.begin();
//		while(true){
//			try{
				Object entity = findEntityById(ent.getClass(),id);
				if(entity == null) return false;
				_service.deletePersistent(ent);
//				tnx.commit();
				success = true;
//				break;
//			}catch(ConcurrentModificationException cme){
//				if(retry == 0) throw cme;
				
//				--retry;
//			}finally{
//				if(tnx.isActive()){
//					tnx.rollback();
//				}
//			}			
//		}		
		return success;
	}
	
	public Object findEntityById(Class<?> cls,
			Object id) {
		if(cls == null || id == null) return null;
		if(_service.isClosed()) getDataStore();
		try{
			Object result = _service.getObjectById(cls, id);
			return result;
		}catch(JDOObjectNotFoundException nfe){
			return null;
		}
	}
	
	public void closeDataStore(){
		_service.close();
	}
}
