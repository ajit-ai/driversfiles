package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Person;

/**
 * Data access methods for driver data.
 *
 * @author Erik R. Jensen
 */
public interface DriverDao extends Dao<Driver, Long> {

	/**
	 * Gets Driver from Person relationship.
	 *
	 * @param person the person
	 * @return the Driver instance
	 */
	Driver getDriver(Person person);

	/**
	 * Gets List of Drivers based on effectiveUser and filter params.
	 * If a filter param is null then it is not applied. This is most
	 * useful for a "Company" user to search drivers.
	 * 
	 * @param firstName a filter on firstName
	 * @param lastName a filter on lastName
	 * @param email a filter on email
	 * @return List of Driver instances
	 */
	List<Driver> getDriversFilteredByEffectiveUser(String firstName, String lastName, String email);
	
	/**
	 * Gets the driver related to an access code. The access code must be 
	 * valid and not expired.
	 * 
	 * @param code the 6 character code
	 * @return the Driver instance or null if not found
	 */
	Driver getDriverByAccessCode(String code);
	
	/**
	 * Gets List of Drivers whose accessCode is more than 30 days old
	 * 
	 * @return Driver instances
	 */
	List<Driver> getDriversWithExpiredAccessCode();
	
}
