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

import edu.uwm.owyh.exceptions.BuildJDOException;
import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.NonPersistedWrapperObject;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.model.DataStore;

/**
 * @author Vince Maiuri
 *
 */
public class CourseWrapper implements Serializable, WrapperObject<Course>, NonPersistedWrapperObject<Course> {

	private static final long serialVersionUID = 6739430153706318925L;
	public static final String PARENT = KeyFactory.keyToString(Course.getParentkey());
	
	private Course _course;


	private CourseWrapper(Course course) {
		_course = course;
	}

	private CourseWrapper() {
		// Default constructor
	}

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
			return WrapperObjectFactory.getSection().findObjects(null, this, "sectionNum");
		case "eligibletakeys":
			return new ArrayList<Key>(_course.getEligibleTAKeys());
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> addObject(String courseNum, Map<String, Object> properties) {
		if(courseNum == null || properties == null) 
			throw new NullPointerException("Arguments are null!");
		DataStore store = DataStore.getDataStore();
		
		List<String> errors = buildNewCourseWrapper(courseNum, properties);
	
		if(!errors.isEmpty()) return errors;
	
		if(!store.insertEntity(_course, _course.getId())){
			errors.add("Error: Datastore insert failed for unexpected reason!");
		}
	
		return errors;
	}

	@SuppressWarnings("unchecked")
	private boolean setProperty(String propertyKey, Object propertyValue) {
		if(!checkNewInfo(propertyKey, propertyValue)) return false; 
		switch(propertyKey.toLowerCase()){
		case "coursename":
			_course.setCourseName((String)propertyValue);
			break;
		case "eligibletakeys":
			List<Key> taKeys = new ArrayList<Key>();
			taKeys.addAll((List<Key>) propertyValue);
			_course.setEligibleTAKeys(taKeys);
		}
		
		return true;		
	}

	@SuppressWarnings("unchecked")
	private boolean checkNewInfo(String propertyKey, Object propertyValue) {
		boolean isNewInfo = true;
		
		switch(propertyKey.toLowerCase()){
		case "coursename":
			String oldCourseName = _course.getCourseName();
			if(oldCourseName != null){
				isNewInfo = !oldCourseName.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "eligibletakeys":
			List<Key> oldEligibleList = _course.getEligibleTAKeys();
			List<Key> newEligibleList = (List<Key>)propertyValue;
			if(oldEligibleList != null)
				isNewInfo = !oldEligibleList.equals(newEligibleList);
			break;			
		}
		return isNewInfo;
	}

	private void setCourse(String courseNum) {
		_course = getCourse(courseNum);
	}

	private Course getCourse(String courseNum) {
		Key id = WrapperObjectFactory.generateIdFromCourseNum(courseNum);
		
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
		case "eligibletakeys":
			if(!(propertyValue instanceof List<?>))
				throw new IllegalArgumentException(propertyKey + " must be a List!");
			List<?> objects = (List<?>)propertyValue;
			for(Object obj : objects){
				if(!(obj instanceof Key)){
					throw new IllegalArgumentException(propertyKey + " must be a list of Keys!");
				}
				Key key = (Key)obj;
				
				String kind = key.getKind();
				
				if(!Person.getKind().equalsIgnoreCase(kind))
					throw new IllegalArgumentException(propertyKey + " must be a list of Person Keys!");
			}
		}
		
		return error;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		boolean isNewInfo = false;
		List<String> errors = new ArrayList<String>();
		
		Key id = this.getId();
	
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
	
//		String courseNum = id.getName();
//		setCourse(courseNum);
		for(String propertyKey : properties.keySet()){
			isNewInfo |= setProperty(propertyKey, properties.get(propertyKey));
		}
		
		if(!isNewInfo) return errors;
		
		if(!store.updateEntity(_course, _course.getId())){
			errors.add("Error: Datastore update failed for unexpected reason!");
		}
	
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeObject(java.lang.String)
	 */
	@Override
	public boolean removeObject(String courseNum) {
		DataStore store = DataStore.getDataStore();
		
		setCourse(courseNum);
	
		return store.deleteEntity(_course, _course.getId());		
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<Course>> findObjects(String filter,
			WrapperObject<T> parent, String order) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Course>> courses = null;
	
		String filterWithParent = "parentKey == '" + PARENT + "'" +
						"&& " + filter;
		List<Course> entities = store.findEntities(getTable(), filterWithParent, null, order);
		courses = getCoursesFromList(entities);
		
		return courses;
	}
	
	private List<WrapperObject<Course>> getCoursesFromList(List<Course> entities) {
		List<WrapperObject<Course>> courses = new ArrayList<WrapperObject<Course>>();
		for (Course item : entities)
			courses.add(CourseWrapper.getCourseWrapper(item));
		return courses;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObjectById(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public WrapperObject<Course> findObjectById(Key key) {
		DataStore store = DataStore.getDataStore();
		Course course =  (Course) store.findEntityById(getTable(), key);
		
		if(course == null) return null;
		
		return getCourseWrapper(course);
	}

	private static WrapperObject<Course> getCourseWrapper(Course course) {
		return new CourseWrapper(course);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getAllObjects()
	 */
	@Override
	public List<WrapperObject<Course>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Course>> courses = null;
		String filter = "parentKey == '" + PARENT + "'";
	
		courses = getCoursesFromList(store.findEntities(getTable(), filter, null, null));
	
		return courses;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addChildObject(java.lang.Object)
	 */
	@Override
	public List<String> addChildObject(Object childJDO)
			throws UnsupportedOperationException {
		
		String kind = childJDO.getClass().getSimpleName();
		List<String> errors = new ArrayList<String>();
		switch(kind.toLowerCase()){
		case "section":
			if(getCourse().getSections().contains(childJDO)){
				errors.add("Duplicate section!");
			}else{
				getCourse().addSection((Section)childJDO);
			}
			break;
		default: 
			throw new IllegalArgumentException("Course jdo does not have " + kind + " as a child jdo!");
		}
		
		if(!errors.isEmpty()) return errors;
		
		if(!DataStore.getDataStore().updateEntity(getCourse(), getCourse().getId())){
			errors.add("DataStore update failed for unknown reason! Please try again.");
		}
		
		return errors;
	}

	private Course getCourse() {
		return _course;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeChildObject(java.lang.Object)
	 */
	@Override
	public boolean removeChildObject(Object childJDO)
			throws UnsupportedOperationException {
		
		String kind = childJDO.getClass().getSimpleName();
		switch(kind.toLowerCase()){
		case "section":
			if(!getCourse().getSections().contains(childJDO)){
				return false;
			}else{
				getCourse().removeSection((Section)childJDO);
			}
			break;
		default: 
			throw new IllegalArgumentException("Course jdo does not have " + kind + " as a child jdo!");
		}
		
		return DataStore.getDataStore().updateEntity(getCourse(), getCourse().getId());
	}

	public static WrapperObject<Course> getCourseWrapper() {
		return new CourseWrapper();
	}

	@Override
	public WrapperObject<Course> buildObject(String courseNum, Map<String, Object> properties) throws BuildJDOException {
		List<String> errors = buildNewCourseWrapper(courseNum, properties);
		
		if(!errors.isEmpty()) throw new BuildJDOException(errors);
		
		return getCourseWrapper(_course);
	}

	@Override
	public boolean saveAllObjects(List<WrapperObject<Course>> objects) {
		List<Course> courses = new ArrayList<Course>();
		
		for(WrapperObject<Course> object : objects){
			if(!(object instanceof CourseWrapper)) continue;
			
			CourseWrapper course = (CourseWrapper)object;
			
			courses.add(course.getCourse());
		}
		
		return DataStore.getDataStore().insertEntities(courses);
	}

	@Override
	public boolean removeObjects(List<WrapperObject<Course>> courses) {
		List<Course> courseList = new ArrayList<Course>();
		
		for(WrapperObject<Course> course : courses){
			CourseWrapper courseWrapper = (CourseWrapper)course;
			
			courseList.add(courseWrapper.getCourse());
		}
		
		return DataStore.getDataStore().deleteAllEntities(courseList);
	}
	
	private List<String> buildNewCourseWrapper(String courseNum, Map<String, Object> properties){
		if(courseNum == null || properties == null) 
			throw new NullPointerException("Arguments are null!");
		String error;
		Key id = WrapperObjectFactory.generateIdFromCourseNum(courseNum);
		
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
		
		return errors;
	}

	@Override
	public boolean addChild(WrapperObject<?> child) {
		if(!(child instanceof SectionWrapper)) return false;
		
		SectionWrapper sectionChild = (SectionWrapper)child;
		
		if(this.getCourse().getSections().contains(sectionChild)) return false;
		
		this.getCourse().addSection(sectionChild.getSection());
		
		return true;		
	}

}
