package edu.uwm.owyh.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.uwm.owyh.exceptions.BuildJDOException;
import edu.uwm.owyh.jdo.Course;
import edu.uwm.owyh.library.ScrapeUtility;
import edu.uwm.owyh.model.Auth;
import edu.uwm.owyh.model.DataStore;

@SuppressWarnings("serial")
public class LocalScraper extends HttpServlet{

	private ServletContext _context;
	@Override
	public void init(javax.servlet.ServletConfig config) throws ServletException {
		super.init(config);
		_context = config.getServletContext();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String fullPath = _context.getRealPath("/WEB-INF/resources/coursePage.txt");
		try {
			ScrapeUtility.readCoursePageIntoLocalDataStore(fullPath);
		} catch (BuildJDOException e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}

		response.sendRedirect("/classlist");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Auth auth = Auth.getAuth(request);
		if(!auth.verifyAdmin(response)) return;

		DataStore store = DataStore.getDataStore();

		if(request.getParameter("delete_courses") != null){		
			Key parentKey = KeyFactory.createKey("Courses", "RootCourses");

			String filterWithParent = "parentKey == '" + KeyFactory.keyToString(parentKey) + "'";
			List<Course> entities = store.findEntities(Course.class, filterWithParent, null, null);
			
			Auth.removeSessionVariable(request, "courses");
			Auth.removeSessionVariable(request, "coursekeys");

			store.deleteAllEntities(entities);

			store.closeDataStore();
		}

		response.sendRedirect("/classlist");
	}

}
