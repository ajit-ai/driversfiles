package com.driversfiles.www.core;

/**
 * Thrown when a resource is not found.
 *
 * @author Erik R. Jensen
 */
public class NotFoundException extends RuntimeException {

	public NotFoundException() {}

	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(Throwable t) {
		super(t);
	}

	public NotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
