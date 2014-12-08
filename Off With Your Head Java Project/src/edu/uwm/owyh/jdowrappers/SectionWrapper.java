package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.exceptions.BuildJDOException;
import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.library.NonPersistedWrapperObject;
import edu.uwm.owyh.model.DataStore;

public class SectionWrapper implements WrapperObject<Section>, Serializable, NonPersistedWrapperObject<Section>{

	public static final String SECTION_NUM_PATTERN = "^((LEC)|(DIS)|(LAB)|(IND)|(SEM)) \\d{3,4}$";
	public static final String SECTION_DATE_PATTERN = "^((0?[1-9])|(1[0-2]))/(([0-2][1-9])|(3[01]))$";
	private static final long serialVersionUID = -7911639006979553905L;

	Section _section;

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
			String sectionType = _section.getSectionType();
			String sectionNum = _section.getSectionNum();
			return sectionType + " " + sectionNum;
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
		case "credits":
			return _section.getCredits();
		case "room":
			return _section.getRoom();
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> addObject(String courseNum, Map<String, Object> properties) {
		List<String> errors = new ArrayList<String>();
		
		Key parentId = Library.generateIdFromCourseNum(courseNum);		

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ courseNum + " to add section to!");
		
		errors.addAll(buildNewSectionWrapper(courseNum, properties));		

		if(!errors.isEmpty()) return errors;
		
		return parent.addChildObject(getSection());
	}

	private void setSection(String courseNum, String sectionNum) {
		_section = getSection(courseNum, sectionNum);
		
	}

	private Section getSection(String courseNum, String sectionNum) {
		Key sectionKey = Library.generateSectionIdFromSectionAndCourseNum(sectionNum, courseNum);
		
		Section section = DataStore.getDataStore().findEntityById(getTable(), sectionKey);
		
		if(section == null){
			section = Section.getSection(sectionNum, courseNum);
		}
		
		return section;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(String courseNum, Map<String, Object> properties) {

		Section childJDO = getSection();

		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentId = Library.generateIdFromCourseNum(courseNum);
		List<String> errors = new ArrayList<String>();

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ courseNum + " to edit section!");

		try {
			errors = checkAllProperties(properties);
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe.getMessage());
		}

		if(!errors.isEmpty()) return errors;
		
		String sectionNum = (String)properties.get("sectionnum");
		setSection(courseNum, sectionNum);
		
		if(!setAllProperties(properties)) return errors;

		if(!DataStore.getDataStore().updateEntity(_section, getId())){
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

		Key parentKey = Library.generateIdFromCourseNum(id);

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentKey);

		return parent.removeChildObject(childJDO);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<Section>> findObject(String filter, WrapperObject<T> parent, String order) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Section>> sections = null;
		List<Section> entities = store.findEntities(getTable(), filter, parent, order);
		sections = getSectionFromList(entities);

		return sections;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObjectById(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public WrapperObject<Section> findObjectById(Key id) {
		DataStore store = DataStore.getDataStore();
		Section section =  (Section) store.findEntityById(getTable(), id);

		if(section == null) return null;

		return getSectionWrapper(section);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getAllObjects()
	 */
	@Override
	public List<WrapperObject<Section>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Section>> sections = null;
		sections = getSectionFromList(store.findEntities(getTable(), null, null, null));

		return sections;
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

	private List<String> checkAllProperties(Map<String, Object> properties) throws ParseException {
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
		return errors;
	}

	private String checkProperty(String key, Object object) {
		if(!(object instanceof String)) return key + " should have a string value!";

		String val = (String) object;

		switch(key.toLowerCase()){
		case "sectionnum":
			if(!val.matches(SECTION_NUM_PATTERN))
				throw new IllegalArgumentException("Section number does not match expected pattern: "
						+ SECTION_NUM_PATTERN);
			break;
		case "enddate":case "startdate":
			if(!val.matches(SECTION_DATE_PATTERN))
				throw new IllegalArgumentException("Start or end date does not match expected pattern: "
						+ SECTION_DATE_PATTERN);
			break;
		case "starttime":case "endtime":
			if(!val.matches(OfficeHoursWrapper.HOURS_PATTERN)&& !val.trim().isEmpty())
				throw new IllegalArgumentException("Start or end time does not match expected pattern: " 
						+ OfficeHoursWrapper.HOURS_PATTERN);
			break;
		case "days":
			if(!val.matches(OfficeHoursWrapper.DAYS_PATTERN))
				throw new IllegalArgumentException("Days does not match expected pattern: "
						+ OfficeHoursWrapper.DAYS_PATTERN);
		}


		return "";
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
	
	private List<WrapperObject<Section>> getSectionFromList(
			List<Section> entities) {
		List<WrapperObject<Section>> officeHours = new ArrayList<WrapperObject<Section>>();
		for (Section item : entities)
			officeHours.add(SectionWrapper.getSectionWrapper(item));
		return officeHours;
	}

	private Section getSection() {
		if(_section == null) _section= Section.getSection();

		return _section;
	}

	private boolean setProperty(String propertyKey, Object object) throws ParseException {
		String propertyValue = ((String) object).trim();
		double time;
		Date date;
		Section section = getSection();
		
		if(!checkNewInfo(propertyKey, propertyValue)) return false;
		
		switch(propertyKey.toLowerCase()){
		case "days":
			section.setDays(propertyValue);
			setDayBooleans(propertyValue);
			break;
		case "starttime":
			time = Library.parseTimeToDouble(propertyValue);
			section.setStartTime(time);
			break;
		case "endtime":
			time = Library.parseTimeToDouble(propertyValue);
			section.setEndTime(time);
			break;
		case "startdate":
			date = Library.stringToDate(propertyValue);
			section.setStartDate(date);
			break;
		case "enddate":
			date = Library.stringToDate(propertyValue);
			section.setEndDate(date);
			break;
		case "credits":
			section.setCredits(propertyValue);
			break;
		case "instructorfirstname":
			section.setInstructorFirstName(propertyValue);
			break;
		case "instructorlastname":
			section.setInstructorLastName(propertyValue);
			break;
		case "room":
			section.setRoom(propertyValue);
			break;
		}

		return true;
	}

	private boolean checkNewInfo(String propertyKey, String propertyValue) throws ParseException {
		boolean isNewInfo = true;
		Section section = getSection();
		
		switch(propertyKey.toLowerCase()){
		case "days":
			String days = section.getDays();
			if(days != null){
				isNewInfo = !days.equalsIgnoreCase(propertyValue);
			}
			break;
		case "starttime":
			double newStartTime = Library.parseTimeToDouble(propertyValue);
			double oldStartTime = section.getStartTime(); 
			isNewInfo = newStartTime != oldStartTime;
			break;
		case "endtime":
			double newEndTime = Library.parseTimeToDouble(propertyValue);
			double oldEndTime = section.getEndTime();
			isNewInfo = newEndTime != oldEndTime;
			break;
		case "startdate":
			Date newStartDate = Library.stringToDate(propertyValue);
			Date oldStartDate = section.getStartDate();
			if(oldStartDate != null){
				isNewInfo = !oldStartDate.equals(newStartDate);
			}
			break;
		case "enddate":
			Date newEndDate = Library.stringToDate(propertyValue);
			Date oldEndDate = section.getEndDate();
			if(oldEndDate != null){
				isNewInfo = !oldEndDate.equals(newEndDate);
			}
			break;
		case "credits":
			String oldCredits = section.getCredits();
			if(oldCredits != null){
				isNewInfo = !oldCredits.equalsIgnoreCase(propertyValue);
			}
			break;
		case "instructorfirstname":
			String oldFirstName = section.getInstructorFirstName();
			if(oldFirstName != null){
				isNewInfo = !oldFirstName.equalsIgnoreCase(propertyValue);
			}
			break;
		case "instructorlastname":
			String oldLastName = section.getInstructorLastName();
			if(oldLastName != null){
				isNewInfo = !oldLastName.equalsIgnoreCase(propertyValue);
			}
			break;
		case "room":
			String oldRoom = section.getRoom();
			if(oldRoom != null){
				isNewInfo = !oldRoom.equalsIgnoreCase(propertyValue);
			}
			break;
		}
		
		return isNewInfo;
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

	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof SectionWrapper)) return false;

		SectionWrapper other = (SectionWrapper) obj;

		return this._section.equals(other._section);
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

	@Override
	public WrapperObject<Section> buildObject(String courseNum, Map<String, Object> properties) throws BuildJDOException {
		List<String> errors = buildNewSectionWrapper(courseNum, properties);
		
		if(!errors.isEmpty()) throw new BuildJDOException(errors);
		
		return getSectionWrapper(_section);
	}

	@Override
	public boolean saveAllObjects(List<WrapperObject<Section>> objects) {
		List<Section> sections = new ArrayList<Section>();
		
		for(WrapperObject<Section> object : objects){
			if(!(object instanceof SectionWrapper)) continue;
			
			SectionWrapper section = (SectionWrapper)object;
			
			sections.add(section.getSection());
		}
		
		return DataStore.getDataStore().insertEntities(sections);
	}

	@Override
	public boolean deleteAllObjects(String kind) {
		if(!kind.equalsIgnoreCase(Section.getKind())) return false;
		
		List<WrapperObject<Section>> sectionWrappers = getAllObjects();
		
		List<Section> sections = new ArrayList<Section>();
		
		for(WrapperObject<Section> object : sectionWrappers){
			if(!(object instanceof SectionWrapper)) continue;
			
			SectionWrapper section = (SectionWrapper)object;
			
			sections.add(section.getSection());
		}
		
		return DataStore.getDataStore().deleteAllEntities(sections);
	}

	private List<String> buildNewSectionWrapper(String courseNum, Map<String,Object> properties){
		List<String> errors = new ArrayList<String>();	
		
		try{
			errors = checkAllProperties(properties);
		}catch(ParseException pe){
			throw new IllegalArgumentException(pe.getMessage());
		}

		if(!errors.isEmpty()) return errors;
		
		String sectionNum = (String)properties.get("sectionnum");
		
		Key sectionKey = Library.generateSectionIdFromSectionAndCourseNum(sectionNum, courseNum);
		
		if(findObjectById(sectionKey) != null){
			throw new IllegalArgumentException("COMPSCI-" + courseNum + ":" + sectionNum + "- Duplicate Section exists!");
		}		
		
		setSection(courseNum, sectionNum);

		setAllProperties(properties);
		
		return errors;
	}

	@Override
	public boolean addChild(WrapperObject<?> child) {
		return false;
	}
}
