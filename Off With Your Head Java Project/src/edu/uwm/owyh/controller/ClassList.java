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

import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;

@SuppressWarnings("serial")
public class ClassList extends HttpServlet{

	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Auth.removeSessionVariable(request, "errors");
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyUser(response)) return;
		
		
		Map<Integer, Course> courses = (Map<Integer, Course>) Auth.getSessionVariable(request, "courses");
		List<String> errors = (List<String>)request.getAttribute("errors");
		
		if(errors != null){
			Auth.setSessionVariable(request, "errors", errors);
			response.sendRedirect(request.getContextPath() + "/classlist.jsp");
			return;
		}
		if(courses == null){
			List<Course> courseList = (List<Course>) request.getAttribute("courselist");
			String parentKey = KeyFactory.keyToString(Course.getParentkey());
			String filter = "parentKey == '" + parentKey + "'";
			if(courseList == null) courseList = DataStore.getDataStore().findEntities(Course.class, filter, null);
			if(courseList.isEmpty()){
				errors = new ArrayList<String>();
				errors.add("There is no course information in the Datastore!");
				Auth.setSessionVariable(request, "errors", errors);
				response.sendRedirect(request.getContextPath() + "/classlist.jsp");
				return;
			}
			
			courses = new HashMap<Integer, Course>();			
			
			for(Course course : courseList){
				course.getSections(); //touching sections to avoid lazy load issues
				courses.put(Integer.parseInt(course.getCourseNum()),course);
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
		
		Map<Integer, Course> courses = (Map<Integer, Course>) Auth.getSessionVariable(request, "courses");
		int courseNum = Integer.parseInt(request.getParameter("courselist"));
		
		Course selectedCourse = courses.get(courseNum);
				
		request.setAttribute("selectedcourse", selectedCourse);
		
		request.getRequestDispatcher(request.getContextPath() + "/classlist.jsp").forward(request, response);		 
	}

}
