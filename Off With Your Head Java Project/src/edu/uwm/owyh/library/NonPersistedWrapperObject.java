package edu.uwm.owyh.library;

import java.util.List;
import java.util.Map;

import edu.uwm.owyh.exceptions.BuildJDOException;
import edu.uwm.owyh.jdowrappers.WrapperObject;

public interface NonPersistedWrapperObject<T> extends WrapperObject<T>{
	
	abstract WrapperObject<T> buildObject(String id, Map<String,Object> properties) throws BuildJDOException;
	
	abstract boolean saveAllObjects(List<WrapperObject<T>> objects);
	
	abstract boolean deleteAllObjects(String kind);
	
	abstract boolean addChild(WrapperObject<?> child);
}
