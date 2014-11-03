package edu.uwm.owyh.model;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class Client implements Person{
		
	private String _userName;
	private String _password;
	private AccessLevel _accessLevel;
	private Entity _clientEntity;
	private Entity _childContact;
	private static final String TABLE = "users";
	
	
	
	private Client(String userName, String pwd, AccessLevel access){
		_userName = userName;
		_password = pwd;
		_accessLevel = access;
		_clientEntity = createClientEntity();
		_childContact = getChildContact();		
	}
	
	private Entity getChildContact() {
		DataStore store = DataStore.getDataStore();
		Filter filter = new FilterPredicate("toupperemail", FilterOperator.EQUAL, _userName.toUpperCase());
		List<Entity> contacts = store.findEntities(ContactCard.getContactCardTable(), filter, USERKEY);
		if(contacts.isEmpty()) return createChildContact();
		return contacts.get(0);
	}

	private Entity createChildContact() {
		Entity contact = new Entity(ContactCard.getContactCardTable(), _clientEntity.getKey());
		contact.setProperty("name", null);
		contact.setProperty("phone", null);
		contact.setProperty("address", null);
		contact.setProperty("email", _userName);
		contact.setProperty("toupperemail", _userName.toUpperCase());
		return contact;
	}

	private Client(Entity client) {
		_userName = (String) client.getProperty("username");
		_password = (String) client.getProperty("password");
		Long accessLong = (Long) client.getProperty("accesslevel");
		if(accessLong != null){
			int getAccess = accessLong.intValue();
			_accessLevel = AccessLevel.getAccessLevel(getAccess);
		}
		_clientEntity = client;
		_childContact = getChildContact();		
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getUserName()
	 */
	@Override
	public String getUserName(){
		return _userName;
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getToUpperUserName()
	 */
	@Override
	public String getToUpperUserName(){
		if(_userName == null) return null;
		else
			return _userName.toUpperCase();
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getPassword()
	 */
	@Override
	public String getPassword(){
		return _password;
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getAccessLevel()
	 */
	@Override
	public AccessLevel getAccessLevel(){
		return _accessLevel;
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getName()
	 */
	@Override
	public String getName(){
		return (String) _childContact.getProperty("name");
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getEmail()
	 */
	@Override
	public String getEmail(){
		return (String) _childContact.getProperty("email");
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getPhone()
	 */
	@Override
	public String getPhone(){
		return (String) _childContact.getProperty("phone");
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getAddress()
	 */
	@Override
	public String getAddress(){
		return (String) _childContact.getProperty("address");
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password){
		_password = password;
		_clientEntity.setProperty("password", password);
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setAccessLevel(edu.uwm.owyh.model.User.AccessLevel)
	 */
	@Override
	public void setAccessLevel(AccessLevel accessLevel){
		_accessLevel = accessLevel;
		_clientEntity.setProperty("accesslevel", accessLevel.getVal());
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setName(java.lang.String)
	 */
	@Override
	public void setName(String name){
		_childContact.setProperty("name", name);
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email){
		_childContact.setProperty("email", email);
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setPhone(java.lang.String)
	 */
	@Override
	public void setPhone(String phone){
		_childContact.setProperty("phone", phone);
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setAddress(java.lang.String)
	 */
	@Override
	public void setAddress(String address){
		_childContact.setProperty("address", address);
	}
	
	protected static Person getClient(String userName, String pwd, AccessLevel access){
		return new Client(userName, pwd, access);
	}
	
	protected static Person getClient(Entity client) {
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
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#saveUser()
	 */
	/*@Override
	public boolean savePerson() {
		DataStore store = DataStore.getDataStore();
		
		if (_userName.trim().equals("") || _password.equals("") || _accessLevel == null)
			return false;

		Filter filterUsername = new FilterPredicate("toupperusername", FilterOperator.EQUAL, getToUpperUserName());
		List<Entity> users = store.findEntities(getClientTable(), filterUsername);
		
		if (users.size() > 0)
		{
		    if(!users.get(0).getKey().equals(_clientEntity.getKey())){
		    	return false;
		    }else{
		    	store.updateEntity(_clientEntity);
		    }
		        
		}
		
		store.insertEntity(_clientEntity);
		return true;
	}*/
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#removeUser()
	 */
	@Override
	public void removePerson() {
		DataStore store = DataStore.getDataStore();
		if(store.deleteEntity(_clientEntity)){
			store.deleteEntity(_childContact);
		}
	}
	
	private Entity createClientEntity() {
		Entity user = new Entity(getClientTable(), USERKEY);
		user.setProperty("username", _userName);
		user.setProperty("toupperusername", _userName.toUpperCase());
		user.setProperty("password", _password);
		user.setProperty("accesslevel", _accessLevel.getVal());
		return user;
	}
	
	public static String getClientTable(){
		return TABLE;
	}

	@Override
	public boolean addPerson() {
		// TODO Auto-generated method stub
		DataStore store = DataStore.getDataStore();
		//1 Check if user name is uwm email
		if(!checkUserName(_userName)) return false;
		//2 Check if user name exists in datastore
		Person user = findPerson(_userName);
		// -- if so, return false
		if(user != null) return false;
		//3 Else, insertEntity (return based on insert's return)
		if(!store.insertEntity(_clientEntity)) return false;		
		return store.insertEntity(_childContact);
	}

	@Override
	public boolean editPerson() {
		// TODO Auto-generated method stub
		DataStore store = DataStore.getDataStore();
		//1 Ensure userName exists in datastore
		Person user = findPerson(_userName);
		// -- if not, return false
		if(user == null) return false;
		//2 Else, updateEntity (return based on update's return)
		if(!ContactCard.checkPhone((String)_childContact.getProperty("phone"))) return false;
		if(!store.updateEntity(_clientEntity)) return false;
		
		return store.updateEntity(_childContact);		
	}
	
	public static boolean checkUserName(String userName){
		return userName.matches(EMAILPATTERN);
	}

}
