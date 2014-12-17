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
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.AdminHelper;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class ClassList extends HttpServlet{

	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth.removeSessionVariable(request, "errors");
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyUser(response)) return;
		
		
		Map<Integer, WrapperObject<Course>> courses = (Map<Integer, WrapperObject<Course>>) Auth.getSessionVariable(request, "courses");
		List<String> errors = (List<String>)request.getAttribute("errors");
		
		if(errors != null){
			Auth.setSessionVariable(request, "errors", errors);
			response.sendRedirect(request.getContextPath() + "/classlist.jsp");
			return;
		}
		if(courses == null){
			List<WrapperObject<Course>> courseList = (List<WrapperObject<Course>>) request.getAttribute("courselist");
			if(courseList == null) 
				courseList = (List<WrapperObject<Course>>)WrapperObjectFactory.getCourse().getAllObjects();
			if(courseList.isEmpty()){
				errors = new ArrayList<String>();
				errors.add("There is no course information in the Datastore!");
				Auth.setSessionVariable(request, "errors", errors);
				response.sendRedirect(request.getContextPath() + "/classlist.jsp");
				return;
			}
			
			courses = new HashMap<Integer, WrapperObject<Course>>();			
			
			for(WrapperObject<Course> course : courseList){
				course.getProperty("sections"); //touching sections to avoid lazy load issues
				courses.put(Integer.parseInt((String)course.getProperty("coursenum")),course);
			}
			
			List<Integer> courseKeyList = new ArrayList<Integer>(courses.keySet());
			Collections.sort(courseKeyList);
			Auth.setSessionVariable(request, "coursekeys", courseKeyList);
		}
		
		Auth.setSessionVariable(request, "courses", courses);		
		
		request.getRequestDispatcher(request.getContextPath() + "/classlist.jsp").forward(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Auth auth = Auth.getAuth(request);
		
		if(!auth.verifyUser(response)) return;
		
		List<String> errors = new ArrayList<String>();
		List<String> messages = new ArrayList<String>();
		
		Map<Integer, WrapperObject<Course>> courses = (Map<Integer, WrapperObject<Course>>) Auth.getSessionVariable(request, "courses");
		int courseNum = Integer.parseInt(request.getParameter("courselist"));
		
		WrapperObject<Course> selectedCourse = courses.get(courseNum);
				
		request.setAttribute("selectedcourse", selectedCourse);
		
		/* Edit and View Section Instructors */
		if (request.getParameter("viewsection") != null) {
			
			String sectionNumber = (String) request.getParameter("sectionnumber");
			sectionNumber = sectionNumber.substring(4);
			List<WrapperObject<Section>> sections = (List<WrapperObject<Section>>) WrapperObjectFactory.getSection().findObjects("sectionNum == '" + sectionNumber + "'", selectedCourse, null);			

			// Make sure section was actually correct
			if (sections.size() == 1) {
				WrapperObject<Section> section = sections.get(0);
				
				/* Change Section's Insructor */
				if (request.getParameter("editsection") != null) {
					String nameInfo = request.getParameter("changeinstructor");
					if (nameInfo == null) 
						errors.add("Error on Input Data for Instructor");
					else {
						List<WrapperObject<Person>> addingPerson = WrapperObjectFactory.getPerson().findObjects("toUpperUserName == '" + nameInfo.toUpperCase() + "'", null, null);
					
						if (addingPerson == null || addingPerson.size() != 1) {
							errors.add("Could not find Instructor to Add to Section");
						}
						else {
							AdminHelper.assignInstructor(addingPerson.get(0), section, false);
						}
					}
				}
				
				request.setAttribute("editsection", section);
				
				// get possible user to change section instructor to
				List<WrapperObject<Person>> editSectionUsers = null;
				WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
				if (self.getProperty("accesslevel") == AccessLevel.ADMIN) {
					String filterUser = "(accessLevel == " + AccessLevel.INSTRUCTOR.getVal() + " || accessLevel == " + AccessLevel.TA.getVal() + ")";
					editSectionUsers =  WrapperObjectFactory.getPerson().findObjects(filterUser, null, null);
				}
				else if (self.getProperty("accesslevel") == AccessLevel.INSTRUCTOR) { 
					editSectionUsers = AdminHelper.getInstructorList(section);
				}			
				request.setAttribute("editsectionusers", editSectionUsers);
			}
		}
		
		request.setAttribute("editsectionerrors", errors);
		request.setAttribute("editsectionmessages", messages);
		request.getRequestDispatcher(request.getContextPath() + "/classlist.jsp").forward(request, response);		 
	}

}
