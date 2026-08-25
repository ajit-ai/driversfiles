package com.driversfiles.www.core.dao.impl;

import java.util.Calendar;
import java.util.List;

import com.driversfiles.www.core.dao.criteria.Criterion;
import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.MatchMode;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;

/**
 * {@inheritDoc}
 *
 * @author Ajit Kumar
 * @author Ajit Kumar
 */
@Service("driverDao")
@Transactional(readOnly=true)
public class DriverDaoImpl extends DaoImpl<Driver, Long> implements DriverDao {

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Driver> getEntityClass() {
		return Driver.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Driver getDriver(Person person) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("person", person));
		List<Driver> list = findByCriteria(dc);
		return (list.size() > 0 ? list.get(0) : null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Driver> getDriversFilteredByEffectiveUser(String firstName, String lastName, String email) {
		DetachedCriteria dc = criteria();
		dc.createAlias("person", "p");
		if (firstName != null && !firstName.isEmpty()) {
			dc.add(Restrictions.ilike("p.firstName", firstName, MatchMode.ANYWHERE));
		}
		if (lastName != null && !lastName.isEmpty()) {
			dc.add(Restrictions.ilike("p.lastName", lastName, MatchMode.ANYWHERE));
		}
		if (email != null && !email.isEmpty()) {
			dc.add(Restrictions.ilike("p.email", email, MatchMode.ANYWHERE));
		}
		Person p = authService.getEffectiveUser();
		if (p != null) {
			if (p.getType() == PersonType.COMPANY) {
				dc.createAlias("companyDrivers", "cd").add(Restrictions.eq("cd.company", p.getCompany()));
			}
		}
		return findByCriteria(dc);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Driver getDriverByAccessCode(String code) {
		Calendar validDate = Calendar.getInstance();
		validDate.add(Calendar.DAY_OF_YEAR, -30);
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("accessCode", code.toUpperCase()));
		dc.add(Restrictions.gt("accessCodeCreatedDate", validDate.getTime()));
		List<Driver> list = findByCriteria(dc);
		return (list.size() > 0 ? list.get(0) : null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Driver> getDriversWithExpiredAccessCode() {
		Calendar validDate = Calendar.getInstance();
		validDate.add(Calendar.DAY_OF_YEAR, -30);
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		Criterion or1 = Restrictions.le("accessCodeCreatedDate", validDate.getTime());
		Criterion or2 = Restrictions.isNull("accessCodeCreatedDate");
		dc.add(Restrictions.or(or1,  or2));
		List<Driver> list = findByCriteria(dc);
		return list;
	}

}
