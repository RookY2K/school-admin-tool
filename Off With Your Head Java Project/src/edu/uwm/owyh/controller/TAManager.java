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

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;

@SuppressWarnings("serial")
public class TAManager extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (!Auth.getAuth(request).verifyAdmin(response)) return;
		
		String filterTA = "(accessLevel == " + AccessLevel.TA.getVal() + ")";
		List<WrapperObject<Person>> taPersonList =  WrapperObjectFactory.getPerson().findObjects(filterTA, null, null);
		List<Map<String, Object>> taList = new ArrayList<Map<String, Object>>();
		Map<String, Object> taSkillList = new HashMap<String, Object>();
		List<String> skillSelectedList = (List<String>) Auth.getSessionVariable(request, "skillSelectList");
		List<Map<String, Object>>  taFromSelectedCourseList = (List<Map<String, Object>>) request.getAttribute("taFromSelectedCourseList");
		
		/* triple for loop get TA list from selected Course */
		for (WrapperObject<Person> ta : taPersonList) {
			List<String> skills = (List<String>) ta.getProperty("skills");
			for (String skill : skills) 
				taSkillList.put(skill, skill);
			boolean taMatch = false;
			if (taFromSelectedCourseList != null) {
				for (Map<String, Object> taFromSelectedCourse : taFromSelectedCourseList) {
					if (((String)ta.getProperty("email")).equalsIgnoreCase((String) taFromSelectedCourse.get("email"))) {
						taMatch = true;
						break;
					}
				}
			}
			if (!taMatch) {
				if (skillSelectedList == null || skillSelectedList.isEmpty()) {
					taList.add(PropertyHelper.makeUserProperties(ta));	
				}
				else {
					int taListCount = taList.size();
					for (String skill : skillSelectedList) {
						for (String checkSKill : skills) {
							if (skill.equalsIgnoreCase(checkSKill)) {
								taList.add(PropertyHelper.makeUserProperties(ta));
								break;
							}
						}
						if (taListCount < taList.size())
							break;
					}
				}
			}
		}
		
		List<String> taSkills = new ArrayList<String>(taSkillList.keySet());
		request.setAttribute("listofta", taList);
		request.setAttribute("listoftaskills", taSkills);
		
		checkCourseList(request);
		
		request.getRequestDispatcher(request.getContextPath() + "tamanager.jsp").forward(request, response);	
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (!Auth.getAuth(request).verifyAdmin(response)) return;
		
		List<String> messages = new ArrayList<String>();
		List<String> errors = new ArrayList<String>();


		/* Get TA List based on Selected Course */
		String courseNumString = (String) request.getParameter("courselist");
		
		/* TA Skill Filtering*/
		if (request.getParameter("skillcount") != null) {
			List<String> skillSelectList = new ArrayList<String>();
			int skillSelectCount = Integer.parseInt(request.getParameter("skillcount"));
			for (int i = 0; i < skillSelectCount; i++) {
				if (request.getParameter("skill" + i) != null)
					skillSelectList.add(request.getParameter("skill" + i));
			}
			Auth.setSessionVariable(request, "skillSelectList", skillSelectList);
		}
		
		if (request.getParameter("skillclear") != null) {
			Auth.removeSessionVariable(request, "skillSelectList");
		}
		
		if (courseNumString != null) {
			int courseNum = Integer.parseInt(courseNumString);

			Map<Integer, WrapperObject<Course>> courses = checkCourseList(request);
			WrapperObject<Course> selectedCourse = courses.get(courseNum);
			List<Key> taKeyList = new ArrayList<Key>();
			List<Map<String, Object>> taFromSelectedCourseList = new ArrayList<Map<String, Object>>();
			
			if (selectedCourse != null) {
				if (selectedCourse.getProperty("eligibletakeys") != null) {
					taKeyList = (List<Key>) selectedCourse.getProperty("eligibletakeys");
				}
				
				/* Add a TA to a Course */
				String email = (String) request.getParameter("taEmail");
				if (request.getParameter("addTAtoCourse") != null) {
					List<WrapperObject<Person>> taFindList = WrapperObjectFactory.getPerson().findObjects("toUpperUserName == '" + email.toUpperCase() + "'" , null, null);
					if (taFindList != null && !taFindList.isEmpty()) {
						taKeyList.add(taFindList.get(0).getId());
						Map<String, Object> properties = PropertyHelper.propertyMapBuilder("eligibletakeys", taKeyList);
						errors =selectedCourse.editObject(properties);
						if (errors.isEmpty())
							messages.add("TA was added to Course!");
					}
					else {
						errors.add("Could not find TA to add to Course!");
					}
				}
				
				/* Remove a TA from a Couse */
				if (request.getParameter("removeTAfromCourse") != null) {
					List<WrapperObject<Person>> taFindList = WrapperObjectFactory.getPerson().findObjects("toUpperUserName == '" + email.toUpperCase() + "'" , null, null);
					if (taFindList != null && !taFindList.isEmpty()) {
						for (int i = 0; i < taKeyList.size();) {
							if (taKeyList.get(i).equals(taFindList.get(0).getId()))
								taKeyList.remove(i);
							else
								i++;
						}
						
						Map<String, Object> properties = PropertyHelper.propertyMapBuilder("eligibletakeys", taKeyList);
						errors =selectedCourse.editObject(properties);
						if (errors.isEmpty())
							messages.add("TA was removed to Course!");
					}
					else {
						errors.add("Could not find TA to remove from Course!");
					}
				}
				
				for (Key key : taKeyList) {
					WrapperObject<Person> ta = WrapperObjectFactory.getPerson().findObjectById(key);
					taFromSelectedCourseList.add(PropertyHelper.makeUserProperties(ta));
				}
				
				// -72 mean do not filter TA list
				if (courseNum != -72)
					request.setAttribute("taFromSelectedCourseList", taFromSelectedCourseList);
			}
			request.setAttribute("selectedCourseNumber", courseNum);
		}
		
		request.setAttribute("errors", errors);
		request.setAttribute("messages", messages);
		
		doGet(request, response);
		
		//request.getRequestDispatcher(request.getContextPath() + "tamanager.jsp").forward(request, response);
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