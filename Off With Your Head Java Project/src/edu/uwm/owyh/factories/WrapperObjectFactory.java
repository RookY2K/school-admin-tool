package edu.uwm.owyh.factories;

import edu.uwm.owyh.jdowrappers.OfficeHoursWrapper;
import edu.uwm.owyh.jdowrappers.PersonWrapper;
import edu.uwm.owyh.jdowrappers.WrapperObject;

public class WrapperObjectFactory {
	
	public static WrapperObject getPerson(){
		return PersonWrapper.getPersonWrapper();
	}
	
	public static WrapperObject getOfficeHours(){
		return OfficeHoursWrapper.getOfficeHoursWrapper();
	}
}
