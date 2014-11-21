package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.jdo.OfficeHours;


public class OfficeHoursWrapper implements WrapperObject, Serializable{

	private static final long serialVersionUID = 6788916101743070177L;
	public static final String OFFICEHOURPATTERN = "^(M?T?W?R?F?) (\\d{2}|\\d):(\\d{2})(A|P)M-(\\d{2}|\\d):(\\d{2})(A|P)M$";
	
	private OfficeHours _officeHours;

	@Override
	public Key getId() {
		return _officeHours.getId();
	}

	@Override
	public Object getProperty(String propertyKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> addObject(String id, Map<String, Object> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> editObject(String id, Map<String, Object> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeObject(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WrapperObject findObject(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WrapperObject> getAllObjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
