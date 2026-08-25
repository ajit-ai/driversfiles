package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Residence;

/**
 * Data access methods for residence data.
 *
 * @author Ajit Kumar
 */
public interface ResidenceDao extends Dao<Residence, Long> {

	/**
	 * Gets all Residences for a driver
	 * 
	 * @param driver the Driver
	 * @return the list of Residence instances
	 */
	List<Residence> getResidences(Driver driver);

}
