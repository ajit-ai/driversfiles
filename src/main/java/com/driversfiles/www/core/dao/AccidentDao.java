package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Accident;
import com.driversfiles.www.core.data.Driver;

/**
 * Data access methods for Accident data.
 *
 * @author Mark Burns
 */
public interface AccidentDao extends Dao<Accident, Long> {

	/**
	 * Gets all Accidents for a driver
	 * 
	 * @param driver the Driver
	 * @return the list of Accident instances
	 */
	List<Accident> getAccidents(Driver driver);

}
