package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.DataStore;


public class OfficeHoursWrapper implements WrapperObject<OfficeHours>, Serializable{

	private static final long serialVersionUID = 6788916101743070177L;
	public static final String OFFICEHOURPATTERN = "^(M?T?W?R?F?) (\\d{2}|\\d):(\\d{2})(A|P)M-(\\d{2}|\\d):(\\d{2})(A|P)M$";
	public static final String DAYS_PATTERN = "^M?T?W?R?F?$";
	public static final String HOURS_PATTERN = "^(\\d{2}|\\d):(\\d{2})(A|P)M$";

	private OfficeHours _officeHours;

	private OfficeHoursWrapper(){
		//default constructor
		_officeHours = OfficeHours.getOfficeHours();
	}

	public static OfficeHoursWrapper getOfficeHoursWrapper(){
		return new OfficeHoursWrapper();
	}


	@Override
	public Key getId() {
		return _officeHours.getId();
	}

	@Override
	public Class<OfficeHours> getTable() {
		return OfficeHours.getClassname();
	}

	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;

		switch(propertyKey.toLowerCase()){
		case "days":
			return _officeHours.getDays();
		case "starttime":
			double startTime = _officeHours.getStartTime();
			String startTimeString = parseTimeToString(startTime);
			return startTimeString;
		case "endtime":
			double endTime = _officeHours.getEndTime();
			String endTimeString = parseTimeToString(endTime);
			return endTimeString;
		default:
			return null;
		}
	}

	@Override
	public List<String> addObject(String id, Map<String, Object> properties) {
		Key parentId = Library.generateIdFromUserName(id);
		List<String> errors = new ArrayList<String>();

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ id + " to add office hours to!");	
		
		errors = checkAllProperties(properties);

		if(!errors.isEmpty()) return errors;

		Boolean isConflict = checkConflict(parent, properties);

		if(isConflict){
			errors.add("New office hours conflict with other established office hours!");
			return errors;
		}

		setAllProperties(properties);

		return parent.addChildObject(getOfficeHours());
	}

	@SuppressWarnings("unchecked")
	private boolean checkConflict(WrapperObject<Person> parent, Map<String, Object> properties) {
		List<OfficeHours> officeHours = (List<OfficeHours>) parent.getProperty("officehours");
		String days = (String) properties.get("days");
		double startTime = parseTimeToDouble((String)properties.get("starttime"));
		double endTime  = parseTimeToDouble((String) properties.get("endtime"));

		for(OfficeHours conflict : officeHours){
			String compDays = conflict.getDays();
			double compStart = conflict.getStartTime();
			double compEnd = conflict.getEndTime();
			for(int i=0;i<days.length();++i){
				String day = Character.toString(days.charAt(i));
				if(compDays.contains(day)){
					if(startTime >= compStart && startTime <= compEnd 
							|| endTime >= compStart && endTime <= endTime){
						return true;
					}
				}
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> editObject(String id, Map<String, Object> properties) {

		OfficeHours childJDO = getOfficeHours();

		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentId = Library.generateIdFromUserName(id);
		List<String> errors = new ArrayList<String>();

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ id + " to edit office hours!");

		errors = checkAllProperties(properties);
		
		boolean isConflict = checkConflict(parent, properties);
		
		if(isConflict){
			errors.add("Edited office hours conflict with established office hours!");
			return errors;
		}
		
		List<OfficeHours> childHours = (List<OfficeHours>) parent.getProperty("officehours");

		OfficeHours editedHours = OfficeHours.getOfficeHours();		
		editedHours.setDays((String) properties.get("days"));
		editedHours.setStartTime(parseTimeToDouble((String)properties.get("starttime")));
		editedHours.setEndTime(parseTimeToDouble((String)properties.get("endtime")));

		Boolean isDuplicate = childHours.contains(editedHours);

		if(isDuplicate){
			errors.add("Duplicate Office hours!");
			return errors;
		}

		setAllProperties(properties);

		if(!DataStore.getDataStore().updateEntity(_officeHours, getId())){
			errors.add("Unknown datastore error when updating!");
		}
		
		return errors;
	}

	@Override
	public boolean removeObject(String id) {
		OfficeHours childJDO = getOfficeHours();		
		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentKey = Library.generateIdFromUserName(id);

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentKey);

		return parent.removeChildObject(childJDO);
	}

	@Override
	public List<WrapperObject<OfficeHours>> findObject(String filter) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<OfficeHours>> officeHours = null;
		List<OfficeHours> entities = store.findEntities(getTable(), filter);
		officeHours = getOfficeHoursFromList(entities);
		
		return officeHours;
	}

	private List<WrapperObject<OfficeHours>> getOfficeHoursFromList(
			List<OfficeHours> entities) {
		List<WrapperObject<OfficeHours>> officeHours = new ArrayList<WrapperObject<OfficeHours>>();
		for (OfficeHours item : entities)
			officeHours.add(OfficeHoursWrapper.getOfficeHoursWrapper(item));
		return officeHours;
	}

	private static WrapperObject<OfficeHours> getOfficeHoursWrapper(
			OfficeHours item) {
		OfficeHoursWrapper other = getOfficeHoursWrapper();
		other._officeHours = item;
		
		return other;
	}

	@Override
	public WrapperObject<OfficeHours> findObjectById(Key id) {
		DataStore store = DataStore.getDataStore();
		OfficeHours officeHours =  (OfficeHours) store.findEntityById(getTable(), id);
		
		if(officeHours == null) return null;
		
		return getOfficeHoursWrapper(officeHours);
	}

	@Override
	public List<WrapperObject<OfficeHours>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<OfficeHours>> officeHours = null;
			
		officeHours = getOfficeHoursFromList(store.findEntities(getTable(), null));
	
		return officeHours;
	}

	@Override
	public List<String> addChildObject(Object childJDO) {
		throw new UnsupportedOperationException("OfficeHours do not have any children entities");
	}


	@Override
	public boolean removeChildObject(Object childJDO) {
		throw new UnsupportedOperationException("OfficeHours do not have any children entities");
	}

	private static String parseTimeToString(double time) {
		int hoursIn24Cycle = (int)time;
		double fractionalMinutes = time - hoursIn24Cycle;

		long minutes = Math.round(fractionalMinutes * 60);
		int hoursIn12Cycle;
		boolean isPm = false;
		if(hoursIn24Cycle > 12){
			hoursIn12Cycle = hoursIn24Cycle - 12;
			isPm = true;
		}else{
			hoursIn12Cycle = hoursIn24Cycle;
			if(hoursIn12Cycle == 12) isPm = true;
		}
		String AmPm = isPm ? "PM" : "AM";
		String minutesString = Long.toString(minutes);

		minutesString = minutesString.length() == 1 ? "0" + minutesString : minutesString;



		return "" + hoursIn12Cycle + ":" + minutesString + AmPm;
	}

	private OfficeHours getOfficeHours() {
		if(_officeHours == null) _officeHours = OfficeHours.getOfficeHours();

		return _officeHours;
	}

	private void setAllProperties(Map<String, Object> properties) {
		if(properties == null) throw new NullPointerException("Properties argument is null!");


		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	}

	private void setProperty(String propertyKey, Object object) {
		String propertyValue = ((String) object).trim();
		double time;
		OfficeHours officeHours = getOfficeHours();

		switch(propertyKey.toLowerCase()){
		case "days":
			officeHours.setDays(propertyValue);
			break;
		case "starttime":
			time = parseTimeToDouble(propertyValue);
			officeHours.setStartTime(time);
			break;
		case "endtime":
			time = parseTimeToDouble(propertyValue);
			officeHours.setEndTime(time);
			break;
		}


	}

	private double parseTimeToDouble(String time) {
		double hours = parseHours(time);
		double minutes = parseMinutes(time)/60;
		String AmPm = parseAmPm(time);

		if(AmPm.equalsIgnoreCase("pm")){
			if(hours != 12) hours += 12;
		}else{
			if(hours == 12) hours += 12;
		}

		return hours + minutes;
	}

	private String parseAmPm(String time) {
		int startIndex = time.length() - 2;
		String AmPm = time.substring(startIndex);

		if(!(AmPm.equalsIgnoreCase("am") || AmPm.equalsIgnoreCase("pm"))){
			throw new IllegalArgumentException("Am or Pm was not appended to the time!");
		}

		return AmPm;
	}

	private double parseMinutes(String time) { 
		int startIndex = time.indexOf(':') + 1;
		int endIndex = startIndex + 2;

		if(startIndex == -1)
			throw new IllegalArgumentException("Missing ':' separator between hours and minutes!");

		String minutes = time.substring(startIndex, endIndex);

		double numMinutes = -1;

		try{
			numMinutes = Double.parseDouble(minutes);
		}catch(NumberFormatException nfe){
			throw new IllegalArgumentException("Minutes portion of the time was not a number");
		}

		if(numMinutes >= 60 || numMinutes < 0){
			throw new IllegalArgumentException("Minutes should be between 0 and 59!");
		}

		return numMinutes;
	}

	private double parseHours(String time) {
		int startIndex = 0;
		int endIndex = time.indexOf(':');

		if(endIndex == -1)
			throw new IllegalArgumentException("Missing ':' separator between hours and minutes!");

		String hours = time.substring(startIndex, endIndex);

		double numHours = -1;

		try{
			numHours = Double.parseDouble(hours);
		}catch(NumberFormatException nfe){
			throw new IllegalArgumentException("Hours portion of the time was not a number!");
		}

		if(numHours > 12 || numHours <= 0)
			throw new IllegalArgumentException("Hours should be between 1 and 12!");

		return numHours;		
	}

	private List<String> checkAllProperties(Map<String, Object> properties) {
		List<String> errors = new ArrayList<String>();
		int argSize = properties.size();
		int expectedSize = OfficeHours.numProperties();

		if(argSize != expectedSize) 
			throw new IllegalArgumentException("Expected " + expectedSize + " arguments. "
					+ "Actual number was " + argSize);

		String days = (String) properties.get("days");
		if(days == null || days.trim() == null) errors.add("Please check at least one day!");
		else if(!days.trim().matches(DAYS_PATTERN)) throw new IllegalArgumentException("Bad pattern for days!");
		String startTime = (String) properties.get("starttime");
		if(startTime == null || startTime.trim() == null) errors.add("Please enter a value for start time!");
		else if(!startTime.trim().matches(HOURS_PATTERN)) throw new IllegalArgumentException("Bad pattern for startTime!");
		String endTime = (String) properties.get("endtime");
		if(endTime == null || endTime.trim() == null) errors.add("Please enter a value for end time!");
		else if(!endTime.trim().matches(HOURS_PATTERN)) throw new IllegalArgumentException("Bad pattern for endTime!");

		return errors;
	}

	/*
	public static void main(String[] args){
		for(double i = 0; i < 60; i += 1){
			double minutes = i/60;
			double hours = 14.0;

			double time = hours + minutes;

			String timeString = parseTimeToString(time);

			System.out.println("The time is: " + timeString);
		}
	}
	 */

}
