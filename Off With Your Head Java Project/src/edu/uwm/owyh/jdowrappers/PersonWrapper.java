package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.ContactInfo;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.model.DataStore;

/**
 * Wrapper Class for the Person JDO
 * @author Vince Maiuri
 *
 */
public class PersonWrapper implements WrapperObject<Person>,Serializable{


	private static final long serialVersionUID = -4561417175133189867L;
	private Person _person;
	private static final String PARENT = KeyFactory.keyToString(Person.getParentkey());
	public static final String EMAILPATTERN = "^\\w+@uwm.edu$";
	public static final String PHONEPATTERN = "^((\\(\\d{3}\\))|(\\d{3}))[-\\.\\s]{0,1}\\d{3}[-\\.\\s]{0,1}\\d{4}$";
	
	public enum AccessLevel {
		TA(3), INSTRUCTOR(2), ADMIN(1);
		private int value;
		private AccessLevel(int val){
			value = val;
		}
		public int getVal(){
			return value;
		}
		public static AccessLevel getAccessLevel(int val){
			switch(val){
			case 1:
				return AccessLevel.ADMIN;
			case 2:
				return AccessLevel.INSTRUCTOR;
			case 3:
				return AccessLevel.TA;
			default:
				return null;
			}
		}
	}

	//Private constructors
	private PersonWrapper(Person person) {
		_person = person;	
	}

	private PersonWrapper(){
		//Default constructor that returns a WrapperObject object with default (null) 
		//instance variables
	}

	/**
	 * Public accessor method for the PersonWrapper object
	 * @return an instantiated PersonWrapper Object
	 */
	public static WrapperObject<Person> getPersonWrapper(){
		return  new PersonWrapper();
	}

	static WrapperObject<Person> getPersonWrapper(Person client) {
		return new PersonWrapper(client);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.WrapperObject#getUserName()
	 */
	@Override
	public Key getId(){
		return _person.getId();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObject(java.lang.String, edu.uwm.owyh.jdowrappers.WrapperObject)
	 */
	@Override
	public <T> List<WrapperObject<Person>> findObjects(String filter, WrapperObject<T> parent, String order) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Person>> persons = null;
	
		String filterWithParent = "parentKey == '" + PARENT + "'";
		if (filter != null)	filterWithParent +=	"&& " + filter;
		List<Person> entities = store.findEntities(getTable(), filterWithParent, null, order);
		persons = getPersonsFromList(entities);
		
		return persons;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getAllObjects()
	 */
	public List<WrapperObject<Person>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<Person>> persons = null;
		String filter = "parentKey == '" + PARENT + "'";

		persons = getPersonsFromList(store.findEntities(getTable(), filter, null, null));

		return persons;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getTable()
	 */
	@Override
	public Class<Person> getTable(){
		return Person.getClassname();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;
	
		switch(propertyKey){
		case "username":
			return _person.getUserName();
		case "toupperusername":
			return _person.getToUpperUserName();
		case "password":
			return _person.getPassword();
		case "firstname":
			return _person.getContactInfo().getFirstName();
		case "lastname":
			return _person.getContactInfo().getLastName();
		case "email":
			return _person.getContactInfo().getEmail();
		case "phone":
			return _person.getContactInfo().getPhone();
		case "accesslevel":
			Integer accessLevel = _person.getAccessLevel();
			return AccessLevel.getAccessLevel(accessLevel);
		case "streetaddress":
			return _person.getContactInfo().getStreetAddress();
		case "city":
			return _person.getContactInfo().getCity();
		case "state":
			return _person.getContactInfo().getState();
		case "zip":
			return _person.getContactInfo().getZip();
		case "officehours":
			List<OfficeHours> officeHours = this.getPerson().getOfficeHours();
			List<WrapperObject<OfficeHours>> officeHoursWrappers = new ArrayList<WrapperObject<OfficeHours>>();
			for(OfficeHours officeHour : officeHours){
				officeHoursWrappers.add(OfficeHoursWrapper.getOfficeHoursWrapper(officeHour));
			}
			return WrapperObjectFactory.getOfficeHours().findObjects(null, this, "startTime");
		case "officeroom":
			return _person.getOfficeRoom();
		case "temporarypassword":
			return _person.getTempPassword();
		case "skills":
			return _person.getSkills();
		case "sections":
			List<Section> sections = this.getPerson().getSections();
			List<WrapperObject<Section>> sectionWrappers = new ArrayList<WrapperObject<Section>>();
			for(Section section : sections){
				sectionWrappers.add(SectionWrapper.getSectionWrapper(section));
			}
			return sectionWrappers;
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> addObject(String userName, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		Key id = WrapperObjectFactory.generateIdFromUserName(userName);
		
		boolean hasAccessLevel = false;
		List<String> errors = new ArrayList<String>();
	
		if(findObjectById(id) != null){
			errors.add("Error: User already exists!");
		}
	
		error = checkProperty("username", userName);
	
		if(!error.equals("")){
			errors.add(error);
		}
	
		for(String propertyKey : properties.keySet()){
			if(propertyKey.equals("accesslevel")) hasAccessLevel = true;
			error = checkProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}
	
		if(!hasAccessLevel) errors.add("Error: Role is a required field!");
	
		if(!errors.isEmpty()) return errors;
	
		setPerson(userName);		
	
		setProperty("email", userName);
	
		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	
		if(!store.insertEntity(_person, _person.getId())){
			errors.add("Error: Datastore insert failed for unexpected reason!");
		}
	
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#editObject(java.lang.String, java.util.Map)
	 */
	@Override
	public List<String> editObject(Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		List<String> errors = new ArrayList<String>();
		Key id = this.getId();
	
		if(findObjectById(id) == null){
			throw new IllegalArgumentException("That user does not exist!");
		}
	
		for(String propertyKey : properties.keySet()){
			error = checkProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}
	
		if(!errors.isEmpty()) return errors;
//		String userName = id.getName();
//		setPerson(userName);
	
		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}
	
		if(!store.updateEntity(this.getPerson(), id)){
			errors.add("Error: Datastore update failed for unexpected reason!");
		}
	
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeObject(java.lang.String)
	 */
	@Override
	public boolean removeObject(String userName) {
		DataStore store = DataStore.getDataStore();
		
		setPerson(userName);
	
		return store.deleteEntity(_person, _person.getId());		
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#findObjectById(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public WrapperObject<Person> findObjectById(Key id) {
		DataStore store = DataStore.getDataStore();
		Person person =  (Person) store.findEntityById(getTable(), id);
		
		if(person == null) return null;
		
		return getPersonWrapper(person);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#addChildObject(java.lang.Object)
	 */
	@Override
	public List<String> addChildObject(Object ChildJDO) throws UnsupportedOperationException {
		String kind = ChildJDO.getClass().getSimpleName();
		List<String> errors = new ArrayList<String>();
		switch(kind.toLowerCase()){
		case "officehours":
			if(getPerson().getOfficeHours().contains(ChildJDO)){
				errors.add("Duplicate office hours!");
			}else{
				getPerson().addOfficeHours((OfficeHours) ChildJDO);
			}
			break;			
		case "contactinfo":
			if(getPerson().getContactInfo() != null){
				throw new IllegalStateException("ContactInfo child jdo already exists!");
			}else{
				getPerson().setContactInfo((ContactInfo) ChildJDO);
			}
			break;
		default: 
			throw new IllegalArgumentException("Person jdo does not have " + kind + " as a child jdo!");
		}
		
		if(!DataStore.getDataStore().updateEntity(getPerson(), getPerson().getId())){
			errors.add("DataStore update failed for unknown reason! Please try again.");
		}
		
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.jdowrappers.WrapperObject#removeChildObject(java.lang.Object)
	 */
	@Override
	public boolean removeChildObject(Object childJDO) throws UnsupportedOperationException {
		String kind = childJDO.getClass().getSimpleName();
		
		switch(kind.toLowerCase()){
		case "officehours":
			return _person.removeOfficeHours((OfficeHours) childJDO);
		case "contactinfo":
			_person.setContactInfo(null);
			return true;
		default:
			return false;
		}
	}

	/**
	 * 
	 * @param email
	 * @return true if email is in proper format
	 */
	public static boolean checkEmail(String email){
		return email.matches(EMAILPATTERN);
	}

	/**
	 * Checks officehours for the proper format
	 * @param formatString
	 * @return true if the officehours are in the proper format string
	 */
	public static boolean checkOfficeHours(String formatString){
		return formatString.matches(OfficeHoursWrapper.OFFICEHOURPATTERN);
	}

	/**
	 * Checks the phone number for proper format
	 * @param phone
	 * @return true if the phone number is in the proper format
	 */
	public static boolean checkPhone(String phone){
		return (phone.trim() == ""|| phone.trim().matches(PHONEPATTERN));
	}

	//private mutators
	private void setPerson(String userName){
		_person = getPerson(userName);		
	}
	
	@SuppressWarnings("unchecked")
	private void setProperty(String propertyKey, Object object) {
//		String error = "";

//		error = checkProperty(propertyKey, obj);

		switch(propertyKey){
		case "password":
			_person.setPassword((String) object);
			break;
		case "accesslevel":
			Integer accessLevel = ((AccessLevel)object).getVal();
			_person.setAccessLevel(accessLevel);
			break;
		case "firstname":
			_person.getContactInfo().setFirstName((String) object);
			break;
		case "lastname":
			_person.getContactInfo().setLastName((String) object);
			break;
		case "email":
			_person.getContactInfo().setEmail((String) object);
			break;
		case "phone":
			_person.getContactInfo().setPhone((String) object);
			break;
		case "streetaddress":
			_person.getContactInfo().setStreetAddress((String)object);
			break;
		case "city":
			_person.getContactInfo().setCity((String) object);
			break;
		case "state":
			_person.getContactInfo().setState((String) object);
			break;
		case "zip":
			_person.getContactInfo().setZip((String) object);
			break;
		case "officeroom":
			_person.setOfficeRoom((String) object);
			break;
		case "temporarypassword":
			_person.setTempPassword((String) object);
			break;
		case "skills":
			_person.setSkills((List<String>) object);
			break;
		case "sections":
			List<Section> sections = new ArrayList<Section>();
			List<?> objects = (List<?>)object;
			
			for(Object obj : objects){
				SectionWrapper section = (SectionWrapper) obj;
				
				sections.add(section.getSection());
			}
			_person.setSections(sections);
			break;
		default:
			throw new IllegalArgumentException(propertyKey + 
					" is not a valid property of " + getClass().getSimpleName());
		}

//		return error;
	}


	//private Accessors
	private Person getPerson(String userName) {
		DataStore store = DataStore.getDataStore();

		Key id = WrapperObjectFactory.generateIdFromUserName(userName);

		Person user = (Person) store.findEntityById(getTable(), id);

		if(user == null) {
			user = Person.getPerson(userName);
		}
		return user;
	}
	
	Person getPerson(){
		return _person;
	}
	
	private List<WrapperObject<Person>> getPersonsFromList(List<Person> entities) {
		List<WrapperObject<Person>> persons = new ArrayList<WrapperObject<Person>>();
		for (Person item : entities)
			persons.add(PersonWrapper.getPersonWrapper(item));
		return persons;
	}
	

	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 */
	private void checkObjectIsString(Object obj){
		if(!(obj instanceof String)){
			throw new IllegalArgumentException("Property must be of type String!");
		}
	}

	private String checkProperty(String propertyKey, Object object) {
		String error = "";

		if(propertyKey == null){
			throw new NullPointerException("Property Key is null!");
		}

		switch(propertyKey){

		case "phone":
			checkObjectIsString(object);
			if(!checkPhone((String)object)){
				error = "Error: Phone number is formatted incorrectly!";
			}			
			break;
		case "username":
			checkObjectIsString(object);
			if(!checkEmail((String)object)){
				error = "Error: Email must be a UWM email address!";
			}
			break;
		case "email":
			checkObjectIsString(object);
			if(!checkEmail((String)object)){
				error = "Error: Email must be a UWM email address!";
			}
			break;
		case "accesslevel":
			if(!(object instanceof AccessLevel)){
				throw new IllegalArgumentException("Property must be of type AccessLevel");
			}
			break;

		case "officehours":
			checkObjectIsString(object);
			if(!checkOfficeHours((String)object)){
				error = "Error: Incorrectly Formatted Office Hours String!";
			}
			break;
		case "skills":
			if (! (object instanceof List<?>)) {
				error = "Error: Incorrectly Formatted Skills, must be List<String>!";
			}
			break;
		case "sections":
			if(! (object instanceof List<?>))
				throw new IllegalArgumentException("sections must be a List!");
			List<?> objects = (List<?>)object;
			for(Object obj : objects){
				if(obj == null)
					throw new NullPointerException("A section in Sections cannot be null!");
				if(!(obj instanceof SectionWrapper))
					throw new IllegalArgumentException("Sections must be a list of SectionWrappers!");
			}
			break;
		default:
			checkObjectIsString(object);
			break;
		}
		return error;
	}

	@Override
	public boolean removeObjects(List<WrapperObject<Person>> persons) {
		List<Person> personList = new ArrayList<Person>();
		
		for(WrapperObject<Person> person : persons){
			PersonWrapper personWrapper = (PersonWrapper) person;
			
			personList.add(personWrapper.getPerson());
		}
		
		return DataStore.getDataStore().deleteAllEntities(personList);
	}
}
