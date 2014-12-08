package edu.uwm.owyh.model;

import java.util.ConcurrentModificationException;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.jdowrappers.WrapperObject;
import edu.uwm.owyh.library.PMF;

/**
 * Datastore interaction Layer. All datastore calls should come through this
 * layer.
 * @author Vince Maiuri
 *
 */
public final class DataStore{
	
	private static PersistenceManager _service;
	private static DataStore _store = null;

	//Private constructor
	private DataStore(){
		_service =  PMF.get();
	}

	/**
	 * Public accessor for an instantiated DataStore object. 
	 * @return an instantiated DataStore object
	 */
	public static DataStore getDataStore(){
		if(_store == null || _service.isClosed()){
			_store = new DataStore();
		}
		return _store;
	}

	/**
	 * Returns a list of entities based on filter from specified table
	 * @param table - The JDO class that is being searched for
	 * @param filter - The JDOQL filter
	 * @param parentEntity - Optional parameter that will do search by parent if not null
	 * @return List<E> matching filter
	 */
	@SuppressWarnings("unchecked")
	public <E, T> List<E> findEntities(Class<E> table,String filter, WrapperObject<T> parentEntity, String order){
		if(_service.isClosed()) getDataStore();
		List<E> results = null;
		Key parentKey = null;
		
		Query query = _service.newQuery(table);
		
		if(parentEntity != null){
			if(filter != null){
				filter = "parent == parentEntity && " + filter;
			}else{
				filter = "parent == parentEntity";
			}
			parentKey = parentEntity.getId();
		}
		query.setFilter(filter);
		if(order != null)query.setOrdering(order);
		
		if(parentEntity != null) query.declareParameters("String parentEntity");		

		results = (List<E>) query.execute(parentKey);

		return results;
	}

	/**
	 * Inserts one entity into the datastore.
	 * @param <E> class of entity
	 * @param entity to be inserted
	 * @param id - primary key of entity to be inserted
	 * @return true if successful
	 */
	@SuppressWarnings("unchecked")
	public <E> boolean insertEntity(E entity, Key id){
		if(entity == null) return false;
		if(_service.isClosed()) getDataStore();

		E ent = (E) findEntityById(entity.getClass(),id);
		if(ent != null) return false;
		_service.makePersistent(entity);

		return true;
	}

	/**
	 * updates an existing entity with new properties
	 * @param <E> Class of entity being updated
	 * @param id - primary key of entity being updated
	 * @param ent - entity being updated
	 * @return true if an entity is successfully updated
	 */
	@SuppressWarnings("unchecked")
	public <E> boolean updateEntity(E ent, Key id){
		if(ent == null) return false;
		if(_service.isClosed()) getDataStore();

		E entity = (E) findEntityById(ent.getClass(),id);
		if(entity == null) return false;
		_service.makePersistent(ent);

		return true;	
	}

	/**
	 * deletes an entity if the entity exists
	 * @param <E> class of entity being deleted
	 * @param ent - entity being deleted
	 * @param id - primary key of entity being deleted
	 * @return true if an entity is deleted
	 */
	public <E> boolean deleteEntity(E ent, Key id){
		if(ent == null) return false;
		if(_service.isClosed()) getDataStore();
		Object entity = findEntityById(ent.getClass(),id);
		if(entity == null) return false;
		_service.deletePersistent(ent);		
		return true;
	}

	/**
	 * Find an entity given it's id
	 * @param cls - class of entity being searched for
	 * @param id - Primary key of entity being searched for
	 * @return the entity if found, else null
	 */
	public <E> E findEntityById(Class<E> cls,
			Key id) {
		if(cls == null || id == null) return null;
		if(_service.isClosed()) getDataStore();
		try{
			E result = _service.getObjectById(cls, id);
			return result;
		}catch(JDOObjectNotFoundException nfe){
			return null;
		}
	}

	/**
	 * Closes the PersistenceManager
	 */
	public void closeDataStore(){
		_service.close();
	}

	/**
	 * <pre>Batch insertion of entities. Transaction based:
	 * I.E. If any entity fails to be inserted, 
	 * then all entities are not inserted.</pre>
	 * @param entities - A list of entities to be inserted
	 * @return true if all entities are inserted. Else false.
	 */
	public <E> boolean insertEntities(List<E> entities) {
		int retries = 3;
		Transaction tnx = _service.currentTransaction();
		boolean success = false;
		tnx.begin();
		while(!success){
			try{
				_service.makePersistentAll(entities);
				tnx.commit();
				success = true;
			}catch(ConcurrentModificationException cme){
				if(retries == 0) throw cme;
				
				--retries;
			}finally{
				if(tnx.isActive()) tnx.rollback();
			}
		}		
		
		return success;
	}

	/**
	 * <pre>Batch deletion of entities. Transaction based:
	 * I.E. if any entity in list fails to be deleted, then
	 * all entities in list are not deleted</pre>
	 * @param entities - List of entities to be deleted
	 * @return true if all entities are deleted, else false.
	 */
	public <E> boolean deleteAllEntities(List<E> entities) {
		int retries = 3;
		Transaction tnx = _service.currentTransaction();
		boolean success = false;
		tnx.begin();
		
		while(!success){
			try{
				_service.deletePersistentAll(entities);
				tnx.commit();
				success = true;
			}catch(ConcurrentModificationException cme){
				if(retries == 0) throw cme;
				
				--retries;
			}finally{
				if(tnx.isActive()) tnx.rollback();
			}
		}
		
		return success;		
	}
}
