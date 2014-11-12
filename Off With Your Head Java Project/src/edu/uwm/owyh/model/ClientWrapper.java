package edu.uwm.owyh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class ClientWrapper implements Person,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4561417175133189867L;
	private String _userName;
	private Client _client;
	private static final Class<Client> TABLE = Client.class;
	private static final String PARENT = KeyFactory.keyToString(Client.PARENTKEY);

	private ClientWrapper(Client client) {
		_userName = client.getUserName();
		_client = client;	
	}

	private ClientWrapper(){
		//Default constructor that returns a Client object with default (null) 
		//instance variables
	}

	private void setClient(String userName, boolean isClone){
		_userName = userName;
		Client other = getClient(userName);
		if(isClone)other = getClientClone(other);
		
		_client = other;		
	}
	
	private Client getClientClone(Client other) {
		return other.clone();
	}

	private Client getClient(String userName) {
		DataStore store = DataStore.getDataStore();

		String filter = "toUpperUserName == '" + userName.toUpperCase() + 
				"' && parentKey == '" + PARENT + "'";

		List<Client> users = store.findEntities(getClientTable(), filter);
		if(users.isEmpty()) {
			Client client = new Client(userName);
			return client;
		}
		return users.get(0);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getUserName()
	 */
	@Override
	public String getUserName(){
		return _userName;
	}

	protected static Person getClientWrapper(){
		return  new ClientWrapper();
	}

	private static Person getClientWrapper(Client client) {
		return new ClientWrapper(client);
	}

	public Person findPerson(String username) {
		DataStore store = DataStore.getDataStore();
		List<Person> clients = null;

		String filterUsername = "toUpperUserName == '" + username.toUpperCase() + 
				"' && parentKey == '" + PARENT + "'"; 
		List<Client> entities = store.findEntities(getClientTable(), filterUsername);
		clients = getPersonsFromList(entities);
		if (clients.size() == 0) return null;

		return clients.get(0);
	}

	public List<Person> getAllPersons() {
		DataStore store = DataStore.getDataStore();
		List<Person> clients = null;
		String filter = "parentKey == '" + PARENT + "'";
		try{
			clients = getPersonsFromList(store.findEntities(getClientTable(), filter));
		}finally{
			store.closeDataStore();
		}
		return clients;
	}

	private List<Person> getPersonsFromList(List<Client> entities) {
		List<Person> clients = new ArrayList<Person>();
		for (Client item : entities)
			clients.add(ClientWrapper.getClientWrapper(item));
		return clients;
	}

	/**
	 * 
	 * @return
	 */
	public static Class<Client> getClientTable(){
		return TABLE;
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean checkEmail(String userName){
		return userName.matches(EMAILPATTERN);
	}

	public static boolean checkPhone(String phone){
		return (phone.trim() == ""|| phone.trim().matches(PHONEPATTERN));
	}

	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;

		switch(propertyKey){
		case "username":
			return _client.getUserName();
		case "toupperusername":
			return _client.getToUpperUserName();
		case "password":
			return _client.getPassword();
		case "firstname":
			return _client.getFirstName();
		case "lastname":
			return _client.getLastName();
		case "email":
			return _client.getEmail();
		case "phone":
			return _client.getPhone();
		case "accesslevel":
			Integer accessLevel = _client.getAccessLevel();
			return AccessLevel.getAccessLevel(accessLevel);
		case "streetaddress":
			return _client.getStreetAddress();
		case "city":
			return _client.getCity();
		case "state":
			return _client.getState();
		case "zip":
			return _client.getZipCode();
		default:
			return null;
		}
	}

	private String setProperty(String propertyKey, Object object) {
		String error = "";
		Object obj = object;

		error = checkProperty(propertyKey, obj);

		switch(propertyKey){
		case "password":
			_client.setPassword((String) obj);
			break;
		case "accesslevel":
			Integer accessLevel = ((AccessLevel)obj).getVal();
			_client.setAccessLevel(accessLevel);
			break;
		case "firstname":
			_client.setFirstName((String) obj);
			break;
		case "lastname":
			_client.setLastName((String) obj);
			break;
		case "email":
			_client.setEmail((String) obj);
			break;
		case "phone":
			_client.setPhone((String) obj);
			break;
		case "streetaddress":
			_client.setStreetAddress((String)obj);
			break;
		case "city":
			_client.setCity((String) obj);
			break;
		case "state":
			_client.setState((String) obj);
			break;
		case "zip":
			_client.setZip((String) obj);
			break;			
		default:
			throw new IllegalArgumentException(propertyKey + " is not a valid property of " + getClass().getSimpleName());
		}
		if(propertyKey == "accesslevel"){
			obj = ((AccessLevel)object).getVal();
		}

		return error;
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
		default:
			checkObjectIsString(object);
			break;
		}
		return error;
	}

	@Override
	public List<String> addPerson(String userName, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		boolean hasAccessLevel = false;
		boolean isClone = false;
		List<String> errors = new ArrayList<String>();

		if(findPerson(userName) != null){
			errors.add("Error: User already exists!");
			//return errors;
			isClone = true;
		}
			
		setClient(userName, isClone);		

		error = checkProperty("username", userName);

		if(!error.equals("")){
			errors.add(error);
		}

		setProperty("email", userName);

		for(String propertyKey : properties.keySet()){
			if(propertyKey.equals("accesslevel")) hasAccessLevel = true;
			error = setProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}

		if(!hasAccessLevel) errors.add("Error: Role is a required field!");
		if(errors.isEmpty()){ 
			if(!store.insertEntity(_client, _client.getId())){
				errors.add("Error: Datastore insert failed for unexpected reason!");
			}
		}

		return errors;
	}

	@Override
	public List<String> editPerson(String userName, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		List<String> errors = new ArrayList<String>();

		if(findPerson(userName) == null){
			errors.add("Error: That requested user to edit does not exist!");
			return errors;
		}

		setClient(userName,false);

		for(String propertyKey : properties.keySet()){
			error = setProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}

		if(errors.isEmpty()){
			if(!store.updateEntity(_client, _client.getId())){
				errors.add("Error: Datastore update failed for unexpected reason!");
			}
		}

		return errors;
	}

	@Override
	public boolean removePerson(String userName) {
		DataStore store = DataStore.getDataStore();
		setClient(userName, false);

		return store.deleteEntity(_client, _client.getId());		
	}

	@PersistenceCapable
	private static class Client implements Serializable,Cloneable{

		private static final long serialVersionUID = 9116432906543554361L;
		private static final Key PARENTKEY = KeyFactory.createKey("Users", "RootUsers");
		private static final String KIND = "ClientWrapper$" + TABLE.getSimpleName();
		
		
		@PrimaryKey
		@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
		private Key id;
		
		@Persistent
		@Extension(vendorName="datanucleus", key="gae.parent-pk", value="true")
		private String parentKey;
		
		@Persistent
		private String userName;
		@Persistent
		private String toUpperUserName;
		@Persistent
		private String password;
		@Persistent
		private String firstName;
		@Persistent
		private String lastName;
		@Persistent
		private String email;
		@Persistent
		private String phone;
		@Persistent
		private Integer accessLevel;
		@Persistent
		private String streetAddress;
		@Persistent
		private String city;
		@Persistent
		private String state;
		@Persistent
		private String zip;

		private Client(String userName){
			KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
			
			id = keyBuilder.addChild(KIND, userName).getKey();
			
			setUserName(userName);
			setToUpperUserName(userName);
		}
		
		@Override
		protected Client clone(){
			Client other = null;
			try {
				 other = (Client) super.clone();
			} catch (CloneNotSupportedException e) {
				//Will not happen as Client implements Cloneable
				e.printStackTrace();
			}
			
			KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
			other.id = keyBuilder.addChild(KIND, "clone").getKey();	
			
			return other;			
		}
		
		
		private Key getId(){
			return id;
		}
		

		private void setCity(String city) {
			this.city = city;			
		}

		private void setZip(String zip) {
			this.zip = zip;			
		}

		private void setState(String state) {
			this.state = state;			
		}

		private void setStreetAddress(String street) {
			streetAddress = street;			
		}

		private void setLastName(String lastName) {
			this.lastName = lastName;			
		}

		private void setFirstName(String firstName) {
			this.firstName = firstName;			
		}

		private String getZipCode() {
			return zip;
		}

		private String getState() {
			return state;
		}

		private String getCity() {
			return city;
		}

		private String getStreetAddress() {
			return streetAddress;
		}

		private String getLastName() {
			return lastName;
		}

		private String getFirstName() {
			return firstName;
		}

		private Client(){
			//default constructor
		}


		/**
		 * @return the userName
		 */
		private String getUserName() {
			return userName;
		}

		/**
		 * @param userName the userName to set
		 */
		private void setUserName(String userName) {
			this.userName = userName;
		}

		/**
		 * @return the username in UPPERCASE
		 */
		private String getToUpperUserName(){
			return toUpperUserName;
		}

		/**
		 * @param userName set the uppercase of userName 
		 */
		private void setToUpperUserName(String userName){
			toUpperUserName = userName.toUpperCase();
		}		

		/**
		 * @return the password
		 */
		private String getPassword() {
			return password;
		}
		/**
		 * @param password the password to set
		 */
		private void setPassword(String password) {
			this.password = password;
		}
		/**
		 * @return the email
		 */
		private String getEmail() {
			return email;
		}
		/**
		 * @param email the email to set
		 */
		private void setEmail(String email) {
			this.email = email;
		}
		/**
		 * @return the phone
		 */
		private String getPhone() {
			return phone;
		}
		/**
		 * @param phone the phone to set
		 */
		private void setPhone(String phone) {
			this.phone = phone;
		}
		/**
		 * @return the accessLevel
		 */
		private Integer getAccessLevel() {
			return accessLevel;
		}
		/**
		 * @param accessLevel the accessLevel to set
		 */
		private void setAccessLevel(Integer accessLevel) {
			this.accessLevel = accessLevel;
		}		
	}

}
