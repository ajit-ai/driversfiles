package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.dao.Dao;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;

import java.util.List;

/**
 * Provides access to person data.
 *
 * @author Ajit Kumar
 */
public interface PersonDao extends Dao<Person, Long> {

	/**
	 * Finds a a person by their username.
	 *
	 * @param email the email address of the person
	 * @return the person or null if not found
	 */
	Person findByEmail(String email);

	/**
	 * Returns list of person from startIndex sorted by first name.
	 *
	 * @param startIndex the start index for person list
	 * @param maxResult the maximum number of person in the person list
	 * @return the person list
	 */
	List<Person> getPeople(int startIndex, int maxResult);

	/**
	 * Returns total number of People.
	 *
	 * @return the total number of person
	 */
	Number getPeopleCount();

	/**
	 * Perform wild card search on first name, last name or email and returns list of person from startIndex
	 * sorted by first. If maxResults is less than 0, all results will be returned.
	 *
	 * @param searchValue the value to be search in first name, last name and email
	 * @param startIndex the start index for person list
	 * @param maxResult the maximum number of person in the person list
	 * @return the person list
	 */
	List<Person> getPeople(String searchValue, int startIndex, int maxResult);
	
	/**
	 * Perform wild card search on first name, last name, email, person type, company name, or company number and 
	 * returns list of person from startIndex sorted by first. If maxResults is less than 0, all results will be
	 * returned.
	 *
	 * @param firstName the value to be search in first name
	 * @param lastName the value to be search in last name
	 * @param email the value to be search in email
	 * @param type the value to be search in type
	 * @param companyName the value to be search in company name
	 * @param companyNumber the value to be search in company number
	 * @param startIndex the start index for person list
	 * @param maxResult the maximum number of person in the person list
	 * @return the person list
	 */
	List<Person> getPeople(String firstName, String lastName,
			String email, PersonType type, String companyName,
			String companyNumber, int startIndex, int maxResult);

	/**
	 * Perform wild card search on first name, last name or email and returns total number of person.
	 *
	 * @param searchValue the value to be search in first name, last name and email
	 * @return the total number of person
	 */
	long getPeopleCount(String searchValue);

	/**
	 * Returns the person having the uuid as provided
	 *
	 * @param uuid the universally unique identified .
	 * @return the person
	 */
	Person findByUuid(String uuid);
}
