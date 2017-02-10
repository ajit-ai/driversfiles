package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.CompanyDriver;
import com.driversfiles.www.core.data.Driver;

/**
 * Data access methods for CompanyDriver data.
 *
 * @author Mark Burns
 * @author Erik R. Jensen
 */
public interface CompanyDriverDao extends Dao<CompanyDriver, Long> {

	/**
	 * Returns the matching driver company relation.
	 *
	 * @param company the company
	 * @param driver the driver
	 * @return the relation or null
	 */
	CompanyDriver get(Company company, Driver driver);

	/**
	 * Returns the matching dirver company relation.
	 *
	 * @param company the company
	 * @param driverNumber the driver number
	 * @return the relation or null
	 */
	CompanyDriver get(Company company, String driverNumber);

	/**
	 * Returns matching company driver relations.
	 *
	 * @param company the company
	 * @return the company driver relation
	 */
	List<CompanyDriver> get(Company company);
}
