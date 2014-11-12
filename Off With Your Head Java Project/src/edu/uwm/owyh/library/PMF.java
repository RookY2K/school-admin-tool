package edu.uwm.owyh.library;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

public class PMF {
	private static PersistenceManager pmfInstance = null;
	
	public static PersistenceManager get(){
		if(pmfInstance == null || pmfInstance.isClosed()){
			pmfInstance = JDOHelper
					.getPersistenceManagerFactory("transactions-optional")
					.getPersistenceManager();
		}
		return pmfInstance;
	}
}
