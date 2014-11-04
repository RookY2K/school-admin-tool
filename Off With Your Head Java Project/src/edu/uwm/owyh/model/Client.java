package edu.uwm.owyh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class Client implements Person,Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -4561417175133189867L;
	private String _userName;
	private Entity _clientEntity;
	private static final String TABLE = "users";
	
	private Client(Entity client) {
		_userName = (String) client.getProperty("username");
		_clientEntity = client;		
	}
	
	private Client(){
		//Default constructor that returns a Client object with default (null) 
		//instance variables
	}
	
	private void setClient(String userName){
		_userName = userName; 
		_clientEntity = getClientEntity(userName);
	}
	
	private Entity getClientEntity(String userName) {
		DataStore store = DataStore.getDataStore();
		Filter filter = new FilterPredicate("toupperusername", FilterOperator.EQUAL, userName.toUpperCase());
		List<Entity> users = store.findEntities(getClientTable(), filter, USERKEY);
		if(users.isEmpty()) return new Entity(getClientTable(),USERKEY);
		return users.get(0);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getUserName()
	 */
	@Override
	public String getUserName(){
		return _userName;
	}
		
	protected static Person getClient(){
		return  new Client();
	}
	
	private static Person getClient(Entity client) {
		return new Client(client);
	}
	
	public Person findPerson(String username) {
		DataStore store = DataStore.getDataStore();
		Filter filterUsername = new FilterPredicate("toupperusername", FilterOperator.EQUAL, username.toUpperCase());
		List<Person> clients = getPersonsFromList(store.findEntities(getClientTable(), filterUsername, USERKEY));
		if (clients.size() == 0) return null;
		return clients.get(0);
	}
	
	public List<Person> getAllPersons() {
		DataStore store = DataStore.getDataStore();
		List<Person> clients = getPersonsFromList(store.findEntities(getClientTable(), null, USERKEY));
		return clients;
	}
	
	private List<Person> getPersonsFromList(List<Entity> entities) {
		List<Person> clients = new ArrayList<Person>();
		for (Entity item : entities)
			clients.add(Client.getClient(item));
		return clients;
	}
	
	public static String getClientTable(){
		return TABLE;
	}
	
	public static boolean checkEmail(String userName){
		return userName.matches(EMAILPATTERN);
	}
	
	public static boolean checkPhone(String phone){
		return (phone.trim() == ""|| phone.trim().matches(PHONEPATTERN));
	}

	@Override
	public Object getProperty(String propertyKey) {
		if(propertyKey == null) return null;
		Object propertyVal = _clientEntity.getProperty(propertyKey);
		
		if(propertyKey.equals("accesslevel")){
			if(propertyVal instanceof Integer){
				propertyVal = AccessLevel.getAccessLevel((Integer)propertyVal);
			}else if(propertyVal instanceof Long){
				propertyVal = AccessLevel.getAccessLevel(((Long)propertyVal).intValue());
			}
		}
		
		return propertyVal;
	}

	private String setProperty(String propertyKey, Object object) {
		String error = "";
		Object obj = object;
		if(propertyKey == "accesslevel"){
			obj = ((AccessLevel)object).getVal();
		}
		_clientEntity.setProperty(propertyKey, obj);
		
		error = checkProperty(propertyKey, obj);
		
		if(!error.equals("")) return error;
		
		return error;
	}

	private String checkProperty(String propertyKey, Object object) {
		String error = "";
		switch(propertyKey){
		case "phone":
			if(!checkPhone((String)object)){
				error = "Error: Phone number is formatted incorrectly!";
			}
			break;
		case "username":
			if(!checkEmail((String)object)){
				error = "Error: User name must be a UWM email address!";
			}
			break;
		case "email":
			if(!checkEmail((String)object)){
				error = "Error: Primary email must be a UWM email address!";
			}
			break;
		default:
			break;
		}
		return error;
	}

	@Override
	public List<String> addPerson(String userName, Map<String, Object> properties) {
		DataStore store = DataStore.getDataStore();
		String error;
		boolean hasAccessLevel = false;
		List<String> errors = new ArrayList<String>();
		
		if(findPerson(userName) != null){
			errors.add("Error: User already exists!");
			return errors;
		}
		
		setClient(userName);
		
		error = setProperty("username", userName);
				
		if(!error.equals("")){
			errors.add(error);
		}
		
		setProperty("toupperusername", userName.toUpperCase());
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
			if(!store.insertEntity(_clientEntity)){
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
		
		setClient(userName);
		
		for(String propertyKey : properties.keySet()){
			error = setProperty(propertyKey, properties.get(propertyKey));
			if(!error.equals("")){
				errors.add(error);
			}
		}
		
		if(errors.isEmpty()){
			if(!store.updateEntity(_clientEntity)){
				errors.add("Error: Datastore update failed for unexpected reason!");
				return errors;
			}
		}	
		return errors;
	}

	@Override
	public boolean removePerson(String userName) {
		DataStore store = DataStore.getDataStore();
		
		setClient(userName);
		
		return store.deleteEntity(_clientEntity);
	}

}
