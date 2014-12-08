package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

/**
 * <pre>
 * Interface for all JDO wrapper classes. Used to hide the JDO 
 * implementation. More importantly, allows control over what is set
 * to the JDO's to avoid accidental persistence of bad data.
 * </pre>
 * @author Vince Maiuri
 *
 * @param <E> - JDO class 
 */
public interface WrapperObject<E> extends Serializable{
	/**
	 * <pre>
	 * Accessor for the Primary key id of the JDO
	 * Key data includes the Parent key if you wish
	 * to extract it with id.getParent()
	 * </pre>
	 * @return Key id
	 */
	abstract Key getId();
	
	/**
	 * <pre>
	 * Returns the Class of the WrapperObject
	 * This is useful for interaction with the Datastore
	 * </pre>
	 * @return the Class of the WrapperObject
	 */
	abstract Class<E> getTable();
	
	/**
	 * <pre>
	 * Given an appropriate property name (propertyKey), the 
	 * associated property value is returned.
	 * </pre>
	 * @param propertyKey - Property Name
	 * @return Property value if exists, null otherwise
	 */
	abstract Object getProperty(String propertyKey);
	
	/**
	 * <pre>
	 * Adds the object to the datastore if it does not already exist. Returns any
	 * Errors that occur. If errors do occur, then nothing is added to the datastore.
	 * </pre>
	 * @param id - Equal to objects identifying property, if it has one, else equal to its parent's identifying property
	 * @param properties - Map of properties to be set. 
	 * @return List of errors if there were any. A non-empty list means the object was not added to the datastore.
	 */
	abstract List<String> addObject(String id, Map<String,Object> properties);
	
	/**
	 * <pre>
	 * Edits an existing calling object in the datastore. Returns any errors that occur. If errors do occur,
	 * then nothing was edited. 
	 * </pre>
	 * @param id - Equal to objects identifying property, if it has one, else equal to its parent's identifying property
	 * @param properties - Map of properties to set
	 * @return List of errors if there were any. A non-empty list means the object was not edited in the datastore.
	 */
	abstract List<String> editObject(String id, Map<String,Object> properties);

	/**
	 * Removes calling object from the datastore.
	 * @param id - The identifying property of the object, if it hs one, else equal to its parent's identifying property.
	 * @return true if the object was removed
	 */
	abstract boolean removeObject(String id);
	
	/**
	 * Finds all objects with the given filter with given parent(optional)
	 * @param filter <pre> JDOQL filter used to find specific object(s). If null, then equivalent to calling
	 *                 getAllObjects</pre>
	 * @param parent <pre> Optional parameter (set null if not desired) that allows a search for an object
	 *                 with the given parent</pre>
	 * @return a list of found objects. List will be empty if nothing found. 
	 */
	abstract <T> List<WrapperObject<E>> findObject(String filter, WrapperObject<T> parent, String order);
	
	/**
	 * Finds an object by it's primary key
	 * @param key
	 * @return the object if it's found. Null otherwise.
	 */
	abstract WrapperObject<E> findObjectById(Key key);

	/**
	 * Gets all objects of the given calling objects class.
	 * @return a list of all the objects found. List will be empty if nothing found.
	 */
	abstract List<WrapperObject<E>> getAllObjects();

	/**
	 * <pre>Optional utility method for Parent wrapper classes to set their children fields.
	 * Allows a hook from child wrapper class to inform the parent wrapper class to set its
	 * child JDO field. JDO's with no children will not implement this method.</pre>
	 * @param childJDO - Child JDO to set as child of the calling parent object
	 * @return <pre>A list of any errors that might have occurred while setting the child jdo. If list is
	 *         not empty, then no child was added.</pre>
	 * @throws <pre>UnsupportedOperationException - If JDO class has no child classes, this method will not
	 *                                         be implemented</pre>
	 */	
	abstract List<String> addChildObject(Object childJDO) throws UnsupportedOperationException;	
	
	/**
	 * <pre>Optional utility method for Parent wrapper classes to remove child jdo's from child fields.
	 * Allows a hook from child wrapper class to inform the parent wrapper class to remove Child jdo.
	 * JDO's with no children will not implement this method.</pre>
	 * @param childJDO - Child JDO to remove from calling parent object
	 * @return true if child jdo was removed
	 * @throws <pre>UnsupportedOperationException - If JDO class has no child classes, this method will not
	 *                                         be implemented</pre>
	 */
	abstract boolean removeChildObject(Object childJDO) throws UnsupportedOperationException;
}