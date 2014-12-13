package edu.uwm.owyh.library;

import java.util.Random;

public final class PasswordGenerator {

	private static final String passwordKey = "ABCEDFGHIJKLMNOPQRSTUVWXYZabcedfghijklmnopqrstuvwxyz1234567890";
	private static final int passwordKeySize = 6;

	private PasswordGenerator() {
		// prevents instantiation
	}

	/**
	 * Utility method that generate a temporary random password for user who forgot there password
	 * @return String password
	 */
	public static String generateRandomPassword() {
		String result ="";
		Random rnd = new Random();	
		for (int i = 0; i < passwordKeySize; i++) {
			int pos = rnd.nextInt(passwordKey.length());
			result += passwordKey.substring(pos, pos + 1);
		}
		return result;
	}

}
