package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.MatchMode;
import com.driversfiles.www.core.dao.criteria.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static com.driversfiles.www.core.dao.criteria.Order.asc;
import static com.driversfiles.www.core.dao.criteria.Restrictions.*;

/**
 * {@inheritDoc}
 */
@Repository("personDao")
@Transactional
public class PersonDaoImpl extends DaoImpl<Person, Long> implements PersonDao {

	private static final Logger log = LoggerFactory.getLogger(PersonDaoImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Person> getEntityClass() {
		return Person.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Person findByEmail(String email) {
		return first(findByCriteria(criteria().add(eq("email", email))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Person> getPeople(int startIndex, int maxResult) {
		return findByCriteria(criteria().add(isNotNull("id")).addOrder(asc("firstName").ignoreCase()),
				startIndex, maxResult);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Number getPeopleCount() {
		return (Long) count(findByCriteria(criteria().setProjection(Projections.count("id"))
				.add(isNotNull("id"))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Person> getPeople(String searchValue, int startIndex, int maxResult) {
		DetachedCriteria dc = criteria()
				.add(disjunction().add(like("firstName", searchValue, MatchMode.ANYWHERE).ignoreCase())
						.add(like("lastName", searchValue, MatchMode.ANYWHERE).ignoreCase())
						.add(like("email", searchValue, MatchMode.ANYWHERE).ignoreCase())).add(isNotNull("id"))
				.addOrder(asc("firstName").ignoreCase());
		if (maxResult > 0) {
			return findByCriteria(dc);
		}
		return findByCriteria(dc, startIndex, maxResult);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Person> getPeople(String firstName, String lastName,
			String email, PersonType type, String companyName,
			String companyNumber, int startIndex, int maxResult) {
		
		DetachedCriteria dc = criteria();
		if (isNotBlank(firstName)) {
			dc.add(like("firstName", firstName, MatchMode.ANYWHERE).ignoreCase());
		}
		if (isNotBlank(lastName)) {
			dc.add(like("lastName", lastName, MatchMode.ANYWHERE).ignoreCase());
		}
		if (isNotBlank(email)) {
			dc.add(like("email", email, MatchMode.ANYWHERE).ignoreCase());
		}
		if (type != null) {
			dc.add(eq("type", type));
		}
		if (isNotBlank(companyName) || isNotBlank(companyNumber)) {
			dc.createAlias("company", "c");
			
			if (isNotBlank(companyName)) {
				dc.add(like("c.name", companyName, MatchMode.ANYWHERE).ignoreCase());
			}
			if (isNotBlank(companyNumber)) {
				dc.add(like("c.companyNumber", companyNumber, MatchMode.ANYWHERE).ignoreCase());
			}
		}
		dc.addOrder(asc("firstName").ignoreCase());
		if (maxResult > 0) {
			return findByCriteria(dc, startIndex, maxResult);
		}
		return findByCriteria(dc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getPeopleCount(String searchValue) {
		return (Long) firstElement(findByCriteria(criteria().setProjection(Projections.count("id")).add(
				disjunction().add(like("firstName", searchValue, MatchMode.ANYWHERE).ignoreCase())
						.add(like("lastName", searchValue, MatchMode.ANYWHERE).ignoreCase())
						.add(like("email", searchValue, MatchMode.ANYWHERE).ignoreCase()))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Person findByUuid(String uuid) {
		return first(findByCriteria(criteria().add(eq("uuid", uuid))));
	}
}