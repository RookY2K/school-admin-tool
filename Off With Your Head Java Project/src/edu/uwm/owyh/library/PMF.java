package edu.uwm.owyh.library;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

/**
 * Singleton Persistence Mangager Factory (PMF) class
 * @author Vince Maiuri
 *
 */
public class PMF {
	private static PersistenceManager pmfInstance = null;
	
	private PMF(){
		//Ensures that a PMF is never instantiated.
	}
	
	/**
	 * @return an instantiated Persistance manager. 
	 */
	public static PersistenceManager get(){
		if(pmfInstance == null || pmfInstance.isClosed()){
			pmfInstance = JDOHelper
					.getPersistenceManagerFactory("transactions-optional")
					.getPersistenceManager();
		}
		return pmfInstance;
	}
}
