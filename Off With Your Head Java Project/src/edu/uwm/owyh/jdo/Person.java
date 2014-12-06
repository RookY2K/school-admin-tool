package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.library.Library;

/**
 * Person jdo class
 * @author Vince Maiuri
 */
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
	
	@Persistent(dependent="true")
	private ContactInfo contactInfo;
	
	@Persistent(mappedBy = "parentPerson")
	@Element(dependent = "true")
	private List<OfficeHours> officeHours;
	
	@Persistent
	private String userName;
	
	@Persistent
	private String toUpperUserName;
	
	@Persistent
	private String password;
	
	@Persistent
	private Integer accessLevel;
	
	@Persistent
	private String officeRoom;
	
	@Persistent
	private String tempPassword;

	//Private constructors
	/*
	 * Private internal constructor that assigns a primary key based on the parentkey 
	 * parameter. Parentkey is generated as a constant to use for every Person jdo. This
	 * ensures that each Person entity is in the same entity group. This is useful for 
	 * keeping strong consistency in queries.
	 */
	private Person(String userName){
		id = Library.generateIdFromUserName(userName);
		
		setUserName(userName);
		setToUpperUserName(userName);
		
		/* Create an empty array list */
		officeHours = new ArrayList<OfficeHours>();
		
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
	
	/**
	 * @return the contactInfo child JDO
	 */
	public ContactInfo getContactInfo(){
		return contactInfo;
	}

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
	 * @return the accessLevel
	 */
	public Integer getAccessLevel() {
		return accessLevel;
	}
	
	/**
	 * Returns a list of all office hours
	 * @return List<OfficeHours> officeHours
	 */
	public List<OfficeHours> getOfficeHours()
	{
		return officeHours;
	}
	
	/**
	 * return office room
	 * @param room
	 */
	public String getOfficeRoom() {
		return officeRoom;
	}
	
	/**
	 * return temporary password
	 */
	public String getTempPassword() {
		return tempPassword;
	}
	
	//Mutators
	/**
	 * Sets the contactinfo for the Person
	 * @param info
	 */
	public void setContactInfo(ContactInfo info){
		contactInfo = info;
	}	

	/**
	 * Sets the password field
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
	
	/**
	 * Sets the access level field
	 * @param accessLevel the accessLevel to set
	 */
	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	/**
	 * Sets the offceRoom for the Person
	 * @param room
	 */
	public void setOfficeRoom(String room) {
		this.officeRoom = room;
	}
	
	/**
	 * Sets the tempPassword for the Person
	 * @param password
	 */
	public void setTempPassword(String password) {
		this.tempPassword = password;
	}
	
	private void setUserName(String userName) {
		this.userName = userName;
	}

	private void setToUpperUserName(String userName){
		toUpperUserName = userName.toUpperCase();
	}
	
	/**
	 * Adds the specified office hours to the list of office hours.
	 * @param officeHours - Child OfficeHours jdo to add to the Person JDO list field.
	 */
	public void addOfficeHours(OfficeHours officeHours)
	{
		if(officeHours == null) 
			throw new NullPointerException("officeHours cannot be null!");
		if(getOfficeHours().contains(officeHours))
			throw new IllegalArgumentException("Duplicate officeHours entry!");
		this.officeHours.add(officeHours);
	}

	/**
	 * Removes one officehour jdo from the OfficeHours list field
	 * @param officeHours
	 * @return true if the officehours was successfully removed
	 */
	public boolean removeOfficeHours(OfficeHours officeHours){
		if(officeHours == null) 
			throw new NullPointerException("officeHours cannot be null!");
		
		return this.officeHours.remove(officeHours);
	}

	//Utility Methods
}

