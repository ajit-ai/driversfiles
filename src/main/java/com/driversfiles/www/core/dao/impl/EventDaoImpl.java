package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.dao.EventDao;
import com.driversfiles.www.core.data.Event;
import com.driversfiles.www.core.data.EventType;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.dao.criteria.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.driversfiles.www.core.dao.criteria.Restrictions.eq;

/**
 * {@inheritDoc}
 */
@Repository("eventDao")
@Transactional
public class EventDaoImpl extends DaoImpl<Event, Long> implements EventDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Event> getEntityClass() {
		return Event.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(EventType type, String message) {
		Event event = createEvent(type, message);
		save(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(EventType type, String message, String ip){
		Event event = createEvent(type, message);
		event.setIp(ip);
		save(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(EventType type, String message, Person person) {
		Event event = createEvent(type, message);
		event.setPerson(person);
		save(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(EventType type, String message, Person subject, String ip) {
		Event event = createEvent(type, message);
		event.setPerson(subject);
		event.setIp(ip);
		save(event);
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getEvents(final EventType type, final int eventCount) {
		return findByCriteria(criteria().add(eq("type", type))
				.addOrder(Order.desc("eventDate")),0, eventCount);
	}

	/**
	 * {@inheritDoc}
	 */
	public void purgeEvents() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -12);
		bulkUpdate("delete Event e where e.eventDate < ?", cal.getTime());
	}

	private Event createEvent(EventType type, String message) {
		return new Event(type, new Date(), message);
	}
}
