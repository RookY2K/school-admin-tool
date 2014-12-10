package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.library.Library;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.Email;

@SuppressWarnings("serial")
public class EmailAdmin extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (!Auth.getAuth(request).verifyUser(response)) return;
		
		WrapperObject<Person> self = (WrapperObject<Person>) Auth.getSessionVariable(request, "user");
		request.setAttribute("myemail", self.getProperty("email"));
		request.getRequestDispatcher(request.getContextPath() + "/emailadmin.jsp").forward(request, response);
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String returnURI = request.getParameter("returnURI");
		String email = request.getParameter("targetEmail");
		String message = request.getParameter("message");
		
		if (returnURI == null)
			request.getParameter(request.getContextPath() + "/");
		
		if (returnURI.endsWith(".jsp"))
			returnURI = returnURI.replace(".jsp", "");
		
		/* Limit Email to 4 per Sessions */
		if (Library.setSessionActionLimit(request, "sendAdminEmail", 4)) {
			response.sendRedirect(request.getContextPath() + returnURI + "#emailtoomany");
			return;
		}

		if (email != null && message != null) {		
			String addMsg = "You have gotten a help request from OWYH \n The user enter his email as: " + email + " \n Here is there message: \n\n";
			Email.sendMessage(Email.adminEmail, "OWYH Admin", "OWYH Help Request", addMsg + message);
		}
		
		response.sendRedirect(request.getContextPath() + returnURI + "#emailsent");
		
	}
}
