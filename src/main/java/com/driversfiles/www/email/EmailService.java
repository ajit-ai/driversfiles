package com.driversfiles.www.email;

import java.util.Locale;
import java.util.Map;

/**
 * Service used to provide all email related functionality.
 * 
 * @author Ajit Kumar
 * @author Ajit Kumar
 */

public interface EmailService {

	/**
	 * Sends an email. The recipient will be the default email recipient as defined in spring.properties.
	 *
	 * @param type the type of email
	 * @param locale the locale to generate the email for
	 * @param arguments the arguments for the email template
	 */
	public void send(EmailType type, Locale locale, Map<String,Object> arguments) throws EmailException;

	/**
	 * Sends an email to the provided recipient.
	 *
	 * @param to the recipient of the email
	 * @param type the type of email
	 * @param locale the locale to generate the email for
	 * @param arguments the arguments for the email template
	 */
	public void send(String to, EmailType type, Locale locale, Map<String, Object> arguments) throws EmailException;

	/**
	 * Sends an email asynchronously in a separate thread.
	 *
	 * @param type the type of email
	 * @param locale the local to generate the email for
	 * @param arguments the arguments for the email template
	 */
	public void sendAsync(EmailType type, Locale locale, Map<String, Object> arguments);

	/**
	 * Sends and email asynchronously in a separate thread. The recipient will be the default email recipient as
	 * defined in spring.properties.
	 *
	 * @param to the recipient of the email
	 * @param type the type of the email
	 * @param locale the locale to generate the email for
	 * @param arguments the arguments for the email template
	 */
	public void sendAsync(String to, EmailType type, Locale locale, Map<String, Object> arguments);
}

