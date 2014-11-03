/**
 * 
 */
package edu.uwm.owyh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author Vince Maiuri
 *
 */
public class ContactCard implements Person, Serializable {
	private String _name;
	private String _phone;
	private String _address;
	private String _email;
	private Entity _contactEntity;
	private Entity _parentClient;
	private static final String PHONEPATTERN = "^((\\(\\d{3}\\))|(\\d{3}))[-\\.\\s]{0,1}\\d{3}[-\\.\\s]{0,1}\\d{4}$";
	private static final String TABLE = "contactCards";
	
	private ContactCard(String name, String phone, String address, String email){
		_name = name;
		_phone = phone;
		_address = address;
		_email = email;
		_parentClient = getParentClient();
		_contactEntity = createContactCardEntity();		
	}
	
	private Entity getParentClient() {
		DataStore store = DataStore.getDataStore();
		Filter filter = new FilterPredicate("toupperusername", FilterOperator.EQUAL, _email.toUpperCase());
		List<Entity> clients = store.findEntities(Client.getClientTable(), filter, USERKEY);
		
		if(clients.isEmpty()) return createParentClient();
		
		return clients.get(0);
	}
	
	private Entity createParentClient() {
		Entity client = new Entity(Client.getClientTable(),USERKEY);
		client.setProperty("username", _email);
		client.setProperty("toupperusername", _email.toUpperCase());
		client.setProperty("password", null);
		client.setProperty("accesslevel", null);
	
		return client;
	}

	private ContactCard(Entity contact){
		_email = (String) contact.getProperty("email");
		_name = (String) contact.getProperty("name");
		_phone = (String) contact.getProperty("phone");
		_address = (String) contact.getProperty("address");
		_contactEntity = contact;
		_parentClient = getParentClient();
	}
	
	private ContactCard(){
		//returns ContactCard object with default (null) instance variables
	}
	
	protected static Person getContactCard(){
		return new ContactCard();
	}
	
	protected static Person getContactCard(Entity contact){
		return new ContactCard(contact);
	}
	
	protected static Person getContactCard(String name, String phone, String address, String email){
		return new ContactCard(name,phone,address,email);
	}
	
	public static String getContactCardTable(){
		return TABLE;
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getUserName()
	 */
	@Override
	public String getUserName() {
		return (String) _parentClient.getProperty("username");
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getToUpperUserName()
	 */
	@Override
	public String getToUpperUserName() {
		return getUserName().toUpperCase();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getPassword()
	 */
	@Override
	public String getPassword() {
		return (String) _parentClient.getProperty("password");
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getAccessLevel()
	 */
	@Override
	public AccessLevel getAccessLevel() {
		return (AccessLevel) _parentClient.getProperty("accesslevel");
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getName()
	 */
	@Override
	public String getName() {
		return _name;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getEmail()
	 */
	@Override
	public String getEmail() {
		return _email;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getPhone()
	 */
	@Override
	public String getPhone() {
		return _phone;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#getAddress()
	 */
	@Override
	public String getAddress() {
		return _address;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		_parentClient.setProperty("password", password);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setAccessLevel(edu.uwm.owyh.model.Person.AccessLevel)
	 */
	@Override
	public void setAccessLevel(AccessLevel accessLevel) {
		_parentClient.setProperty("accesslevel", accessLevel.getVal());
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		_name = name;
		_contactEntity.setProperty("name", name);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		_email = email;
		_contactEntity.setProperty("email", email);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setPhone(java.lang.String)
	 */
	@Override
	public void setPhone(String phone) {
		_phone = phone;
		_contactEntity.setProperty("phone", phone);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#setAddress(java.lang.String)
	 */
	@Override
	public void setAddress(String address) {
		_address = address;
		_contactEntity.setProperty("address", address);
	}

	
	/* (non-Javadoc)
	 * @see edu.uwm.owyh.model.Person#removeClient()
	 */
	@Override
	public void removePerson() {
		// TODO Auto-generated method stub
		DataStore store = DataStore.getDataStore();
		if(store.deleteEntity(_contactEntity)){
			store.deleteEntity(_parentClient);
		}
	}
	public static boolean checkEmail(String email){
		return email.matches(EMAILPATTERN);
	}
	
	
	public static boolean checkPhone(String phone){
		return phone.matches(PHONEPATTERN);
	}
	
	private Entity createContactCardEntity() {
		Entity contact = new Entity(getContactCardTable(), _parentClient.getKey());
		contact.setProperty("name", _name);
		contact.setProperty("email", _email);
		contact.setProperty("toupperemail", _email.toUpperCase());
		contact.setProperty("phone", _phone);
		contact.setProperty("address", _address);
		return contact;
	}

	@Override
	public boolean addPerson() {
		// TODO Auto-generated method stub
		DataStore store = DataStore.getDataStore();
		//1 Check if email is uwm email
		if(!checkEmail(_email)) return false;
	
		//2 Check if phone is appropriate pattern or null
		if(_phone != null && !checkPhone(_phone)) return false;
	
		//3 Check if parent entity exists:
		if(!store.insertEntity(_parentClient)) return false;
		// return insertEntity
		return store.insertEntity(_contactEntity);
	}

	@Override
	public boolean editPerson() {
		// TODO Auto-generated method stub
		DataStore store = DataStore.getDataStore();
		//1 Ensure email exists in datastore
		Person user = findPerson(_email);
		// -- if not, return false
		if(user == null) return false;
		//2 Else, updateEntity (return based on update's return)
		if(checkPhone(_phone)) return false;
		if(!store.updateEntity(_contactEntity)) return false;

		return store.updateEntity(_parentClient);
	}

	@Override
	public Person findPerson(String email) {
		DataStore store = DataStore.getDataStore();
		Filter filterUsername = new FilterPredicate("toupperemail", FilterOperator.EQUAL, email.toUpperCase());
		List<Person> clients = getPersonsFromList(store.findEntities(getContactCardTable(), filterUsername, _parentClient.getKey()));
		if (clients.size() == 0) return null;
		return clients.get(0);
	}

	@Override
	public List<Person> getAllPersons() {
		DataStore store = DataStore.getDataStore();
		List<Person> clients = getPersonsFromList(store.findEntities(getContactCardTable(), null, USERKEY));
		return clients;
	}

	private List<Person> getPersonsFromList(List<Entity> entities) {
		List<Person> contacts = new ArrayList<Person>();
		for (Entity item : entities)
			contacts.add(ContactCard.getContactCard(item));
		return contacts;
	}

}
