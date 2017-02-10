package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.data.Event;
import com.driversfiles.www.core.data.EventType;
import com.driversfiles.www.core.data.Person;

import java.util.List;

/**
 * Handles event data access.
 * 
 * @author Erik R. Jensen
 * @author Abhinav Nahar
 */
public interface EventDao extends Dao<Event, Long>{

	/**
	 * Logs an event.
	 *
	 * @param type the type of event
	 * @param message the event message
	 */
	void log(EventType type, String message);

	/**
	 * Logs an event.
	 *
	 * @param type the type of event
	 * @param message the event message
	 * @param ip the ip address
	 */
	void log(EventType type, String message, String ip);

	/**
	 * Logs an event.
	 *
	 * @param type the type of event
	 * @param message the event message
	 * @param person the person being acted upon
	 */
	void log(EventType type, String message, Person person);

	/**
	 * Logs an event.
	 *
	 * @param type the type of event
	 * @param message the event message
	 * @param subject the person acting on Object
	 * @param ip the ip address
	 */
	void log(EventType type, String message, Person subject, String ip);

	/**
	 * Return the list of events
	 * 
	 * @param type the type of report
	 * @param eventCount the number of event to be returnedr
	 * @return the list of events
	 */
	List<Event> getEvents(final EventType type, final int eventCount);

	/**
	 * Deletes events older than 12 months
	 */
	void purgeEvents();
}
