package edu.uwm.owyh.interfaces;

import java.util.List;
import java.util.Map;

import edu.uwm.owyh.exceptions.BuildJDOException;

public interface NonPersistedWrapperObject<T> extends WrapperObject<T>{
	
	abstract WrapperObject<T> buildObject(String id, Map<String,Object> properties) throws BuildJDOException;
	
	abstract boolean saveAllObjects(List<WrapperObject<T>> objects);
	
	abstract boolean addChild(WrapperObject<?> child);
}
