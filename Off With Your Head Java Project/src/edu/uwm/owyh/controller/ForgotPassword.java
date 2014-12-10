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
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Email;

@SuppressWarnings("serial")
public class ForgotPassword extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		request.getRequestDispatcher(request.getContextPath() + "/forgotpassword.jsp").forward(request, response);	
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String username = (String) request.getParameter("email");
		
		if (username == null || username.equals("")) {
			doGet(request, response);
			return;
		}
		
		Key myId = Library.generateIdFromUserName(username);
		WrapperObject<Person> user = WrapperObjectFactory.getPerson().findObjectById(myId);
		List<String> errors = new ArrayList<String>();
		List<String> messages = new ArrayList<String>();
		
		String requestPassword = (String) Auth.getSessionVariable(request, "requestpassword");

		if (requestPassword == null) {	
			requestPassword = "0";
			Auth.setSessionVariable(request, "requestpassword", "1");
		}
		
		/* Prevent Email Spam by Limiting Password Request to 4 */ 
		if (Library.setSessionActionLimit(request, "sendForgotPasswordEmail", 4)) {
			errors.add("You have tried to reset your password too many times. Please Email the Administrator (<a href=\"/forgotpassword#emailforhelp\">vamaiuri@uwm.edu</a>) for more help.");
			request.setAttribute("errors", errors);
			doGet(request, response);
			return;
		}
		
		if (user == null) {
			errors.add("The email was not found!");
			request.setAttribute("errors", errors);
			doGet(request, response);
			return;
		}
		
		/* Create new random password */
		String tempPassword = (String) user.getProperty("temporarypassword");
		
		if (tempPassword == null || tempPassword.equals("")) {
			tempPassword = Library.genderateRandomPassword();
			Map<String, Object> properties = Library.propertyMapBuilder("temporarypassword", tempPassword);
			errors = WrapperObjectFactory.getPerson().editObject(username, properties);
			
			if (!errors.isEmpty()) {
				request.setAttribute("errors", errors);
				doGet(request, response);
				return;				
			}
		}

		/* Email user there new password */
		messages.add("Your password has been changed, check your email for your new password.");
		String name = user.getProperty("firstname") + " " + user.getProperty("lastname");
		String msg = "Off With Your Head \n You have requsted a Password Change. \n Your new password is: " + tempPassword;
		errors = Email.sendMessage(username, name, "OWYH Password Change", msg);
		
		request.setAttribute("errors", errors);
		request.setAttribute("messages", messages);
		doGet(request, response);
		
	}

}
