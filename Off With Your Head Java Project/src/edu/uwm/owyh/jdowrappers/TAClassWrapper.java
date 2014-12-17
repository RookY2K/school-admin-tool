/**
 * 
 */
package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.StringHelper;
import edu.uwm.owyh.model.DataStore;

/**
 * @author Vince Maiuri
 *
 */
public class TAClassWrapper implements WrapperObject<TAClass>, Serializable {

	public static final String CLASS_NUM_PATTERN = "^[a-zA-Z]+ \\d{3,4}$";
	public static final String CLASS_TYPE_PATTERN = "^((LEC)|(DIS)|(LAB)|(IND)|(SEM))$";
	private static final long serialVersionUID = 1346412171271128130L;
	private TAClass _taClass;

	
	private TAClassWrapper() {
		//default constructor
	}
	
	static TAClassWrapper getTAClassWrapper(TAClass taClass){
		TAClassWrapper other = getTAClassWrapper();
		
		other._taClass = taClass;
		
		return other;
	}
	
	public static TAClassWrapper getTAClassWrapper(){
		return new TAClassWrapper();
	}
	
	TAClass getTAClass(){
		return _taClass;
	}
	
	private TAClass getTAClass(String classNum, String classType, String taUserName){
		Key taClassKey = WrapperObjectFactory.generateIdFromClassNumAndType(classNum, classType, taUserName);
		
		TAClass taClass = DataStore.getDataStore().findEntityById(getTable(), taClassKey);
		
		if(taClass == null){
			taClass = TAClass.getTAClass(classNum, classType, taUserName);
		}
		
		return taClass;
	}
	
	private void setTAClass(String classNum, String classType, String taUserName){
		_taClass = getTAClass(classNum, classType, taUserName);
	}
	
	@Override
	public Key getId() {
		return getTAClass().getId();
	}

	@Override
	public Class<TAClass> getTable() {
		return TAClass.getClassname();
	}

	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;
		
		switch(propertyKey.toLowerCase()){
		case "classnum":
			return getTAClass().getClassNum();
		case "classname":
			return getTAClass().getClassName();
		case "classtype":
			return getTAClass().getClassType();
		case "days":
			return getTAClass().getDays();
		case "startdate":
			return StringHelper.dateToString(getTAClass().getStartDate());
		case "enddate":
			return StringHelper.dateToString(getTAClass().getEndDate());
		case "starttime":
			return StringHelper.timeToString(getTAClass().getStartTime());
		case "endtime":
			return StringHelper.timeToString(getTAClass().getEndTime());
		default: 
			return null;
		}
	}

	@Override
	public List<String> addObject(String taUserName, Map<String, Object> properties) {
		List<String> errors = new ArrayList<String>();
		
		Key parentId = WrapperObjectFactory.generateIdFromUserName(taUserName);		

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ taUserName + " to add section to!");
		
		if(!parent.getProperty("accesslevel").equals(AccessLevel.TA))
			throw new IllegalArgumentException("Cannot add taClass to a non-TA person!");
		
		errors.addAll(buildNewTAClassWrapper(taUserName, properties));		

		if(!errors.isEmpty()) return errors;
		
		return parent.addChildObject(getTAClass());
	}

	@Override
	public List<String> editObject(Map<String, Object> properties) {
		
		TAClass childJDO = getTAClass();

		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentId = childJDO.getId().getParent();
		List<String> errors = new ArrayList<String>();

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ parentId.getName() + " to edit taClass!");

		try {
			errors = checkAllProperties(properties);
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe.getMessage());
		}

		if(!errors.isEmpty()) return errors;
		
		if(!setAllProperties(properties)) return errors;

		if(!DataStore.getDataStore().updateEntity(getTAClass(), getId())){
			errors.add("Unknown datastore error when updating!");
		}

		return errors;
	}

	@Override
	public boolean removeObject() {
		TAClass childJDO = getTAClass();		
		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentKey = childJDO.getId().getParent();

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentKey);

		return parent.removeChildObject(childJDO);
	}

	@Override
	public boolean removeObjects(List<WrapperObject<TAClass>> taClasses) {
		List<TAClass> taClassList = new ArrayList<TAClass>();
		
		for(WrapperObject<TAClass> taClassWrapper : taClasses){
			TAClassWrapper taClass = (TAClassWrapper)taClassWrapper;
			
			taClassList.add(taClass.getTAClass());
		}
		
		return DataStore.getDataStore().deleteAllEntities(taClassList);
	}

	@Override
	public <T> List<WrapperObject<TAClass>> findObjects(String filter, 
			WrapperObject<T> parent, String order) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<TAClass>> taClasses = null;
		List<TAClass> entities = store.findEntities(getTable(), filter, parent, order);
		taClasses = getTAClassFromList(entities);

		return taClasses;
	}

	@Override
	public WrapperObject<TAClass> findObjectById(Key key) {
		DataStore store = DataStore.getDataStore();
		TAClass taClass =  (TAClass) store.findEntityById(getTable(), key);

		if(taClass == null) return null;

		return getTAClassWrapper(taClass);
	}

	@Override
	public List<WrapperObject<TAClass>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<TAClass>> taClasses = null;
		taClasses = getTAClassFromList(store.findEntities(getTable(), null, null, null));

		return taClasses;
	}

	@Override
	public List<String> addChildObject(Object childJDO)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("TAClass does not have any children entities");
	}

	@Override
	public boolean removeChildObject(Object childJDO)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("TAClass does not have any children entities");
	}

	private List<String> buildNewTAClassWrapper(
			String taUserName, Map<String, Object> properties) {
		
		List<String> errors = new ArrayList<String>();	
		
		try{
			errors = checkAllProperties(properties);
		}catch(ParseException pe){
			throw new IllegalArgumentException(pe.getMessage());
		}
	
		if(!errors.isEmpty()) return errors;
		
		String taClassNum = (String)properties.get("classnum");
		String taClassType = (String)properties.get("classtype");
		
		Key taClassKey = WrapperObjectFactory.generateIdFromClassNumAndType(taClassNum, taClassType, taUserName);
		
		if(findObjectById(taClassKey) != null){
			throw new IllegalArgumentException("Class Number: " + taClassNum + " with type: " + 
					taClassType + " already exists for user: " + taUserName);
		}		
		
		setTAClass(taClassNum, taClassType, taUserName);
	
		setAllProperties(properties);
		
		return errors;
	}

	private List<String> checkAllProperties(Map<String, Object> properties) throws ParseException {
		List<String> errors = new ArrayList<String>();
		
		String taClassNum = (String) properties.get("classnum");
		String taClassType = (String) properties.get("classtype");
		Date startDate = StringHelper.stringToDate((String) properties.get("startdate"));
		Date endDate = StringHelper.stringToDate((String) properties.get("enddate"));
		double startTime = StringHelper.parseTimeToDouble((String) properties.get("starttime"));
		double endTime = StringHelper.parseTimeToDouble((String)properties.get("endtime"));
		if(taClassNum == null){
			if(getTAClass() == null || getTAClass().getClassNum() == null){
				throw new NullPointerException("Class number is missing for addObject operation!");
			}
		}
		
		if(taClassType == null){
			if(getTAClass() == null || getTAClass().getClassType() == null){
				throw new NullPointerException("Class type is missing for addObject operation!");
			}
		}
	
		if(startDate != null && endDate != null){
			if(!startDate.before(endDate))
				errors.add("Start date must be before end date!");				
		}
	
		if(startTime != -1 && endTime != -1){
			if(startTime > endTime)
				errors.add("Start time must be before end time!");			
		}		
	
		for(String key : properties.keySet()){
			String error = checkProperty(key, properties.get(key));
			if(!error.trim().isEmpty()){
				errors.add(error);
			}
		}
		return errors;
	}

	private String checkProperty(String propertyKey, Object propertyValue) {
		String val;
		switch(propertyKey.toLowerCase()){
		case "classnum":
			val = checkString(propertyKey, propertyValue);
			if(!val.matches(CLASS_NUM_PATTERN)){
				throw new IllegalArgumentException("Class Number does not match expected pattern: "
						+ CLASS_NUM_PATTERN);
			}
			break;
		case "classname":
			checkString(propertyKey, propertyValue);
			break;
			
		case "classtype":
			val = checkString(propertyKey, propertyValue);
			if(!val.matches(CLASS_TYPE_PATTERN)){
				throw new IllegalArgumentException("Class type does not match expected pattern: "
						+ CLASS_TYPE_PATTERN);
			}
			break;
			
		case "enddate":case "startdate":
			val = checkString(propertyKey,propertyValue);
			if(!val.matches(SectionWrapper.DATE_PATTERN))
				throw new IllegalArgumentException("Start or end date does not match expected pattern: "
						+ SectionWrapper.DATE_PATTERN);
			break;
		case "starttime":case "endtime":
			val = checkString(propertyKey,propertyValue);
			if(!val.matches(OfficeHoursWrapper.HOURS_PATTERN)&& !val.trim().isEmpty())
				throw new IllegalArgumentException("Start or end time does not match expected pattern: " 
						+ OfficeHoursWrapper.HOURS_PATTERN);
			break;
		case "days":
			val = checkString(propertyKey,propertyValue);
			if(!val.matches(OfficeHoursWrapper.DAYS_PATTERN))
				throw new IllegalArgumentException("Days does not match expected pattern: "
						+ OfficeHoursWrapper.DAYS_PATTERN);
			break;
		default:
			throw new IllegalArgumentException(propertyKey + " is not a valid property of TAClass");
		}	
		
		
		return "";
	}

	private String checkString(Object propertyValue, Object propertyValue2) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean setAllProperties(Map<String, Object> properties) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<WrapperObject<TAClass>> getTAClassFromList(
			List<TAClass> entities) {
		// TODO Auto-generated method stub
		return null;
	}

}
