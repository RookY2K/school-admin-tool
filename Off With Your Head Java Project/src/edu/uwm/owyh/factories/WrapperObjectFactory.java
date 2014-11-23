package edu.uwm.owyh.factories;

import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.OfficeHoursWrapper;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.WrapperObject;

public class WrapperObjectFactory {
	
	public static WrapperObject<Person> getPerson(){
		return PersonWrapper.getPersonWrapper();
	}
	
	public static WrapperObject<OfficeHours> getOfficeHours(){
		return OfficeHoursWrapper.getOfficeHoursWrapper();
	}
}
