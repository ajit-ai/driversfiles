package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.Truck;

/**
 * Data access methods for Truck data.
 *
 * @author Mark Burns
 */
public interface TruckDao extends Dao<Truck, Long> {

	/**
	 * Gets a filtered list of Trucks
	 * 
	 * @param vin the VIN number
	 * @param year the the year of the truck
	 * @param make the make of the truck
	 * @param model the model of the truck
	 * @param active 
	 * @return the list of Truck instances
	 */
	List<Truck> getTrucksFilteredByEffectiveUser(String vin, Integer year, String make, String model, Boolean active);
	
	/**
	 * Gets trucks related to a person, optionally filtered by the active flag
	 * 
	 * @param person
	 * @param active
	 * @return
	 */
	List<Truck> getTrucks(Person person, Boolean active);

	/**
	 * Finds a truck by its truck number.
	 *
	 * @param company the company the truck belongs to
	 * @param truckNumber the truck's number
	 * @return the truck or null of not found
	 */
	Truck getByTruckNumber(Company company, String truckNumber);
}

