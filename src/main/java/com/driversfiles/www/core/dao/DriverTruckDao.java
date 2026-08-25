package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.DriverTruck;
import com.driversfiles.www.core.data.Truck;

/**
 * Data access methods for DriverTruck data.
 *
 * @author Ajit Kumar
 * @author Ajit Kumar
 */
public interface DriverTruckDao extends Dao<DriverTruck, Long> {

	/**
	 * Returns a driver truck relation.
	 *
	 * @param driver the driver
	 * @param truck the truck
	 * @return the relation or null if not found
	 */
	DriverTruck get(Driver driver, Truck truck);

	/**
	 * Returns matching driver truck relations.
	 *
	 * @param driver the driver
	 * @return the relations
	 */
	List<DriverTruck> get(Driver driver);
}
