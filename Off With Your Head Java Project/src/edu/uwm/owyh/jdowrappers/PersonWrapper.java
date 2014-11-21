package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.WrapperObject.AccessLevel;
import edu.uwm.owyh.model.DataStore;

public class PersonWrapper implements WrapperObject,Serializable{


	private static final long serialVersionUID = -4561417175133189867L;
//	private String _userName;
	private Person _person;
	private static final String PARENT = KeyFactory.keyToString(Person.getParentkey());
	

	//Private constructors
	private PersonWrapper(Person person) {
//		_userName = person.getUserName();
		_person = person;	
	}

	private PersonWrapper(){
		//Default constructor that returns a WrapperObject object with default (null) 
		//instance variables
	}
	
	
	//private mutators
	private void setPerson(String userName){
//		_userName = userName;

		_person = getPerson(userName);		
	}
	
	private void setProperty(String propertyKey, Object object) {
//		String error = "";
		Object obj = object;

//		error = checkProperty(propertyKey, obj);

		switch(propertyKey){
		case "password":
			_person.setPassword((String) obj);
			break;
		case "accesslevel":
			Integer accessLevel = ((AccessLevel)obj).getVal();
			_person.setAccessLevel(accessLevel);
			break;
		case "firstname":
			_person.getContactInfo().setFirstName((String) obj);
			break;
		case "lastname":
			_person.getContactInfo().setLastName((String) obj);
			break;
		case "email":
			_person.getContactInfo().setEmail((String) obj);
			break;
		case "phone":
			_person.getContactInfo().setPhone((String) obj);
			break;
		case "streetaddress":
			_person.getContactInfo().setStreetAddress((String)obj);
			break;
		case "city":
			_person.getContactInfo().setCity((String) obj);
			break;
		case "state":
			_person.getContactInfo().setState((String) obj);
			break;
		case "zip":
			_person.getContactInfo().setZip((String) obj);
			break;			
		case "officehours":
			_person.addOfficeHours((String) obj);
			break;
		default:
			throw new IllegalArgumentException(propertyKey + " is not a valid property of " + getClass().getSimpleName());
		}

//		return error;
	}


	//private Accessors
	private Person getPerson(String userName) {
		DataStore store = DataStore.getDataStore();

		Key id = Person.generateIdFromUserName(userName);

		Person user = (Person) store.findEntityById(getPersonTable(), id);

		if(user == null) {
			user = Person.getPerson(userName);
		}
		return user;
	}
	
	private static WrapperObject getPersonWrapper(Person client) {
		return new PersonWrapper(client);
	}
	
	private List<WrapperObject> getPersonsFromList(List<Person> entities) {
		List<WrapperObject> persons = new ArrayList<WrapperObject>();
		for (Person item : entities)
			persons.add(PersonWrapper.getPersonWrapper(item));
		return persons;
	}
	

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.WrapperObject#getUserName()
	 */
	@Override
	public Key getId(){
		return _person.getId();
	}

	public static WrapperObject getPersonWrapper(){
		return  new PersonWrapper();
	}

	public WrapperObject findObject(String username) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject> persons = null;

		String filterUsername = "toUpperUserName == '" + username.toUpperCase() + 
				"' && parentKey == '" + PARENT + "'"; 
		List<Person> entities = store.findEntities(getPersonTable(), filterUsername);
		persons = getPersonsFromList(entities);
		if (persons.size() == 0) return null;

		return persons.get(0);
	}

	public List<WrapperObject> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject> persons = null;
		String filter = "parentKey == '" + PARENT + "'";

		persons = getPersonsFromList(store.findEntities(getPersonTable(), filter));

		return persons;
	}

	/**
	 * 
	 * @return
	 */
	public static Class<Person> getPersonTable(){
		return Person.getClassname();
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean checkEmail(String userName){
		return userName.matches(EMAILPATTERN);
	}

	public static boolean checkOfficeHours(String formatString){
		return formatString.matches(OfficeHoursWrapper.OFFICEHOURPATTERN);
	}

	public static boolean checkPhone(String phone){
		return (phone.trim() == ""|| phone.trim().matches(PHONEPATTERN));
	}

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
			return _person.getOfficeHours();
		default:
			return null;
		}
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
		default:
			checkObjectIsString(object);
			break;
		}
		return error;
	}

	@Override
	public List<String> addObject(String userName, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		boolean hasAccessLevel = false;
		//		boolean isClone = false;
		List<String> errors = new ArrayList<String>();

		if(findObject(userName) != null){
			errors.add("Error: User already exists!");
			//			isClone = true;
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

	@Override
	public List<String> editObject(String userName, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		List<String> errors = new ArrayList<String>();
		//		Boolean isClone = true;

		if(findObject(userName) == null){
			throw new IllegalArgumentException("That user does not exist!");
		}

		for(String propertyKey : properties.keySet()){
			error = checkProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}

		if(!errors.isEmpty()) return errors;

		setPerson(userName);

		for(String propertyKey : properties.keySet()){
			setProperty(propertyKey, properties.get(propertyKey));
		}

		if(!store.updateEntity(_person, _person.getId())){
			errors.add("Error: Datastore update failed for unexpected reason!");
		}

		return errors;
	}

	@Override
	public boolean removeObject(String userName) {
		DataStore store = DataStore.getDataStore();
		
		setPerson(userName);

		return store.deleteEntity(_person, _person.getId());		
	}
}