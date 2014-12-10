/**
 * 
 */
package edu.uwm.owyh.exceptions;

import java.util.List;

/**
 * @author Vince Maiuri
 *
 */
public class BuildJDOException extends Exception {

	private static final long serialVersionUID = -159238969651195376L;

	public BuildJDOException(List<String> messages){
		String errors = "List of " + messages.size() + " errors: ";
		int count = 1;
		for(String message : messages){
			errors += count + ": " + message + "\n";
		}
		
		new BuildJDOException(errors);
	}
	
	/**
	 * 
	 */
	public BuildJDOException() {
		super();
	}

	/**
	 * @param message
	 */
	public BuildJDOException(String message) {
		super(message);
	}
}
