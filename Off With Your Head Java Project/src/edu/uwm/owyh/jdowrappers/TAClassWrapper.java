/**
 * 
 */
package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Person;
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
	
	/**
	 * 
	 * @param classNum
	 * @param classType
	 * @param taUserName
	 * @return the TAClass
	 */
	private TAClass getTAClass(String classNum, String classType, String taUserName){
		Key taClassKey = WrapperObjectFactory.generateIdFromClassNumAndType(classNum, classType, taUserName);
		
		TAClass taClass = DataStore.getDataStore().findEntityById(getTable(), taClassKey);
		
		if(taClass == null){
			taClass = TAClass.getTAClass(classNum, classType, taUserName);
		}
		
		return taClass;
	}
	
	/**
	 * 
	 * @param classNum
	 * @param classType
	 * @param taUserName
	 */
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
		if(propertyKey == null)
			return new NullPointerException("Property Key for getProperty cannot be null!");
		
		switch(propertyKey.toLowerCase()){
		case "classnum":
			return getTAClass().getTAClassNum();
		case "classname":
			return getTAClass().getTAClassName();
		case "classtype":
			return getTAClass().getTAClassType();
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
			if(getTAClass() == null || getTAClass().getTAClassNum() == null){
				throw new NullPointerException("Class number is missing for addObject operation!");
			}
		}
		
		if(taClassType == null){
			if(getTAClass() == null || getTAClass().getTAClassType() == null){
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

	private String checkString(String propertyKey, Object propertyValue) {
		if(!(propertyValue instanceof String)) 
			throw new IllegalArgumentException(propertyKey + " must be of String type!");
		
		return (String) propertyValue;
	}

	private boolean setAllProperties(Map<String, Object> properties) {
		if(properties == null) throw new NullPointerException("Properties argument is null!");
		boolean newInfo = false;
		for(String propertyKey : properties.keySet()){
			try{
				newInfo |= setProperty(propertyKey, properties.get(propertyKey));
			}catch(ParseException pe){
				throw new IllegalArgumentException(pe.getMessage());
			}
		}
		
		return newInfo;
	}

	private boolean setProperty(String propertyKey, Object propertyValue) throws ParseException {
		double time;
		Date date;
		TAClass taClass = getTAClass();
		
		if(!checkNewInfo(propertyKey, propertyValue)) return false;
		
		switch(propertyKey.toLowerCase()){
		case "classname":
			taClass.setClassName((String)propertyValue);
			break;
		case "days":
			taClass.setDays((String) propertyValue);
			setDayBooleans((String) propertyValue);
			break;
		case "starttime":
			time = StringHelper.parseTimeToDouble((String) propertyValue);
			taClass.setStartTime(time);
			break;
		case "endtime":
			time = StringHelper.parseTimeToDouble((String) propertyValue);
			taClass.setEndTime(time);
			break;
		case "startdate":
			date = StringHelper.stringToDate((String) propertyValue);
			taClass.setStartDate(date);
			break;
		case "enddate":
			date = StringHelper.stringToDate((String) propertyValue);
			taClass.setEndDate(date);
			break;
		}
	
		return true;
	}

	private void setDayBooleans(String propertyValue) {
		TAClass taClass = getTAClass();
		resetOnDays(taClass);
		for(int i=0; i<propertyValue.length(); ++i){
			switch(propertyValue.charAt(i)){
			case 'M':
				taClass.setOnMonday(true);
				break;
			case 'T':
				taClass.setOnTuesday(true);
				break;
			case 'W':
				taClass.setOnWednesday(true);
				break;
			case 'R':
				taClass.setOnThursday(true);
				break;
			case 'F':
				taClass.setOnFriday(true);
				break;
			}
		}
	}

	private void resetOnDays(TAClass taClass) {
		taClass.setOnMonday(false);
		taClass.setOnTuesday(false);
		taClass.setOnWednesday(false);
		taClass.setOnThursday(false);
		taClass.setOnFriday(false);
	}

	private boolean checkNewInfo(String propertyKey, Object propertyValue) throws ParseException {
		boolean isNewInfo = true;
		TAClass taClass = getTAClass();
		
		switch(propertyKey.toLowerCase()){
		case "classname":
			String className = taClass.getTAClassName();
			if(className != null){
				isNewInfo = !className.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "days":
			String days = taClass.getDays();
			if(days != null){
				isNewInfo = !days.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "starttime":
			double newStartTime = StringHelper.parseTimeToDouble((String) propertyValue);
			double oldStartTime = taClass.getStartTime(); 
			isNewInfo = newStartTime != oldStartTime;
			break;
		case "endtime":
			double newEndTime = StringHelper.parseTimeToDouble((String) propertyValue);
			double oldEndTime = taClass.getEndTime();
			isNewInfo = newEndTime != oldEndTime;
			break;
		case "startdate":
			Date newStartDate = StringHelper.stringToDate((String) propertyValue);
			Date oldStartDate = taClass.getStartDate();
			if(oldStartDate != null){
				isNewInfo = !oldStartDate.equals(newStartDate);
			}
			break;
		case "enddate":
			Date newEndDate = StringHelper.stringToDate((String) propertyValue);
			Date oldEndDate = taClass.getEndDate();
			if(oldEndDate != null){
				isNewInfo = !oldEndDate.equals(newEndDate);
			}
			break;
		}
		
		return isNewInfo;
	}

	private List<WrapperObject<TAClass>> getTAClassFromList(
			List<TAClass> entities) {
		List<WrapperObject<TAClass>> taClasses = new ArrayList<WrapperObject<TAClass>>();
		for (TAClass item : entities)
			taClasses.add(TAClassWrapper.getTAClassWrapper(item));
		return taClasses;
	}

}
