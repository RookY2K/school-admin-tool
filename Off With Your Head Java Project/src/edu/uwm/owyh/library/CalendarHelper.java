/**
 * 
 */
package edu.uwm.owyh.library;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.model.CellObject;
import edu.uwm.owyh.model.UserScheduleElement;

/**
 * @author Vince Maiuri
 *
 */
public final class CalendarHelper {

	WrapperObject<Person> _user;
	/**
	 * 
	 */
	private CalendarHelper() {
		
	}

	
	@SuppressWarnings("unchecked")
	public static CellObject[][] getCellObjectArray(WrapperObject<Person> user){
		List<WrapperObject<OfficeHours>> officeHours = (List<WrapperObject<OfficeHours>>) user.getProperty("officehours");
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) user.getProperty("sections");
		List<WrapperObject<TAClass>> taClasses = (List<WrapperObject<TAClass>>) user.getProperty("taclasses");
		CellObject[][] aSchedule = new CellObject[5][30];
		initializeSchedule(aSchedule);
		
		if(!officeHours.isEmpty()) fillSchedule(aSchedule, officeHours, user);
		if(!sections.isEmpty()) fillSchedule(aSchedule, sections, user);
		if(!taClasses.isEmpty()) fillSchedule(aSchedule, taClasses, user);
				
		CellObject[][] newArray = CellObject.configure(aSchedule);
		
		return newArray;
	}
	
	
	private static void initializeSchedule(CellObject[][] aSchedule){
		int width = aSchedule.length;
		int height = aSchedule[0].length;
		UserScheduleElement blankElement = new UserScheduleElement("","","","","");
		CellObject blankCell = CellObject.getCellObject(blankElement, "blank", "blank", 0.5);
		for(int i=0; i<width; ++i){
			for(int j=0; j<height; ++j){				
				aSchedule[i][j] = blankCell;
			}
		}
	}
	
	private static <E> void fillSchedule(CellObject[][] aSchedule,
			List<WrapperObject<E>> scheduleObjects, WrapperObject<Person> user) {
		
		String type = scheduleObjects.get(0).getTable().getSimpleName().toLowerCase();
		String span = getSpan(type);
				
		for(int k = 0; k < 5; k++)
		{
			String day = getDay(k);
					
			for (WrapperObject<E> scheduleObject : scheduleObjects)
			{
				String days = (String) scheduleObject.getProperty("days");
				String startTime = (String) scheduleObject.getProperty("starttime");
				String endTime = (String) scheduleObject.getProperty("endtime");
				String room = getRoom(scheduleObject, user);
				String title = getTitle(scheduleObject);
				double length = StringHelper.parseTimeToDouble(endTime) - StringHelper.parseTimeToDouble(startTime);
				length = roundLength(length);

				UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, title);
				CellObject cell = CellObject.getCellObject(element, type, span, length);
				int count = 0;
				
				for(double i = 0; i < 15; i = i + 0.5)
				{
					String stime = StringHelper.timeToString(i + 8);
					String etime = StringHelper.timeToString(i + 8.5);
					if(element.isPartOfElement(day, stime, etime))
					{
						aSchedule[k][count] = cell;
					}
					++count;
				}							
			}							
		}	
	}


	private static <T> String getRoom(WrapperObject<T> scheduleObject, WrapperObject<Person> user) {
		String type = scheduleObject.getTable().getSimpleName();
		String room = "";
		switch(type.toLowerCase()){
		case "officehours":
			room = (String) user.getProperty("officeroom");
			break;
		case "section":
			room = (String) scheduleObject.getProperty("room");
			break;
		case "taclass":
			room = (String) scheduleObject.getProperty("classnum");
			break;
		}
		
		return room;
	}


	private static String getSpan(String type) {
		String span = "";
		
		switch(type.toLowerCase()){
		case "officehours":
			span = "office-hour";
			break;
		case "section":case "taclass":
			span = "class-hour";
			break;			
		}
		
		return span;
	}


	private static <T> String getTitle(WrapperObject<T> scheduleObject) {
		String type = scheduleObject.getTable().getSimpleName();
		String title = "";
		switch(type.toLowerCase()){
		case "officehours":
			title = "Office Hours";
			break;
		case "section":
			Key parentKey = scheduleObject.getId().getParent();
			title = "COMPSI-" + parentKey.getName();
			break;
		case "taclass":
			title = (String) scheduleObject.getProperty("classname");
			break;
		}
		return title;
	}


	private static String getDay(int k) {
		String day = "";
		switch(k){
		case 0: 
			day = "M";
			break;
		case 1: 
			day = "T";
			break;
		case 2:
			day = "W";
			break;
		case 3:
			day = "R";
			break;
		case 4:
			day = "F";
			break;				
		}
		
		return day;
	}


	private static double roundLength(double l)
	{
		double length = l;
		double decimalPortion = length % 1;
		
		if(decimalPortion > 0.5) length = Math.floor(length + .5);
		else if (decimalPortion > 0.0) length = Math.floor(length) + .5;
		return length;
	}

}
