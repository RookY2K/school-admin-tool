package edu.uwm.owyh.jdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

import edu.uwm.owyh.factories.WrapperObjectFactory;

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
	@Unowned
	@Order(extensions = @Extension(vendorName="datanucleus",key="list-ordering", value="sectionNum asc"))
	private List<Section> sections;
	
	@Persistent(dependent="true")
	private ContactInfo contactInfo;
	
	@Persistent(mappedBy = "parent")
	@Element(dependent = "true")
	@Order(extensions = @Extension(vendorName="datanucleus",key="list-ordering", value="startTime asc"))
	private List<OfficeHours> officeHours;
	
	@Persistent(mappedBy = "parent")
	@Element(dependent = "true")
	@Order(extensions = @Extension(vendorName="datanucleus",key="list-ordering", value="startDate asc, startTime asc"))
	private List<TAClass> taClasses;
	
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
	
	@Persistent
	private List<String> skills;
	
	@Persistent
	private List<Key> lectureCourses;
	

	//Private constructors
	/*
	 * Private internal constructor that assigns a primary key based on the parentkey 
	 * parameter. Parentkey is generated as a constant to use for every Person jdo. This
	 * ensures that each Person entity is in the same entity group. This is useful for 
	 * keeping strong consistency in queries.
	 */
	private Person(String userName){
		id = WrapperObjectFactory.generateIdFromUserName(userName);
		
		setUserName(userName);
		setToUpperUserName(userName);
		
		/* Create an empty array list */
		officeHours = new ArrayList<OfficeHours>();
		taClasses = new ArrayList<TAClass>();
		lectureCourses = new ArrayList<Key>();
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

	//Accessors	
	
	/**
	 * Access for the Primary key
	 * @return Primary key
	 */
	public Key getId(){
		return id;
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
	
	public List<TAClass> getTAClasses(){
		return taClasses;
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
	
	/**
	 * return Person's skills
	 */
	public List<String> getSkills() {
		return skills;
	}
	
	/**
	 * @return the sections
	 */
	public List<Section> getSections() {
		return sections;
	}

	/**
	 * @return the lectureCourses
	 */
	public List<Key> getLectureCourses() {
		return lectureCourses;
	}

	/**
	 * @param sections the sections to set
	 */
	public void setSections(List<Section> sections) {
		this.sections = sections;
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
	 * Sets TA's skills
	 * @param Strings of skill in a List<String>
	 */
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	
	/**
	 * Sets the tempPassword for the Person
	 * @param password
	 */
	public void setTempPassword(String password) {
		this.tempPassword = password;
	}
	
	/**
	 * @param lectureCourses the lectureCourses to set
	 */
	public void setLectureCourses(List<Key> lectureCourses) {
		this.lectureCourses = lectureCourses;
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
	
	public void addTAClass(TAClass taClass){
		if(taClass == null)
			throw new NullPointerException("taClass cannot be null!");
		if(getTAClasses().contains(taClass))
			throw new IllegalArgumentException("Duplicate TAClass entry");
		this.getTAClasses().add(taClass);
	}
	
	public boolean removeTAClass(TAClass taClass){
		if(taClass == null){
			throw new NullPointerException("taClass cannot be null!");
		}
		
		return this.getTAClasses().remove(taClass);
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	private void setToUpperUserName(String userName){
		toUpperUserName = userName.toUpperCase();
	}

	//Utility Methods
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Person)) return false;
		
		Person other = (Person)object;
		
		return other.getUserName().equalsIgnoreCase(this.getUserName());		
	}
	
	@Override
	public int hashCode(){
		return this.getId().hashCode();
	}
}

