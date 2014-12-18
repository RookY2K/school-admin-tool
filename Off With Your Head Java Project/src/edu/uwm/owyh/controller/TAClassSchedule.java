package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdo.TAClass;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class TAClassSchedule extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (!Auth.getAuth(request).verifyUser(response)) return;
		
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		
		List<WrapperObject<TAClass>> classes = (List<WrapperObject<TAClass>>) self.getProperty("taclasses");
		
		checkCourseList(request);
		request.setAttribute("classes", PropertyHelper.makeTAClassProperties(classes));
		request.getRequestDispatcher("taclassschedule.jsp").forward(request, response);	
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (!Auth.getAuth(request).verifyUser(response)) return;
		
		List<String> errors = new ArrayList<String>();
		List<String> messages = new ArrayList<String>();
		WrapperObject<Course> selectedCourse = null;
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");	
		
		String courseNumString = (String) request.getParameter("courselist");
		if (courseNumString != null) {
			Map<Integer, WrapperObject<Course>> courses = checkCourseList(request);
			int courseNum = Integer.parseInt(courseNumString);
			selectedCourse = courses.get(courseNum);
			request.setAttribute("selectedCourseNumber", courseNum);
			request.setAttribute("selectedcourse", selectedCourse);
		}
		
		/* ADD new COMPSCI Class to schedule */
		if (request.getParameter("addCSClass") != null) {
			
			String sectionNum = request.getParameter("addCSClass");
			sectionNum = sectionNum.substring(sectionNum.indexOf(" ") + 1, sectionNum.length());
			List<WrapperObject<Section>> sectionList = WrapperObjectFactory.getSection().findObjects("sectionNum == '" + sectionNum + "'", selectedCourse, null);

			if (sectionList.size() == 1) { // && selectedCourse != null
				WrapperObject<Section> section = sectionList.get(0);
			
				String sectionNumType= (String) section.getProperty("sectionnum");
				String classtype = sectionNumType.substring(0, sectionNumType.indexOf(" "));
				String classnum = "COMPSCI " + selectedCourse.getProperty("coursenum");
				String days = (String) section.getProperty("days");
				
				Map<String, Object> properties = PropertyHelper.propertyMapBuilder(
						"days", section.getProperty("days")
						, "starttime", section.getProperty("starttime")
						, "endtime", section.getProperty("endtime")
						, "classnum", classnum
						, "classname", classnum
						, "classtype", classtype
						, "enddate", section.getProperty("enddate")
						, "startdate", section.getProperty("startdate")
					);
				for (String key : properties.keySet())
					if (properties.get(key) == null)
						properties.put(key, "");
				
				List<WrapperObject<TAClass>> taClassList = (List<WrapperObject<TAClass>>) self.getProperty("taclasses");
				for (WrapperObject<TAClass> taClass : taClassList) {
					String taClassNum = (String) taClass.getProperty("classnum");
					String taClassType = (String) taClass.getProperty("classtype");
					String taClassDay = (String) taClass.getProperty("classtype");
					if (taClassNum.equalsIgnoreCase(classnum) && taClassType.equalsIgnoreCase(classtype)) {
						errors.add("You already have this class in your schedule and you can only take one Lecuture and Lab per Course.");
					}
						
				}
				if (errors.isEmpty())
					errors = WrapperObjectFactory.getTAClass().addObject((String)self.getProperty("username"), properties);
				if (errors.isEmpty())
					messages.add("New class was added to schedule.");

			}
			else
				errors.add("Could not find section to add");
				
		}
		
		/* ADD new NON-COMPSCI Class to schedule */
		if (request.getParameter("addNonCSClass") != null) {
			String days = "";
			String starttime = "";
			String endtime = "";
			if (request.getParameter("M") != null)
				days += "M";
			if (request.getParameter("T") != null)
				days += "T";
			if (request.getParameter("W") != null)
				days += "W";
			if (request.getParameter("R") != null)
				days += "R";
			if (request.getParameter("F") != null)
				days += "F";
			if (request.getParameter("start_hour") != null)
				starttime += request.getParameter("start_hour") + ":";
			if (request.getParameter("start_minute") != null)
				starttime += request.getParameter("start_minute");
			if (request.getParameter("start_ampm") != null)
				starttime += request.getParameter("start_ampm");
			if (request.getParameter("end_hour") != null)
				endtime += request.getParameter("end_hour") + ":";
			if (request.getParameter("end_minute") != null)
				endtime += request.getParameter("end_minute");
			if (request.getParameter("end_ampm") != null)
				endtime += request.getParameter("end_ampm");

			String classnum = request.getParameter("classnum1") + " " + request.getParameter("classnum2");
			String classname = request.getParameter("classname");
			String classtype = request.getParameter("classtype");
			
			Map<String, Object> properties = PropertyHelper.propertyMapBuilder(
					"days", days
					, "starttime", starttime
					, "endtime", endtime
					, "classnum", classnum
					, "classname", classname
					, "classtype", classtype
					, "enddate", "3/1"
					, "startdate", "2/2"
				);
			for (String key : properties.keySet())
				if (properties.get(key) == null)
					properties.put(key, "");
						
			errors = WrapperObjectFactory.getTAClass().addObject((String)self.getProperty("username"), properties);
			if (errors.isEmpty()) {
				messages.add("New class was added to schedule.");
			}
		}
		
		/* Remove a Class From Schedule */
		if (request.getParameter("removeClass") != null) {
			
		}
		
		request.setAttribute("errors", errors);
		request.setAttribute("messages", messages);
		
		doGet(request, response);
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<Integer, WrapperObject<Course>> checkCourseList(HttpServletRequest request) {
		Map<Integer, WrapperObject<Course>> courses = (Map<Integer, WrapperObject<Course>>) Auth.getSessionVariable(request, "courses");
		if (courses == null) {
			List<WrapperObject<Course>> courseList = (List<WrapperObject<Course>>)WrapperObjectFactory.getCourse().getAllObjects();
			courses = new HashMap<Integer, WrapperObject<Course>>();			
			
			for(WrapperObject<Course> course : courseList){
				course.getProperty("sections"); //touching sections to avoid lazy load issues
				courses.put(Integer.parseInt((String)course.getProperty("coursenum")),course);
			}
			
			List<Integer> courseKeyList = new ArrayList<Integer>(courses.keySet());
			Collections.sort(courseKeyList);
			Auth.setSessionVariable(request, "coursekeys", courseKeyList);
			
			Auth.setSessionVariable(request, "courses", courses);		
		}
		
		return courses;
	}
}
