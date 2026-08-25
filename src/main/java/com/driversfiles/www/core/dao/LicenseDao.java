package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.License;

/**
 * Data access methods for license data.
 *
 * @author Ajit Kumar
 * @author Ajit Kumar
 */
public interface LicenseDao extends Dao<License, Long> {

	/**
	 * Gets the current license for a driver
	 * 
	 * @param driver the Driver
	 * @return the License instance
	 */
	License getCurrentLicense(Driver driver);

	/**
	 * Gets all licenses for a driver
	 * 
	 * @param driver the Driver
	 * @return the list of License instances
	 */
	List<License> getLicenses(Driver driver);

	/**
	 * Gets the old licenses for a driver
	 * 
	 * @param driver the Driver
	 * @return the list of License instances
	 */
	List<License> getAdditionalLicenses(Driver driver);

	/**
	 * Gets a license for a driver with a number
	 * 
	 * @param driver the Driver
	 * @param number the license number
	 * @return the License instance
	 */
	License getLicense(Driver driver, String state, String number);
}
