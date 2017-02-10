package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Person;

/**
 * Data access methods for Company data.
 *
 * @author Mark Burns
 * @author Erik R. Jensen
 */
public interface CompanyDao extends Dao<Company, Long> {

	/**
	 * Gets all Companies ordered by name.
	 * 
	 * @return the list of Company instances
	 */
	List<Company> getCompanies();

	/**
	 * Gets Company for a person
	 *
	 * @param person the person instance owning the company
	 * @return the Company instance or null if not found
	 */
	Company getCompany(Person person);

	/**
	 * Get a company by name
	 * 
	 * @param name the name to search for
	 * @return
	 */
	Company getByName(String name);

	/**
	 * Get a company by companyNumber
	 * 
	 * @param number valid to search for
	 * @return
	 */
	Company getByNumber(String number);
}
