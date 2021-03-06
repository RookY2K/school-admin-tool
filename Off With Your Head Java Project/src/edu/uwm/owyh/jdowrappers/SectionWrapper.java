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
import edu.uwm.owyh.interfaces.NonPersistedWrapperObject;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.library.StringHelper;
import edu.uwm.owyh.model.DataStore;

public class SectionWrapper implements WrapperObject<Section>, Serializable, NonPersistedWrapperObject<Section>{

	public static final String SECTION_NUM_PATTERN = "^((LEC)|(DIS)|(LAB)|(IND)|(SEM)) \\d{3,4}$";
	public static final String DATE_PATTERN = "^((0?[1-9])|(1[0-2]))/((0?[1-9])|([12][0-9])|(3[01]))$";
	private static final long serialVersionUID = -7911639006979553905L;

	private Section _section;

	private SectionWrapper(){
		//default constructor
	}

	static WrapperObject<Section> getSectionWrapper(
			Section item) {
		SectionWrapper other = getSectionWrapper();
		other._section = item;

		return other;
	}

	public static SectionWrapper getSectionWrapper(){
		return new SectionWrapper();
	}

	Section getSection() {
		if(_section == null) _section= Section.getSection();
	
		return _section;
	}

	private Section getSection(String courseNum, String sectionNum) {
		Key sectionKey = WrapperObjectFactory.generateSectionIdFromSectionAndCourseNum(sectionNum, courseNum);
		
		Section section = DataStore.getDataStore().findEntityById(getTable(), sectionKey);
		
		if(section == null){
			section = Section.getSection(sectionNum, courseNum);
		}
		
		return section;
	}

	private void setSection(String courseNum, String sectionNum) {
		_section = getSection(courseNum, sectionNum);		
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
			return StringHelper.dateToString(startDate);
		case "enddate":
			Date endDate = _section.getEndDate();
			return StringHelper.dateToString(endDate);
		case "starttime":
			double startTime = _section.getStartTime();
			return StringHelper.timeToString(startTime);
		case "endtime":
			double endTime = _section.getEndTime();
			return StringHelper.timeToString(endTime);	
		case "credits":
			return _section.getCredits();
		case "room":
			return _section.getRoom();
		case "overwriteinstructor":
			return _section.isOverwriteInstructor();
		case "instructor":
			Person instructor = _section.getInstructor();
			if(instructor == null) return null;
			return PersonWrapper.getPersonWrapper(instructor);
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
		
		Key parentId = WrapperObjectFactory.generateIdFromCourseNum(courseNum);		

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ courseNum + " to add section to!");
		
		errors.addAll(buildNewSectionWrapper(courseNum, properties));		

		if(!errors.isEmpty()) return errors;
		
		return parent.addChildObject(getSection());
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(Map<String, Object> properties) {

		Section childJDO = getSection();

		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentId = childJDO.getId().getParent();
		List<String> errors = new ArrayList<String>();

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentId);

		if(parent == null) throw new IllegalArgumentException("No parent exists with ID: " 
				+ parentId.getName() + " to edit section!");

		try {
			errors = checkAllProperties(properties);
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe.getMessage());
		}

		if(!errors.isEmpty()) return errors;
		
//		String sectionNum = (String)properties.get("sectionnum");
//		setSection(courseNum, sectionNum);
		
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
	public boolean removeObject() {
		Section childJDO = getSection();		
		if(childJDO.getId() == null) throw new IllegalStateException("Calling object is not a Persisted JDO!");

		Key parentKey = childJDO.getId().getParent();

		WrapperObject<Course> parent = WrapperObjectFactory.getCourse().findObjectById(parentKey);

		return parent.removeChildObject(childJDO);
	}

	@Override
	public boolean removeObjects(List<WrapperObject<Section>> sections) {
		List<Section> sectionList = new ArrayList<Section>();
		
		for(WrapperObject<Section> sectionWrapper : sections){
			SectionWrapper section = (SectionWrapper)sectionWrapper;
			
			sectionList.add(section.getSection());
		}
		
		return DataStore.getDataStore().deleteAllEntities(sections);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<Section>> findObjects(String filter, WrapperObject<T> parent, String order) {
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
	public boolean addChild(WrapperObject<?> child) {
		throw new UnsupportedOperationException("Section do not have any children entities");
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
		
		Key sectionKey = WrapperObjectFactory.generateSectionIdFromSectionAndCourseNum(sectionNum, courseNum);
		
		if(findObjectById(sectionKey) != null){
			throw new IllegalArgumentException("COMPSCI-" + courseNum + ":" + sectionNum + "- Duplicate Section exists!");
		}		
		
		setSection(courseNum, sectionNum);
	
		setAllProperties(properties);
		
		return errors;
	}

	private List<String> checkAllProperties(Map<String, Object> properties) throws ParseException {
		List<String> errors = new ArrayList<String>();
	
		String sectionNum = (String) properties.get("sectionnum");
		Date startDate = StringHelper.stringToDate((String) properties.get("startdate"));
		Date endDate = StringHelper.stringToDate((String) properties.get("enddate"));
		double startTime = StringHelper.parseTimeToDouble((String) properties.get("starttime"));
		double endTime = StringHelper.parseTimeToDouble((String)properties.get("endtime"));
		if(sectionNum == null){
			if(_section == null || _section.getSectionNum() == null){
				throw new NullPointerException("Section number is missing!");
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

	private String checkProperty(String key, Object object) {
		String val;
		
		switch(key.toLowerCase()){
		case "sectionnum":
			val = checkString(key,object);
			if(!val.matches(SECTION_NUM_PATTERN))
				throw new IllegalArgumentException("Section number does not match expected pattern: "
						+ SECTION_NUM_PATTERN);
			break;
		case "enddate":case "startdate":
			val = checkString(key,object);
			if(!val.matches(DATE_PATTERN))
				throw new IllegalArgumentException("Start or end date does not match expected pattern: "
						+ DATE_PATTERN);
			break;
		case "starttime":case "endtime":
			val = checkString(key,object);
			if(!val.matches(OfficeHoursWrapper.HOURS_PATTERN)&& !val.trim().isEmpty())
				throw new IllegalArgumentException("Start or end time does not match expected pattern: " 
						+ OfficeHoursWrapper.HOURS_PATTERN);
			break;
		case "days":
			val = checkString(key,object);
			if(!val.matches(OfficeHoursWrapper.DAYS_PATTERN))
				throw new IllegalArgumentException("Days does not match expected pattern: "
						+ OfficeHoursWrapper.DAYS_PATTERN);
			break;
		case "instructor":
			if(!(object instanceof PersonWrapper)) 
				throw new IllegalArgumentException("Instructor must be a PersonWrapper object!");
			break;
		case "overwriteinstructor":
			if(!(object instanceof Boolean))
				throw new IllegalArgumentException("OverwriteInstructor must be of type Boolean!");
			break;
		}
	
	
		return "";
	}

	private boolean checkNewInfo(String propertyKey, Object propertyValue) throws ParseException {
		boolean isNewInfo = true;
		Section section = getSection();
		
		switch(propertyKey.toLowerCase()){
		case "sectionnum":
			isNewInfo = false;
			break;
		case "days":
			String days = section.getDays();
			if(days != null){
				isNewInfo = !days.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "starttime":
			double newStartTime = StringHelper.parseTimeToDouble((String) propertyValue);
			double oldStartTime = section.getStartTime(); 
			isNewInfo = newStartTime != oldStartTime;
			break;
		case "endtime":
			double newEndTime = StringHelper.parseTimeToDouble((String) propertyValue);
			double oldEndTime = section.getEndTime();
			isNewInfo = newEndTime != oldEndTime;
			break;
		case "startdate":
			Date newStartDate = StringHelper.stringToDate((String) propertyValue);
			Date oldStartDate = section.getStartDate();
			if(oldStartDate != null){
				isNewInfo = !oldStartDate.equals(newStartDate);
			}
			break;
		case "enddate":
			Date newEndDate = StringHelper.stringToDate((String) propertyValue);
			Date oldEndDate = section.getEndDate();
			if(oldEndDate != null){
				isNewInfo = !oldEndDate.equals(newEndDate);
			}
			break;
		case "credits":
			String oldCredits = section.getCredits();
			if(oldCredits != null){
				isNewInfo = !oldCredits.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "instructorfirstname":
			String oldFirstName = section.getInstructorFirstName();
			if(oldFirstName != null){
				isNewInfo = !oldFirstName.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "instructorlastname":
			String oldLastName = section.getInstructorLastName();
			if(oldLastName != null){
				isNewInfo = !oldLastName.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "room":
			String oldRoom = section.getRoom();
			if(oldRoom != null){
				isNewInfo = !oldRoom.equalsIgnoreCase((String) propertyValue);
			}
			break;
		case "instructor":
			Person oldInstructor = section.getInstructor();
						
			if(oldInstructor != null){
				PersonWrapper newInstructor = (PersonWrapper)propertyValue;
				isNewInfo = !oldInstructor.equals(newInstructor.getPerson());
			}
			break;
		case "overwriteinstructor":
			boolean oldOverwriteVal = section.isOverwriteInstructor();
			
			isNewInfo = oldOverwriteVal != (boolean)propertyValue;
			break;
		}
		
		return isNewInfo;
	}

	private static String checkString(String key, Object obj){
		if(!(obj instanceof String)) 
			throw new IllegalArgumentException(key + " must be of String type!");
		
		return (String) obj;
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

	private boolean setProperty(String propertyKey, Object object) throws ParseException {
		Object propertyValue = object;
		double time;
		Date date;
		Section section = getSection();
		
		if(!checkNewInfo(propertyKey, propertyValue)) return false;
		
		switch(propertyKey.toLowerCase()){
		case "days":
			section.setDays((String) propertyValue);
			setDayBooleans((String) propertyValue);
			break;
		case "starttime":
			time = StringHelper.parseTimeToDouble((String) propertyValue);
			section.setStartTime(time);
			break;
		case "endtime":
			time = StringHelper.parseTimeToDouble((String) propertyValue);
			section.setEndTime(time);
			break;
		case "startdate":
			date = StringHelper.stringToDate((String) propertyValue);
			section.setStartDate(date);
			break;
		case "enddate":
			date = StringHelper.stringToDate((String) propertyValue);
			section.setEndDate(date);
			break;
		case "credits":
			section.setCredits((String) propertyValue);
			break;
		case "instructorfirstname":
			section.setInstructorFirstName((String) propertyValue);
			break;
		case "instructorlastname":
			section.setInstructorLastName((String) propertyValue);
			break;
		case "room":
			section.setRoom((String) propertyValue);
			break;
		case "overwriteinstructor":
			section.setOverwriteInstructor((boolean) propertyValue);
			break;
		case "instructor":
			PersonWrapper instructor = (PersonWrapper) propertyValue;
			section.setInstructor(instructor.getPerson());
			break;
		}
	
		return true;
	}

	private static void resetOnDays(Section section){
		section.setOnMonday(false);
		section.setOnTuesday(false);
		section.setOnWednesday(false);
		section.setOnThursday(false);
		section.setOnFriday(false);
	}

	private void setDayBooleans(String propertyValue) {
		Section section = getSection();
		resetOnDays(section);
		for(int i=0; i<propertyValue.length(); ++i){
			switch(propertyValue.charAt(i)){
			case 'M':
				section.setOnMonday(true);
				break;
			case 'T':
				section.setOnTuesday(true);
				break;
			case 'W':
				section.setOnWednesday(true);
				break;
			case 'R':
				section.setOnThursday(true);
				break;
			case 'F':
				section.setOnFriday(true);
				break;
			}
		}
	
	}

	private List<WrapperObject<Section>> getSectionFromList(
			List<Section> entities) {
		List<WrapperObject<Section>> sections = new ArrayList<WrapperObject<Section>>();
		for (Section item : entities)
			sections.add(SectionWrapper.getSectionWrapper(item));
		return sections;
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
}
