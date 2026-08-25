package com.driversfiles.www.email;

/**
 * Thrown when errors occur during email processing.
 * 
 * @author Ajit Kumar
 */
public class EmailException extends Exception {

	public EmailException() {}

	public EmailException(String msg) {
		super(msg);
	}

	public EmailException(String msg, Throwable t) {
		super(msg, t);
	}
}
