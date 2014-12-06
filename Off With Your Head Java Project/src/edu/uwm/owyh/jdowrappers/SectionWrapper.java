package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.DataStore;

public class SectionWrapper implements WrapperObject<Section>, Serializable{

	public static final String SECTION_NUM_PATTERN = "^((LEC)|(DIS)|(LAB)|(IND)|(SEM)) \\d{3,4}$";
	public static final String SECTION_DATE_PATTERN = "^((0?[1-9])|(1[0-2]))/(([0-2][1-9])|(3[01]))$";
	private static final long serialVersionUID = -7911639006979553905L;

	private Section _section;

	private SectionWrapper(){
		//default constructor
		//		_section = Section.getSection();
	}

	private static WrapperObject<Section> getSectionWrapper(
			Section item) {
		SectionWrapper other = getSectionWrapper();
		other._section = item;

		return other;
	}

	public static SectionWrapper getSectionWrapper(){
		return new SectionWrapper();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getId()
	 */
	@Override
	public Key getId() {
		return _section.getId();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getTable()
	 */
	@Override
	public Class<Section> getTable() {
		return Section.getClassname();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;

		switch(propertyKey.toLowerCase()){
		case "instructorfirstname":
			return _section.getInstructorFirstName();
		case "instructorlastname":
			return _section.getInstructorLastName();
		case "sectionnum":
			return _section.getSectionNum();
		case "days":
			return _section.getDays();
		case "startdate":
			Date startDate = _section.getStartDate();
			return Library.dateToString(startDate);
		case "enddate":
			Date endDate = _section.getEndDate();
			return Library.dateToString(endDate);
		case "starttime":
			double startTime = _section.getStartTime();
			return Library.timeToString(startTime);
		case "endtime":
			double endTime = _section.getEndTime();
			return Library.timeToString(endTime);		
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> addObject(String courseNum, Map<String, Object> properties) {
		Key parentId = Library.generateIdFromCourseNum(courseNum);
		List<String> errors = new ArrayList<String>();

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ courseNum + " to add section to!");	

		errors = checkAllProperties(properties);

		if(!errors.isEmpty()) return errors;

		//		boolean isConflict = checkConflict(parent, properties);
		//
		//		if(isConflict){
		//			errors.add("New office hours conflict with other established office hours!");
		//			return errors;
		//		}

		setAllProperties(properties);

		return parent.addChildObject(getSection());
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(String id, Map<String, Object> properties) {

		Section childJDO = getSection();

		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentId = Library.generateIdFromUserName(id);
		List<String> errors = new ArrayList<String>();

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ id + " to edit office hours!");

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
		Section childJDO = getSection();		
		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentKey = Library.generateIdFromUserName(id);

		WrapperObject<Person> parent = WrapperObjectFactory.getPerson().findObjectById(parentKey);

		return parent.removeChildObject(childJDO);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<Section>> findObject(String filter, WrapperObject<T> parent) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Section>> officeHours = null;
		List<Section> entities = store.findEntities(getTable(), filter, parent);
		officeHours = getSectionFromList(entities);

		return officeHours;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObjectById(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public WrapperObject<Section> findObjectById(Key id) {
		DataStore store = DataStore.getDataStore();
		Section officeHours =  (Section) store.findEntityById(getTable(), id);

		if(officeHours == null) return null;

		return getSectionWrapper(officeHours);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getAllObjects()
	 */
	@Override
	public List<WrapperObject<Section>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Section>> officeHours = null;
		officeHours = getSectionFromList(store.findEntities(getTable(), null, null));

		return officeHours;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addChildObject(java.lang.Object)
	 */
	@Override
	public List<String> addChildObject(Object childJDO) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Section do not have any children entities");
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeChildObject(java.lang.Object)
	 */
	@Override
	public boolean removeChildObject(Object childJDO) throws UnsupportedOperationException{
		throw new UnsupportedOperationException("Section do not have any children entities");
	}

	private List<String> checkAllProperties(Map<String, Object> properties) {
		List<String> errors = new ArrayList<String>();

		String sectionNum = (String) properties.get("sectionnum");
		Date startDate = Library.stringToDate((String) properties.get("startdate"));
		Date endDate = Library.stringToDate((String) properties.get("enddate"));
		double startTime = Library.parseTimeToDouble((String) properties.get("starttime"));
		double endTime = Library.parseTimeToDouble((String)properties.get("endtime"));
		if(sectionNum == null){
			throw new NullPointerException("Section number is missing!");
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

		int argSize = properties.size();
		int expectedSize = Section.numProperties();
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
		start = Library.parseTimeToDouble(startTime);

		if(endTime == null || endTime.trim() == "") errors.add("Please enter a value for end time!");
		else if(!endTime.trim().matches(HOURS_PATTERN)) throw new IllegalArgumentException("Bad pattern for endTime!");
		end = Library.parseTimeToDouble(endTime);

		if(start >= end) errors.add("Office hours cannot start at or after end time");

		return errors;
	}

	private String checkProperty(String key, Object object) {
		// TODO Auto-generated method stub
		if(!(object instanceof String)) return key + " should have a string value!";

		String val = (String) object;

		switch(key.toLowerCase()){
		case "sectionnum":
			return checkSectionNum(val);
			String type = val.substring(0, 3);
			String num = val.substring(3).trim();

			switch(type.toUpperCase()){
			case "LEC":case "LAB":case "DIS":
			case "IND":case "SEM": break;
			default:
				return "Section must be of type: LEC,LAB,DIS,IND, or SEM";
			}

			num
		}


		return null;
	}

	private String checkSectionNum(String val) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setAllProperties(Map<String, Object> properties) {
		if(properties == null) throw new NullPointerException("Properties argument is null!");


		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	}

	@SuppressWarnings("unchecked")
	private boolean checkConflict(WrapperObject<Person> parent, Map<String, Object> properties) {
		List<WrapperObject<Section>> officeHours = (List <WrapperObject<Section>>) parent.getProperty("officehours");
		String days = (String) properties.get("days");
		double startTime = Library.parseTimeToDouble((String)properties.get("starttime"));
		double endTime  = Library.parseTimeToDouble((String) properties.get("endtime"));

		for(WrapperObject<Section> conflict : officeHours){

			boolean skip = conflict.equals(this);
			if(skip) continue;

			String compDays = (String) conflict.getProperty("days");
			double compStart = Library.parseTimeToDouble((String) conflict.getProperty("starttime"));
			double compEnd = Library.parseTimeToDouble((String)conflict.getProperty("endtime"));

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

	private List<WrapperObject<Section>> getSectionFromList(
			List<Section> entities) {
		List<WrapperObject<Section>> officeHours = new ArrayList<WrapperObject<Section>>();
		for (Section item : entities)
			officeHours.add(SectionWrapper.getSectionWrapper(item));
		return officeHours;
	}

	private Section getSection() {
		if(_officeHours == null) _officeHours = Section.getSection();

		return _officeHours;
	}

	private void setProperty(String propertyKey, Object object) {
		String propertyValue = ((String) object).trim();
		double time;
		Section officeHours = getSection();

		switch(propertyKey.toLowerCase()){
		case "days":
			officeHours.setDays(propertyValue);
			setDayBooleans(propertyValue);
			break;
		case "starttime":
			time = Library.parseTimeToDouble(propertyValue);
			officeHours.setStartTime(time);
			break;
		case "endtime":
			time = Library.parseTimeToDouble(propertyValue);
			officeHours.setEndTime(time);
			break;
		}


	}

	private void setDayBooleans(String propertyValue) {
		Section officeHours = getSection();
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

	private static void resetOnDays(Section officeHours){
		officeHours.setOnMonday(false);
		officeHours.setOnTuesday(false);
		officeHours.setOnWednesday(false);
		officeHours.setOnThursday(false);
		officeHours.setOnFriday(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof WrapperObject<?>)) return false;

		WrapperObject<Section> other = (WrapperObject<Section>) obj;

		String days = (String) this.getProperty("days");
		String otherDays = (String) other.getProperty("days");
		String startTime = (String) this.getProperty("starttime");
		String otherStartTime = (String) other.getProperty("starttime");
		String endTime = (String) this.getProperty("endtime");
		String otherEndTime = (String) other.getProperty("endtime");

		return days.equals(otherDays) && startTime.equals(otherStartTime) && endTime.equals(otherEndTime);
	}

	@Override 
	public int hashCode(){
		return this.getSection().hashCode();
	}

	public static void main(String[] args) throws ParseException{
		String sectionNum = "Sem 00121";
		System.out.println(sectionNum.toUpperCase() + " matches " + SECTION_NUM_PATTERN + ": " + sectionNum.toUpperCase().matches(SECTION_NUM_PATTERN));

		String date = "32/27";

		System.out.println(date + " matches " + SECTION_DATE_PATTERN + ": " + date.matches(SECTION_DATE_PATTERN));
		//		String date2 = "02/27";
		Date today = Library.stringToDate(date);
		//		Date tomorrow = Library.stringToDate(date2);
		//		
		System.out.println(today.toString());
		//		
		String todayString = Library.dateToString(today);
		//		
		System.out.println(todayString);	
		//		System.out.println("Today is before tomorrow: " + today.before(tomorrow));
		//		System.out.println("Tomorrow is before today: " + tomorrow.before(today));
	}

}
