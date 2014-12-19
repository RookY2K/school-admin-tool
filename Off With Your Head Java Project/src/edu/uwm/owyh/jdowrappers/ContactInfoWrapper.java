/**
 * 
 */
package edu.uwm.owyh.jdowrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.uwm.owyh.factories.WrapperObjectFactory;
import edu.uwm.owyh.interfaces.WrapperObject;
import edu.uwm.owyh.jdo.ContactInfo;
import edu.uwm.owyh.jdo.Person;
import edu.uwm.owyh.model.DataStore;

/**
 * @author Vince Maiuri
 *
 */
public class ContactInfoWrapper implements Serializable, WrapperObject<ContactInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3517285569099672327L;
	private ContactInfo _contactInfo;

	private static WrapperObject<ContactInfo> getContactInfoWrapper(
			ContactInfo item) {
		ContactInfoWrapper other = getContactInfoWrapper();
		other._contactInfo = item;
		
		return other;
	}

	public static ContactInfoWrapper getContactInfoWrapper() {
		return new ContactInfoWrapper();
	}

	/**
	 * 
	 */
	private ContactInfoWrapper() {
		//default constructor
	}

	private ContactInfo getContactInfo() {
		return _contactInfo;
	}

	@Override
	public Key getId() {
		return getContactInfo().getId();
	}
	

	@Override
	public Class<ContactInfo> getTable() {
		return ContactInfo.getClassname();
	}

	@Override
	public Object getProperty(String propertyKey) {
		return getParent().getProperty(propertyKey);
	}

	@Override
	public List<String> addObject(String id, Map<String, Object> properties) {
		return getParent(id).editObject(properties);
	}

	@Override
	public List<String> editObject(Map<String, Object> properties) {
		return getParent().editObject(properties);
	}

	@Override
	public boolean removeObject() {
		return false;
	}

	@Override
	public boolean removeObjects(List<WrapperObject<ContactInfo>> objects) {
		return false;
	}

	@Override
	public <T> List<WrapperObject<ContactInfo>> findObjects(String filter,
			WrapperObject<T> parent, String order) {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<ContactInfo>> contactInfos = null;
		List<ContactInfo> entities = store.findEntities(getTable(), filter, parent, order);
		contactInfos = getContactInfoFromList(entities);
		
		return contactInfos;		
	}

	@Override
	public WrapperObject<ContactInfo> findObjectById(Key key) {
		DataStore store = DataStore.getDataStore();
		ContactInfo contactInfo =  (ContactInfo) store.findEntityById(getTable(), key);
		
		if(contactInfo == null) return null;
		
		return getContactInfoWrapper(contactInfo);
	}

	@Override
	public List<WrapperObject<ContactInfo>> getAllObjects() {
		DataStore store = DataStore.getDataStore();
		List<WrapperObject<ContactInfo>> contactInfos = null;
		contactInfos = getContactInfoFromList(store.findEntities(getTable(), null, null, null));
	
		return contactInfos;
	}

	@Override
	public List<String> addChildObject(Object childJDO)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContactInfo does not have any children entities");
	}

	@Override
	public boolean removeChildObject(Object childJDO)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContactInfo does not have any children entities");
	}
	
	private List<WrapperObject<ContactInfo>> getContactInfoFromList(
			List<ContactInfo> entities) {
		List<WrapperObject<ContactInfo>> contactInfos = new ArrayList<WrapperObject<ContactInfo>>();
		for (ContactInfo item : entities)
			contactInfos.add(ContactInfoWrapper.getContactInfoWrapper(item));
		return contactInfos;
	}

	private WrapperObject<Person> getParent(String userName){
		Key parentKey = WrapperObjectFactory.generateIdFromUserName(userName);
		
		return WrapperObjectFactory.getPerson().findObjectById(parentKey);
	}
	
	private WrapperObject<Person> getParent(){
		Key parentKey = getContactInfo().getId().getParent();
		
		return WrapperObjectFactory.getPerson().findObjectById(parentKey);
	}

}
