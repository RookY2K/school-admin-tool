package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Person implements Serializable,Cloneable{

	private static final long serialVersionUID = 9116432906543554361L;
	private static final Key PARENTKEY = KeyFactory.createKey("Users", "RootUsers");
	private static final String KIND = Person.class.getSimpleName();
	private static final Class<Person> CLASSNAME = Person.class;
	
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.parent-pk", value="true")
	private String parentKey;
	
	@Persistent
	private List<Section> sections;
	
	@Persistent
	private ContactInfo contactInfo;
	
	@Persistent
	private String userName;
	
	@Persistent
	private String toUpperUserName;
	
	@Persistent
	private String password;
	
	@Persistent
	private Integer accessLevel;
	


	//Private constructors
	/*
	 * Private internal constructor that assigns a primary key based on the parentkey 
	 * parameter. Parentkey is generated as a constant to use for every Person jdo. This
	 * ensures that each Person entity is in the same entity group. This is useful for 
	 * keeping strong consistency in queries.
	 */
	private Person(String userName){
		KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
		
		id = keyBuilder.addChild(KIND, userName).getKey();
		
		setUserName(userName);
		setToUpperUserName(userName);
		
		contactInfo = ContactInfo.getContactInfo();
	}
	
	private Person(){
		//default constructor
	}
	
	//Public accessor for constructor
	/**
	 * Returns an instantiated Person jdo using the provided userName.
	 * @param userName - UWM email address of inputted Person.
	 * @return Person JDO
	 */
	public static Person getPerson(String userName){
		return new Person(userName);
	}
	
	//Accessors	
	
	/**
	 * Access for the Primary key
	 * @return Primary key
	 */
	public Key getId(){
		return id;
	}
	
	/**
	 * @return the classname
	 */
	public static Class<Person> getClassname() {
		return CLASSNAME;
	}

	/**
	 * @return the parentkey
	 */
	public static Key getParentkey() {
		return PARENTKEY;
	}

	/**
	 * @return the kind
	 */
	public static String getKind() {
		return KIND;
	}

	/**
	 * @return the parentKey
	 */
	public String getParentKey() {
		return parentKey;
	}
	
	public ContactInfo getContactInfo(){
		return contactInfo;
	}

	/**
	 * @return the zip
	 */
//	public String getZip() {
//		return zip;
//	}
	
	/**
	 * @return the state
	 */
//	public String getState() {
//		return state;
//	}

	/**
	 * @return the city
	 */
//	public String getCity() {
//		return city;
//	}

	/**
	 * @return the street address
	 */
//	public String getStreetAddress() {
//		return streetAddress;
//	}

	/**
	 * @return the last name
	 */
//	public String getLastName() {
//		return lastName;
//	}

	/**
	 * @return the first name
	 */
//	public String getFirstName() {
//		return firstName;
//	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return the username in UPPERCASE
	 */
	public String getToUpperUserName(){
		return toUpperUserName;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return the email
	 */
//	public String getEmail() {
//		return email;
//	}
	
	/**
	 * @return the phone
	 */
//	public String getPhone() {
//		return phone;
//	}
	
	/**
	 * @return the accessLevel
	 */
	public Integer getAccessLevel() {
		return accessLevel;
	}
	
	//Mutators
	
	public void setContactInfo(ContactInfo info){
		contactInfo = info;
	}
	
	/**
	 * Sets the city field
	 * @param city
	 */
//	public void setCity(String city) {
//		this.city = city;			
//	}

	/**
	 * Sets the zip field
	 * @param zip
	 */
//	public void setZip(String zip) {
//		this.zip = zip;			
//	}

	/**
	 * Sets the State field
	 * @param state
	 */
//	public void setState(String state) {
//		this.state = state;			
//	}

	/**
	 * Sets the street address field
	 * @param street
	 */
//	public void setStreetAddress(String street) {
//		streetAddress = street;			
//	}

	/**
	 * Sets the last name field
	 * @param lastName
	 */
//	public void setLastName(String lastName) {
//		this.lastName = lastName;			
//	}
	
	/**
	 * Sets the first name field
	 * @param firstName
	 */
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;			
//	}	

	/**
	 * Sets the password field
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Sets the email field
	 * @param email the email to set
	 */
//	public void setEmail(String email) {
//		this.email = email;
//	}
	
	/**
	 * Sets the phone number field
	 * @param phone the phone to set
	 */
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
	
	/**
	 * Sets the access level field
	 * @param accessLevel the accessLevel to set
	 */
	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	private void setUserName(String userName) {
		this.userName = userName;
	}

	private void setToUpperUserName(String userName){
		toUpperUserName = userName.toUpperCase();
	}
	
	//Utility Methods
	
		/**
		 * Provides a deep clone of a Person JDO. The returned JDO will
		 * not be persisted in the Datastore if changes are made to it's
		 * fields. 
		 * @return cloned Person JDO
		 */
		@Override
		public Person clone(){
			Person other = null;
			try {
				 other = (Person) super.clone();
			} catch (CloneNotSupportedException e) {
				//Will not happen as Person implements Cloneable
				e.printStackTrace();
			}
			
			KeyFactory.Builder keyBuilder = new KeyFactory.Builder(PARENTKEY);
			other.id = keyBuilder.addChild(KIND, other.userName).getKey();
			
			other.parentKey = null;
			
			other.setContactInfo(getContactInfo().clone());
			
			return other;			
		}
}

