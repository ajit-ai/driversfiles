package com.driversfiles.www.core.dao;

import java.util.Date;
import java.util.List;

import com.driversfiles.www.core.data.ApplicationAccess;
import com.driversfiles.www.core.data.Driver;

/**
 * Data access methods for ApplicationAccess data.
 *
 * @author Mark Burns
 */
public interface ApplicationAccessDao extends Dao<ApplicationAccess, Long> {

	/**
	 * This method combines two common finders (orderBy and maxResults)
	 * 
	 * @param orderBy the field to sort by
	 * @param ascending true if ascending order
	 * @param startIndex value of starting index
	 * @param maxResult the maximum number of results
	 * @return the list of ApplicationAccess records
	 */
	List<ApplicationAccess> find(String orderBy, boolean ascending, int startIndex, int maxResult);
	
	/**
	 * This method returns records based on Driver and includes page size
	 * 
	 * @param drivers a list of Drivers
	 * @param startIndex value of starting index
	 * @param maxResult the maximum number of results
	 * @return the list of ApplicationAccess records
	 */
	List<ApplicationAccess> findByDrivers(List<Driver> drivers, int startIndex, int maxResult);
	
	/**
	 * This method implements the search for ApplicationAccess records. If 
	 * filter params are left null they are not applied.
	 * 
	 * @param drivers a list of Drivers
	 * @param viewerEmail email of the viewer
	 * @param viewerCompany company of the viewer
	 * @param startDate filter on date viewed
	 * @param endDate filter on date viewed
	 * @param startIndex value of starting index
	 * @param maxResult the maximum number of results
	 * @return the list of ApplicationAccess records
	 */
	List<ApplicationAccess> findByFilter(List<Driver> drivers, String viewerEmail, String viewerCompany, Date startDate, Date endDate, int startIndex, int maxResult);
	
}
