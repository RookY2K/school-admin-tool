/**
 * 
 */
package edu.uwm.owyh.library;

import java.util.List;

import edu.uwm.owyh.factories.WrapperObjectFactory;
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
		List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(null, user, null);
		List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) user.getProperty("sections");
		List<WrapperObject<TAClass>> taClasses = (List<WrapperObject<TAClass>>) user.getProperty("taclasses");
		CellObject[][] array = new CellObject[5][30];
		
		if(officeHours.size() != 0)
		{
			String days;
			String startTime;
			String endTime;
			String room;	
			String day = "";
			double length;
			
			for(int k = 0; k < 5; k++)
			{
				if(k == 0) day = "M";
				if(k == 1) day = "T";
				if(k == 2) day = "W";
				if(k == 3) day = "R";
				if(k == 4) day = "F";
				
				for (WrapperObject<OfficeHours> hours : officeHours)
				{
					days = (String) hours.getProperty("days");
					startTime = (String) hours.getProperty("starttime");
					endTime = (String) hours.getProperty("endtime");
					room = (String) user.getProperty("officeroom");
					length = StringHelper.parseTimeToDouble(endTime) - StringHelper.parseTimeToDouble(startTime);
					length = roundLength(length);

					UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, "Office Hours");
					CellObject cell = CellObject.getCellObject(element, "officehours", "office-hour", length);
					int count = 0;
					
					for(double i = 0; i < 15; i = i + 0.5)
					{
						String stime = StringHelper.timeToString(i + 8);
						String etime = StringHelper.timeToString(i + 8.5);
						if(element.isPartOfElement(day, stime, etime))
						{
							array[k][count] = cell;
						}
						else
						{
							if(array[k][count] == null)
							{
								UserScheduleElement blankElement = new UserScheduleElement("","","","","");
								CellObject blankCell = CellObject.getCellObject(blankElement, "blank", "blank", 0.5);
								array[k][count] = blankCell;										
							}
						}
						++count;
					}							
				}							
			}	
		}
		
		if (sections.size() != 0)
		{
			String days;
			String startTime;
			String endTime;
			String room;
			String title;
			String day = "";
			double length;					
			
			for(int k = 0; k < 5; k++)
			{
				if(k == 0) day = "M";
				if(k == 1) day = "T";
				if(k == 2) day = "W";
				if(k == 3) day = "R";
				if(k == 4) day = "F";
				
				for (WrapperObject<Section> course : sections)
				{	
					days = (String) course.getProperty("days");
					startTime = (String) course.getProperty("starttime");
					endTime = (String) course.getProperty("endtime");
					room = (String) course.getProperty("room");
					title = (String) course.getProperty("sectionNum");
					length = StringHelper.parseTimeToDouble(endTime) - StringHelper.parseTimeToDouble(startTime);
					length = roundLength(length);
					
					UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, title);
					CellObject cell = CellObject.getCellObject(element, "section", "class-hour", length);							
					int count = 0;
					for(double i = 0; i < 15; i = i + 0.5)
					{
						String stime = StringHelper.timeToString(i + 8);
						String etime = StringHelper.timeToString(i + 8.5);
						if(element.isPartOfElement(day, stime, etime))
						{
							array[k][count] = cell;
						}
						else
						{
							if(array[k][count] == null)
							{
								UserScheduleElement blankElement = new UserScheduleElement("","","","","");
								CellObject blankCell = CellObject.getCellObject(blankElement, "blank", "blank", 0.5);
								array[k][count] = blankCell;										
							}
						}
						++count;
					}	
				}
			}	

		}
		
		if (taClasses.size() != 0)
		{
			String days;
			String startTime;
			String endTime;
			String room;
			String title;
			String day = "";
			double length;					
			
			for(int k = 0; k < 5; k++)
			{
				if(k == 0) day = "M";
				if(k == 1) day = "T";
				if(k == 2) day = "W";
				if(k == 3) day = "R";
				if(k == 4) day = "F";
				
				for (WrapperObject<TAClass> classes : taClasses)
				{	
					days = (String) classes.getProperty("days");
					startTime = (String) classes.getProperty("startTime");
					endTime = (String) classes.getProperty("endTime");
					room = (String) classes.getProperty("classNum");
					title = (String) classes.getProperty("className");
					length = StringHelper.parseTimeToDouble(endTime) - StringHelper.parseTimeToDouble(startTime);
					length = roundLength(length);
					
					UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, title);
					CellObject cell = CellObject.getCellObject(element, "section", "class-hour", length);							
					int count = 0;
					
					for(double i = 0; i < 15; i = i + 0.5)
					{
						String stime = StringHelper.timeToString(i + 8);
						String etime = StringHelper.timeToString(i + 8.5);
						if(element.isPartOfElement(day, stime, etime))
						{
							array[k][count] = cell;
						}
						else
						{
							if(array[k][count] == null)
							{
								UserScheduleElement blankElement = new UserScheduleElement("","","","","");
								CellObject blankCell = CellObject.getCellObject(blankElement, "blank", "blank", 0.5);
								array[k][count] = blankCell;										
							}
						}
						++count;
					}	
				}
			}	

		}
		
		else if(officeHours.size() == 0 && sections.size() == 0 && taClasses.size() == 0)
		{
				for(int k = 0; k < 5; k++)
				{	
						int count = 0;	
						for(double i = 0; i < 15; i = i + 0.5)
						{
							UserScheduleElement blankElement = new UserScheduleElement("","","","","");
							CellObject blankCell = CellObject.getCellObject(blankElement, "blank", "blank", 0.5);
							array[k][count] = blankCell;										
							++count;
						}														
				}											
		}
		
		
		CellObject[][] newArray = CellObject.configure(array);
		
		return newArray;
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
