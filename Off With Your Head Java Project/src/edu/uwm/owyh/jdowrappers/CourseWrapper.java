/**
 * 
 */
package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.DataStore;

/**
 * @author Vince Maiuri
 *
 */
public class CourseWrapper implements Serializable, WrapperObject<Course> {

	private static final long serialVersionUID = 6739430153706318925L;
	public static final String PARENT = KeyFactory.keyToString(Course.getParentkey());
	
	private Course _course;


	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getId()
	 */
	@Override
	public Key getId() {
		return _course.getId();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getTable()
	 */
	@Override
	public Class<Course> getTable() {
		return Course.getClassname();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;
		
		switch(propertyKey.toLowerCase()){
		case "coursenum":
			return _course.getCourseNum();
		case "coursename":
			return _course.getCourseName();
		case "sections":
			//TODO Return WrapperObject<Section> once I finish sectionWrapper
			break;
		default:
			return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> addObject(String courseNum, Map<String, Object> properties) {
		if(courseNum == null || properties == null) 
			throw new NullPointerException("Arguments are null!");
		DataStore store = DataStore.getDataStore();
		String error;
		Key id = Library.generateIdFromCourseNum(courseNum);
		
		List<String> errors = new ArrayList<String>();
	
		if(findObjectById(id) != null){
			errors.add("Error: Course already exists!");
		}
	
		error = checkProperty("coursenum", courseNum);
	
		if(!error.equals("")){
			errors.add(error);
		}
	
		for(String propertyKey : properties.keySet()){
			error = checkProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}
	
		if(!errors.isEmpty()) return errors;
	
		setCourse(courseNum);		
	
		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	
		if(!store.insertEntity(_course, _course.getId())){
			errors.add("Error: Datastore insert failed for unexpected reason!");
		}
	
		return errors;
	}

	private void setProperty(String propertyKey, Object propertyValue) {
		switch(propertyKey.toLowerCase()){
		case "coursename":
			_course.setCourseName((String)propertyValue);
			break;
		}
	}

	private void setCourse(String courseNum) {
		_course = getCourse(courseNum);
	}

	private Course getCourse(String courseNum) {
		Key id = Library.generateIdFromCourseNum(courseNum);
		
		Course course = (Course)DataStore.getDataStore().findEntityById(getTable(), id);
		
		if(course == null){
			course = Course.getCourse(courseNum);
		}
		
		return course;
	}

	private String checkProperty(String propertyKey, Object propertyValue ) {
		String error = "";
		if(propertyValue == null)
			throw new NullPointerException("Property " + propertyKey + " cannot be null!");
		
		switch(propertyKey.toLowerCase()){
		case "coursename":
			if(!(propertyValue instanceof String))
				throw new IllegalArgumentException(propertyKey + " is not a String!");
			if(((String)propertyValue).trim().length() == 0)
				throw new IllegalArgumentException(propertyKey + " cannot be an empty string!");
			break;
			
		case "coursenum":
			if(!(propertyValue instanceof String))
				throw new IllegalArgumentException(propertyKey + " is not a String!");
			String value = (String)propertyValue;
			if(value.trim().length() != 3)
				throw new IllegalArgumentException(propertyKey + " must be 3 digits long");
			try{
				Integer.parseInt(value);
			}catch(NumberFormatException nfe){
				throw new NumberFormatException("Course number could not be parsed to an Integer. "
						+ "Check that scrape is working correctly");
			}
			break;
		}
		
		return error;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(String courseNum, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		List<String> errors = new ArrayList<String>();
		Key id = Library.generateIdFromCourseNum(courseNum);
	
		if(findObjectById(id) == null){
			throw new IllegalArgumentException("That course does not exist!");
		}
	
		for(String propertyKey : properties.keySet()){
			error = checkProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}
	
		if(!errors.isEmpty()) return errors;
	
		setCourse(courseNum);
	
		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	
		if(!store.updateEntity(_course, _course.getId())){
			errors.add("Error: Datastore update failed for unexpected reason!");
		}
	
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeObject(java.lang.String)
	 */
	@Override
	public boolean removeObject(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<Course>> findObject(String filter,
			WrapperObject<T> parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObjectById(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public WrapperObject<Course> findObjectById(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getAllObjects()
	 */
	@Override
	public List<WrapperObject<Course>> getAllObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addChildObject(java.lang.Object)
	 */
	@Override
	public List<String> addChildObject(Object childJDO)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeChildObject(java.lang.Object)
	 */
	@Override
	public boolean removeChildObject(Object childJDO)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	public static WrapperObject<Course> getCourseWrapper() {
		return new CourseWrapper();
	}

}
