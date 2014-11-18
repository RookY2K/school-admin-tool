package edu.uwm.owyh.jdo;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ContactInfo implements Serializable, Cloneable{

	private static final long serialVersionUID = -8986408783610416695L;

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
	 * Returns the primary key for ContactInfo jdo
	 * @return Key id
	 */
	public Key getId(){
		return id;
	}
	
	/**
	 * Returns the parent Person who the contact info belongs
	 * to
	 * @return Person contactee
	 */
	public Person getContactee(){
		return contactee;
	}
	
	public String getEmail(){
		return email;
	}
	
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
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public void setStreetAddress(String streetAddress){
		this.streetAddress = streetAddress;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public void setZip(String zip){
		this.zip = zip;
	}
	
	//Utility Methods
	
	@Override
	public ContactInfo clone(){
		ContactInfo other = null;
		
		try{
			other = (ContactInfo) super.clone();
		}catch(CloneNotSupportedException e){
			//Won't happen
			e.printStackTrace();
		}		
		
		other.contactee = null;
				
		return other;
	}
}
