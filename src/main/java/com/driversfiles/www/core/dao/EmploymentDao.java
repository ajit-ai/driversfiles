package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Employment;

/**
 * Data access methods for employment data.
 *
 * @author Mark Burns
 */
public interface EmploymentDao extends Dao<Employment, Long> {

	/**
	 * Gets all employment records for a driver
	 * 
	 * @param driver the Driver
	 * @return the list of Employment instances
	 */
	public List<Employment> getEmployments(Driver driver);
	
}

