package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.CompanyTruck;
import com.driversfiles.www.core.data.Truck;

/**
 * Data access methods for CompanyTruck data.
 *
 * @author Ajit Kumar
 */
public interface CompanyTruckDao extends Dao<CompanyTruck, Long> {

	/**
	 * Find a CompanyTruck based on the Truck field
	 *
	 * @param company the company
	 * @param truck the truck
	 * @return the company truck relation or null if not found
	 */
	CompanyTruck get(Company company, Truck truck);

	/**
	 * Gets CompanyTruck from Company relationship.
	 *
	 * @param company the company
	 * @return the matching company truck relations
	 */
	List<CompanyTruck> get(Company company);

}
