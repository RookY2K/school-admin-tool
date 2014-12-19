package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import edu.uwm.owyh.jdo.OfficeHours;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.jdo.Section;
import edu.uwm.owyh.jdowrappers.PersonWrapper.AccessLevel;
import edu.uwm.owyh.library.EmailHelper;
import edu.uwm.owyh.library.PropertyHelper;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Email;

@SuppressWarnings("serial")
public class Admin extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if (!auth.verifyAdmin(response))
			return;
		
		request.getRequestDispatcher(
				request.getContextPath() + "/admin/admin.jsp").forward(request,
				response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Auth auth = Auth.getAuth(request);
		if (!auth.verifyAdmin(response))
			return;

		Map<String, Object> properties = null;
		List<String> errors = new ArrayList<String>();

		AccessLevel accessLevel = null;
		if (request.getParameter("accesslevel") != null) {
			try {
				int access = Integer.parseInt(request
						.getParameter("accesslevel"));
				accessLevel = AccessLevel.getAccessLevel(access);
			} catch (NumberFormatException e) {
				errors.add("Invalid AccessLevel!");
			}
		}

		// Add new User
		if (request.getParameter("addnewuser") != null) {

			String password = request.getParameter("password");
			String verifyPassword = request.getParameter("verifypassword");
			String email = request.getParameter("email");
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");

			if (password == null || password.equals("")
					|| verifyPassword == null)
				errors.add("Invalid Password!");
			if (password != null && !password.equals(verifyPassword))
				errors.add("Passwords does not match!");
			if (email == null || email.equals(""))
				errors.add("Bad Email input!");

			if (errors.isEmpty()) {
				properties = PropertyHelper.propertyMapBuilder("firstname", firstname
						,"lastname", lastname
						,"email", email
						,"phone", "",
						"streetaddress", "",
						"city", "",
						"state", "",
						"zip", ""
						,"password",password
						,"accesslevel", accessLevel
						,"officeroom", "",
						"skills", new ArrayList<String>()
					);
				for (String key : properties.keySet())
					if (properties.get(key) == null)
						properties.put(key, "");
				
				errors = WrapperObjectFactory.getPerson().addObject(email, properties);
				if (errors.isEmpty()) {
					Key id = WrapperObjectFactory.generateIdFromUserName(email);
					WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(id);
					if (user == null)
						errors.add("Username was not found!");

					String name = user.getProperty("firstname") + " " + user.getProperty("lastname");
					String msg = "Off With Your Head \n Your Account has been created. \n Your username is: "
							+ email + "\n Your password is: " + password;
					errors = Email.sendMessage(email, name, "OWYH New Account",	msg);
				}
			}

			request.setAttribute("addnewusererrors", errors);
			request.setAttribute("newuser", properties);
			request.getRequestDispatcher(request.getContextPath() + "/admin/admin.jsp").forward(request, response);			
			return;
		}

		// Add Contact Information
		if (request.getParameter("addcontactinfo") != null) {
			properties = PropertyHelper.propertyMapBuilder("firstname", request.getParameter("firstname")
					, "lastname",request.getParameter("lastname")
					, "email",request.getParameter("email")
					, "phone",request.getParameter("phone")
					, "streetaddress",	request.getParameter("streetaddress")
					, "city",request.getParameter("city")
					, "state",request.getParameter("state")
					, "zip",request.getParameter("zip")
					, "password", ""
					, "accesslevel", accessLevel
					,"officeroom", ""
					,"skills", new ArrayList<String>()
					);
			for (String key : properties.keySet())
				if (properties.get(key) == null)
					properties.put(key, "");
			if (properties.get("accesslevel").equals(""))
				properties.remove("accesslevel");
			errors = WrapperObjectFactory.getPerson().addObject(
					request.getParameter("email"), properties);

			if (!errors.isEmpty()) {
				request.setAttribute("addcontactinfoerrors", errors);
				request.setAttribute("baduserinfo", properties);
			} else
				request.setAttribute("newuser", properties);

			request.getRequestDispatcher(
					request.getContextPath() + "/admin/admin.jsp").forward(
					request, response);
			return;
		}

		// Load Classlist
		if (request.getParameter("reloadclassschedule") != null) {
			response.sendRedirect(request.getContextPath() + "/admin/scraper");
			return;
		}

		if (request.getParameter("triggernewsemester") != null) {
			/* Get all courses */
			WrapperObject<Course> courseWrapperObject = WrapperObjectFactory
					.getCourse();
			List<WrapperObject<Course>> courses = courseWrapperObject
					.getAllObjects();

			for (WrapperObject<Course> course : courses)
				course.removeObject();
			
			Auth.removeSessionVariable(request, "courses");
			Auth.removeSessionVariable(request, "coursekeys");

			/* Get all users. */
			WrapperObject<Person> personWrapperObject = WrapperObjectFactory
					.getPerson();
			List<WrapperObject<Person>> personList = personWrapperObject
					.getAllObjects();

			for (WrapperObject<Person> person : personList) {
				/* Remove all officehours from every person. */
				List<WrapperObject<OfficeHours>> officeHours = WrapperObjectFactory
						.getOfficeHours().findObjects(null, person, null);

				for (WrapperObject<OfficeHours> officeHoursElement : officeHours)
					officeHoursElement.removeObject();

				/* Remove all section/course assignments from people. */
				//Map<String, Object> clearedProperties = PropertyHelper.propertyMapBuilder(
				//		"sections", null, "courses", null);
				List<Section> emptySectionsList = new ArrayList<Section>();
				
				Map<String, Object> clearedProperties = PropertyHelper.propertyMapBuilder(
								"sections", emptySectionsList );
				
				person.editObject(clearedProperties);

				/* Remove all scheduled classes from people. */

			}

			/* Finally, scrape for a new semester */
			String semester = request.getParameter("semester");

			response.sendRedirect(request.getContextPath()
					+ "/admin/scraper?semester=" + semester);
			return;
		}
		
		/* Mass Emailing */
		if (request.getParameter("emailupdateprofile") != null) {
			List<String> emailList = EmailHelper.getUsersWithMissingContactInfo();
			String msg = "Off With Your Head \n Profile Update Request \n You are missing information in your Profile, please got to OWYH's website and update your Profile.";
			for (String email : emailList) {
				Email.sendMessage(email, "OWYH User", "OWYH: Account Update Request", msg);
			}
		}
		
		if (request.getParameter("emailtalabs") != null) {
			List<String> emailList = EmailHelper.getLectureInstructorsWithoutTAsAssigned();
			String msg = "Off With Your Head \n Profile Update Request \n You current have TA you need to assign to Labs for Lecture you are teaching, please got to OWYH's website and Add TA to your Labs.";
			for (String email : emailList) {
				Email.sendMessage(email, "OWYH User", "OWYH: Account Update Request", msg);
			}
		}

		if (request.getParameter("emailtaschedule") != null) {
			List<String> emailList = EmailHelper.getTAsWithMissingClassSchedules();
			String msg = "Off With Your Head \n TA Class Schedule Update Request \n Your Class Schedule is currently empty, please go to OWYH's website and make sure you have add all the class you are taking.";
			for (String email : emailList) {
				Email.sendMessage(email, "OWYH User", "OWYH: Account Update Request", msg);
			}
		}

		if (request.getParameter("emailofficehour") != null) {
			List<String> emailList = EmailHelper.getUsersWithMissingOfficeHours();
			String msg = "Off With Your Head \n Office Hours Update Request \n You currently do not have any office hours, please go to OWYH's website and update your Office Hours.";
			for (String email : emailList) {
				Email.sendMessage(email, "OWYH User", "OWYH: Account Update Request", msg);
			}
		}

		request.getRequestDispatcher(request.getContextPath() + "/admin/admin.jsp").forward(request, response);		
	}
}
