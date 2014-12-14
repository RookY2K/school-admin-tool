package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.library.StringHelper;
import edu.uwm.owyh.model.DataStore;


/**
 * Wrapper class for the OfficeHours JDO class
 * Attempts to limit direct interaction with the OfficeHours JDO class
 * Implements WrapperObject<E> interface
 * @author Vince Maiuri
 */
public class OfficeHoursWrapper implements WrapperObject<OfficeHours>, Serializable{

	private static final long serialVersionUID = 6788916101743070177L;
	public static final String OFFICEHOURPATTERN = "^(M?T?W?R?F?) (\\d{2}|\\d):(\\d{2})(A|P)M-(\\d{2}|\\d):(\\d{2})(A|P)M$";
	public static final String DAYS_PATTERN = "^M?T?W?R?F?$";
	public static final String HOURS_PATTERN = "^(\\d{2}|\\d):(\\d{2})\\s{0,1}(A|P)M$";

	private OfficeHours _officeHours;

	private OfficeHoursWrapper(){
		//default constructor
		_officeHours = OfficeHours.getOfficeHours();
	}

	static WrapperObject<OfficeHours> getOfficeHoursWrapper(OfficeHours item) {
		OfficeHoursWrapper other = getOfficeHoursWrapper();
		other._officeHours = item;
		
		return other;
	}

	public static OfficeHoursWrapper getOfficeHoursWrapper(){
		return new OfficeHoursWrapper();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getId()
	 */
	@Override
	public Key getId() {
		return _officeHours.getId();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getTable()
	 */
	@Override
	public Class<OfficeHours> getTable() {
		return OfficeHours.getClassname();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;

		switch(propertyKey.toLowerCase()){
		case "days":
			return _officeHours.getDays();
		case "starttime":
			double startTime = _officeHours.getStartTime();
			String startTimeString = StringHelper.timeToString(startTime);
			return startTimeString;
		case "endtime":
			double endTime = _officeHours.getEndTime();
			String endTimeString = StringHelper.timeToString(endTime);
			return endTimeString;
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> addObject(String id, Map<String, Object> properties) {
		Key parentId = WrapperObjectFactory.generateIdFromUserName(id);
		List<String> errors = new ArrayList<String>();

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ id + " to add office hours to!");	
		
		errors = checkAllProperties(properties);

		if(!errors.isEmpty()) return errors;

		boolean isConflict = checkConflict(parent, properties);

		if(isConflict){
			errors.add("New office hours conflict with other established office hours!");
			return errors;
		}

		setAllProperties(properties);

		return parent.addChildObject(getOfficeHours());
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(Map<String, Object> properties) {
	
		OfficeHours childJDO = getOfficeHours();
	
		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");
	
		Key parentId = childJDO.getId().getParent();
		List<String> errors = new ArrayList<String>();
	
		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);
	
		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ parentId.getName() + " to edit office hours!");
	
		errors = checkAllProperties(properties);
		
		if(!errors.isEmpty()) return errors;
		
		boolean isConflict = checkConflict(parent, properties);
		
		if(isConflict){
			errors.add("Edited office hours conflict with established office hours!");
			return errors;
		}
	
		setAllProperties(properties);
	
		if(!DataStore.getDataStore().updateEntity(_officeHours, getId())){
			errors.add("Unknown datastore error when updating!");
		}
		
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeObject(java.lang.String)
	 */
	@Override
	public boolean removeObject(String id) {
		OfficeHours childJDO = getOfficeHours();		
		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");
	
		Key parentKey = WrapperObjectFactory.generateIdFromUserName(id);
	
		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentKey);
	
		return parent.removeChildObject(childJDO);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<OfficeHours>> findObjects(String filter, WrapperObject<T> parent, String order) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<OfficeHours>> officeHours = null;
		List<OfficeHours> entities = store.findEntities(getTable(), filter, parent, order);
		officeHours = getOfficeHoursFromList(entities);
		
		return officeHours;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObjectById(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public WrapperObject<OfficeHours> findObjectById(Key id) {
		DataStore store = DataStore.getDataStore();
		OfficeHours officeHours =  (OfficeHours) store.findEntityById(getTable(), id);
		
		if(officeHours == null) return null;
		
		return getOfficeHoursWrapper(officeHours);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getAllObjects()
	 */
	@Override
	public List<WrapperObject<OfficeHours>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<OfficeHours>> officeHours = null;
		officeHours = getOfficeHoursFromList(store.findEntities(getTable(), null, null, null));
	
		return officeHours;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addChildObject(java.lang.Object)
	 */
	@Override
	public List<String> addChildObject(Object childJDO) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("OfficeHours do not have any children entities");
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeChildObject(java.lang.Object)
	 */
	@Override
	public boolean removeChildObject(Object childJDO) throws UnsupportedOperationException{
		throw new UnsupportedOperationException("OfficeHours do not have any children entities");
	}

	private List<String> checkAllProperties(Map<String, Object> properties) {
		List<String> errors = new ArrayList<String>();
		int argSize = properties.size();
		int expectedSize = OfficeHours.numProperties();
		String days = (String) properties.get("days");
		String startTime = (String) properties.get("starttime");
		String endTime = (String) properties.get("endtime");
		double start = -1;
		double end = -1;
		
		
		if(argSize != expectedSize) 
			throw new IllegalArgumentException("Expected " + expectedSize + " arguments. "
					+ "Actual number was " + argSize);
	
		if(days == null || days.trim() == "") errors.add("Please check at least one day!");
		else if(!days.trim().matches(DAYS_PATTERN)) throw new IllegalArgumentException("Bad pattern for days!");
		
		if(startTime == null || startTime.trim() == "") errors.add("Please enter a value for start time!");
		else if(!startTime.trim().matches(HOURS_PATTERN)) throw new IllegalArgumentException("Bad pattern for startTime!");
		start = StringHelper.parseTimeToDouble(startTime);
		
		if(endTime == null || endTime.trim() == "") errors.add("Please enter a value for end time!");
		else if(!endTime.trim().matches(HOURS_PATTERN)) throw new IllegalArgumentException("Bad pattern for endTime!");
		end = StringHelper.parseTimeToDouble(endTime);
		
		if(start >= end) errors.add("Office hours cannot start at or after end time");
		
		return errors;
	}

	private void setAllProperties(Map<String, Object> properties) {
		if(properties == null) throw new NullPointerException("Properties argument is null!");
	
	
		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	}

	@SuppressWarnings("unchecked")
	private boolean checkConflict(WrapperObject<Person> parent, Map<String, Object> properties) {
		List<WrapperObject<OfficeHours>> officeHours = (List <WrapperObject<OfficeHours>>) parent.getProperty("officehours");
		String days = (String) properties.get("days");
		double startTime = StringHelper.parseTimeToDouble((String)properties.get("starttime"));
		double endTime  = StringHelper.parseTimeToDouble((String) properties.get("endtime"));
		
		for(WrapperObject<OfficeHours> conflict : officeHours){
			
			boolean skip = conflict.equals(this);
			if(skip) continue;
			
			String compDays = (String) conflict.getProperty("days");
			double compStart = StringHelper.parseTimeToDouble((String) conflict.getProperty("starttime"));
			double compEnd = StringHelper.parseTimeToDouble((String)conflict.getProperty("endtime"));
			
			for(int i=0;i<days.length();++i){
				String day = Character.toString(days.charAt(i));
				
				if(compDays.contains(day)){
					boolean isStartConflict = startTime >= compStart && startTime < compEnd;
					boolean isEndConflict = endTime > compStart && endTime <=compEnd;
					
					if(isStartConflict || isEndConflict){
						return true;
					}
				}
			}
		}

		return false;
	}

	private List<WrapperObject<OfficeHours>> getOfficeHoursFromList(
			List<OfficeHours> entities) {
		List<WrapperObject<OfficeHours>> officeHours = new ArrayList<WrapperObject<OfficeHours>>();
		for (OfficeHours item : entities)
			officeHours.add(OfficeHoursWrapper.getOfficeHoursWrapper(item));
		return officeHours;
	}

	private OfficeHours getOfficeHours() {
		if(_officeHours == null) _officeHours = OfficeHours.getOfficeHours();

		return _officeHours;
	}

	private void setProperty(String propertyKey, Object object) {
		String propertyValue = ((String) object).trim();
		double time;
		OfficeHours officeHours = getOfficeHours();

		switch(propertyKey.toLowerCase()){
		case "days":
			officeHours.setDays(propertyValue);
			setDayBooleans(propertyValue);
			break;
		case "starttime":
			time = StringHelper.parseTimeToDouble(propertyValue);
			officeHours.setStartTime(time);
			break;
		case "endtime":
			time = StringHelper.parseTimeToDouble(propertyValue);
			officeHours.setEndTime(time);
			break;
		}


	}

	private void setDayBooleans(String propertyValue) {
		OfficeHours officeHours = getOfficeHours();
		resetOnDays(officeHours);
		for(int i=0; i<propertyValue.length(); ++i){
			switch(propertyValue.charAt(i)){
			case 'M':
				officeHours.setOnMonday(true);
				break;
			case 'T':
				officeHours.setOnTuesday(true);
				break;
			case 'W':
				officeHours.setOnWednesday(true);
				break;
			case 'R':
				officeHours.setOnThursday(true);
				break;
			case 'F':
				officeHours.setOnFriday(true);
				break;
			}
		}
		
	}
	
	private static void resetOnDays(OfficeHours officeHours){
		officeHours.setOnMonday(false);
		officeHours.setOnTuesday(false);
		officeHours.setOnWednesday(false);
		officeHours.setOnThursday(false);
		officeHours.setOnFriday(false);
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof OfficeHoursWrapper)) return false;
		
		OfficeHoursWrapper other = (OfficeHoursWrapper) obj;
		
		return this._officeHours.equals(other._officeHours);
	}
	
	@Override 
	public int hashCode(){
		return this.getOfficeHours().hashCode();
	}

	@Override
	public boolean removeObjects(List<WrapperObject<OfficeHours>> officeHours) {
		List<OfficeHours> officeHoursList = new ArrayList<OfficeHours>();
		
		for(WrapperObject<OfficeHours> hours : officeHours){
			OfficeHoursWrapper officeHoursWrapper = (OfficeHoursWrapper)hours;
			
			officeHoursList.add(officeHoursWrapper.getOfficeHours());
		}
		return DataStore.getDataStore().deleteAllEntities(officeHoursList);
	}

}
