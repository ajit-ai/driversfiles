package com.driversfiles.www.template;

/**
 * Throw when errors occur during template processing.
 * 
 * @author Ajit Kumar
 * @author Ajit Kumar
 */
public class TemplateException extends Exception {

	public TemplateException() {}

	public TemplateException(String msg) {
		super(msg);
	}

	public TemplateException(String msg, Throwable t) {
		super(msg, t);
	}
}
