package com.driversfiles.www.core.service;

import com.driversfiles.www.core.data.Driver;

/**
 * This service is used to expire and regenerate access codes for driver 
 * the application
 * 
 * @author mburns
 *
 */
public interface AccessCodeService {

	/**
	 * Generate a new unique access code
	 * 
	 * @param driver - the driver the code will be changed on
	 */
	public void generateNewAccessCode(Driver driver);
}
