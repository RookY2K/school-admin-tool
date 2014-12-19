package edu.uwm.owyh.jdo;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * JDO class for ContactInfo
 * @author Vince Maiuri
 */
@PersistenceCapable
public class ContactInfo implements Serializable, Cloneable{

	private static final long serialVersionUID = -8986408783610416695L;
	private static final String KIND = ContactInfo.class.getSimpleName();
	private static final Class<ContactInfo> CLASSNAME = ContactInfo.class;

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent(mappedBy = "contactInfo")
	private Person contactee;
	
	@Persistent
	private String email;
	
	@Persistent 
	private String phone;
	
	@Persistent
	private String firstName;
	
	@Persistent
	private String lastName;
	
	@Persistent
	private String streetAddress;
	
	@Persistent
	private String city;
	
	@Persistent
	private String state;
	
	@Persistent
	private String zip;
	
	//Constructors
	private ContactInfo(){
		//Default constructor
	}
	
	/**
	 * Public accessor for ContactInfo object
	 * @return a ContactInfo object
	 */
	public static ContactInfo getContactInfo(){
		return new ContactInfo();
	}
	
	//accessors
	
	/**
	 * @return the kind
	 */
	public static String getKind() {
		return KIND;
	}

	/**
	 * @return the classname
	 */
	public static Class<ContactInfo> getClassname() {
		return CLASSNAME;
	}

	/**
	 * Returns the primary key for ContactInfo jdo
	 * @return Key id
	 */
	public Key getId(){
		return id;
	}
	
	/**
	 * Returns the parent Person to whom the contact info belongs
	 * @return Person contactee
	 */
	public Person getContactee(){
		return contactee;
	}
	
	/**
	 * Returns the email
	 * @return the email
	 */
	public String getEmail(){
		return email;
	}
	
	/**
	 * Returns the phone number
	 * @return the phone number
	 */
	public String getPhone(){
		return phone;
	}
	
	/**
	 * Returns the first name of the contactee
	 * @return String firstName
	 */
	public String getFirstName(){
		return firstName;
	}
	
	/**
	 * Returns the last name of the contactee
	 * @return String lastName
	 */
	public String getLastName(){
		return lastName;
	}
	
	/**
	 * Returns the street address of the contactee
	 * @return String streetAddress
	 */
	public String getStreetAddress(){
		return streetAddress;
	}
	
	/**
	 * Returns the city of the contactee
	 * @return String city
	 */
	public String getCity(){
		return city;
	}
	
	/**
	 * Returns the state of the contactee
	 * @return String state
	 */
	public String getState(){
		return state;
	}
	
	/**
	 * Returns the zip code of the contactee
	 * @return String zip
	 */
	public String getZip(){
		return zip;
	}
	
	//Mutator
	
	/**
	 * Sets the email field
	 * @param email
	 */
	public void setEmail(String email){
		this.email = email;
	}
	
	/**
	 * Sets the phone number field
	 * @param phone
	 */
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	/**
	 * Sets the first name field
	 * @param firstName
	 */
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	/**
	 * Sets the last name field
	 * @param lastName
	 */
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	/**
	 * Sets the street address field
	 * @param streetAddress
	 */
	public void setStreetAddress(String streetAddress){
		this.streetAddress = streetAddress;
	}
	
	/**
	 * Sets the city field
	 * @param city
	 */
	public void setCity(String city){
		this.city = city;
	}
	
	/**
	 * Sets the state field
	 * @param state
	 */
	public void setState(String state){
		this.state = state;
	}
	
	/**
	 * Sets the zip code field
	 * @param zip
	 */
	public void setZip(String zip){
		this.zip = zip;
	}
	
	//Utility Methods
	
}
