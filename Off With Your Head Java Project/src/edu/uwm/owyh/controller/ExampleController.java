package edu.uwm.owyh.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ExampleController extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		request.setAttribute("body", "HELLO WORLD");
		
		String[] someRandomList = { "user1", "user2", "user3" };
		request.setAttribute("var", someRandomList);
		
		request.getRequestDispatcher("helloworld.jsp").forward(request, response);	
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.setContentType("text/html");
		
		String helloworld = request.getParameter("helloworld");
		
		response.getWriter().println(helloworld);
		
	}

}
