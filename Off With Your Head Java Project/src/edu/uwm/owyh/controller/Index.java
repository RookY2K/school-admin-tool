package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.CellObject;
import edu.uwm.owyh.model.UserSchedule;
import edu.uwm.owyh.model.UserScheduleElement;
import edu.uwm.owyh.library.StringHelper;
@SuppressWarnings("serial")
public class Index extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		WrapperObject<Person> client = WrapperObjectFactory.getPerson();
		
		int userCount = client.getAllObjects().size();

		if(userCount == 0){
			request.setAttribute("noUsers", true);
			request.getRequestDispatcher("/initiallogin").forward(request, response);
			return;
		}
		
		Auth auth = Auth.getAuth(request);
		boolean isLogin = auth.verifyUser();
		boolean isAdmin = auth.verifyAdmin();
		
		if (isLogin) {
			if (isAdmin){ 
				response.sendRedirect(request.getContextPath() + "/admin");
				return;
			}
			else{
				WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
				Key myId = WrapperObjectFactory.generateIdFromUserName((String) self.getProperty("username"));
				self = WrapperObjectFactory.getPerson().findObjectById(myId);
				
				UserSchedule schedule = new UserSchedule();
				List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(null, self, null);
				List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) self.getProperty("sections");
				List<WrapperObject<TAClass>> taClasses = (List<WrapperObject<TAClass>>) self.getProperty("taclasses");
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
							room = (String) self.getProperty("officeroom");
							length = StringHelper.parseTimeToDouble(endTime) - StringHelper.parseTimeToDouble(startTime);
							if(Math.floor(length) != length) length = Math.floor(length + .25) + 0.5;
							
							UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, "Office Hours");
							CellObject cell = new CellObject(element, "officehours", "office-hour", length);
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
										CellObject blankCell = new CellObject(blankElement, "blank", "blank", 0.5);
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
							if(Math.floor(length) != length) length = Math.floor(length + .25) + 0.5;
			
							UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, title);
							CellObject cell = new CellObject(element, "section", "class-hour", length);							
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
										CellObject blankCell = new CellObject(blankElement, "blank", "blank", 0.5);
										array[k][count] = blankCell;										
									}
								}
								++count;
							}	
						}
					}	

				}
				
				/*if (taClasses.size() != 0)
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
							length = Math.floor(length + .25) + 0.5;
							
							UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, title);
							CellObject cell = new CellObject(element, "section", "class-hour", length);							
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
										CellObject blankCell = new CellObject(blankElement, "blank", "blank", 0.5);
										array[k][count] = blankCell;										
									}
								}
								++count;
							}	
						}
					}	

				}*/
				
				else if(officeHours.size() == 0 && sections.size() == 0 && taClasses.size() == 0)
				{
						for(int k = 0; k < 5; k++)
						{	
								int count = 0;	
								for(double i = 0; i < 15; i = i + 0.5)
								{
									UserScheduleElement blankElement = new UserScheduleElement("","","","","");
									CellObject blankCell = new CellObject(blankElement, "blank", "blank", 0.5);
									array[k][count] = blankCell;										
									++count;
								}														
						}											
				}
				
				
				UserScheduleElement blankElement = new UserScheduleElement("","","","","");
				CellObject dummy = new CellObject(blankElement, "blank", "blank", 0.5);
				CellObject[][] newArray = dummy.configure(array);
				
				request.setAttribute("userschedule", schedule);
				request.setAttribute("array", newArray);
				request.setAttribute("self", PropertyHelper.makeUserProperties(self));
				request.getRequestDispatcher(request.getContextPath() + "/home.jsp").forward(request, response);	
				return;
			}
		}
		else {
			request.getRequestDispatcher(request.getContextPath() + "/index.jsp").forward(request, response);
			return;
		}		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request,response);
	}
}
