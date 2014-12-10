package edu.uwm.owyh.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class Email {

	private static Properties _prop;
	private static Session _session;
	private static final String senderEmail = "ryoko.dragon.fly@gmail.com";
	private static final String senderName = "Off With Your Head Admin";
	
	public static final String adminEmail = "xiong225@uwm.edu";
	
	/**
	 * Send an Email
	 * @param recipientEmail the email address that this email will be sent
	 * @param recipientName the name  of the person who will receive this email
	 * @param subject of the email to be sent
	 * @param message email to be sent
	 * @return list of errors
	 */
	public static List<String> sendMessage(String recipientEmail, String recipientName, String subject, String message) {
		setup();
		List<String> errors = new ArrayList<String>();
		try {
			Message msg = new MimeMessage(_session);
			msg.setFrom(new InternetAddress(senderEmail, senderName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail, recipientName));
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
			} catch (AddressException e) {
				errors.add("Error with address format when trying to send an email.");
			} catch (MessagingException e) {
				errors.add("Error with message format when trying to send an email.");
			} catch (Exception e) {
				errors.add("Error occured when trying to send an email.");
			}
		return errors;
	}

	/**
	 * Lazy Loads Email variables properties and session
	 */
	private static void setup() {
		if (_prop == null)
			_prop = new Properties();
		if (_session == null)
			_session = Session.getDefaultInstance(_prop, null);
	}
}
