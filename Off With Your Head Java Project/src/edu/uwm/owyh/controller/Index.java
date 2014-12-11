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
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.UserSchedule;
import edu.uwm.owyh.model.UserScheduleElement;
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
				Key myId = Library.generateIdFromUserName((String) self.getProperty("username"));
				self = WrapperObjectFactory.getPerson().findObjectById(myId);
				
				UserSchedule schedule = new UserSchedule();
				List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory.getOfficeHours().findObjects(null, self, null);
				List<WrapperObject<Section>> sections = WrapperObjectFactory.getSection().findObjects(null, self, null);
				if (officeHours != null || sections != null)
				{
					String days;
					String startTime;
					String endTime;
					
					for (WrapperObject<OfficeHours> hours : officeHours) 
					{
						days = (String) hours.getProperty("days");
						startTime = (String) hours.getProperty("starttime");
						endTime = (String) hours.getProperty("endtime");
						UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, null, "Office Hours");
						schedule.addElement(element);
					}
				}
				
				if (sections != null)
				{
					for (WrapperObject<Section> course : sections)
					{
						String days;
						String startTime;
						String endTime;
						String room;
						String title;
						
						days = (String) course.getProperty("days");
						startTime = (String) course.getProperty("starttime");
						endTime = (String) course.getProperty("endtime");
						room = (String) course.getProperty("room");
						title = (String) course.getProperty("sectionNum");
						UserScheduleElement element = new UserScheduleElement(days, startTime, endTime, room, title);
						schedule.addElement(element);
					}
				}
				request.setAttribute("userschedule", schedule);
				request.setAttribute("self", Library.makeUserProperties(self));
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
