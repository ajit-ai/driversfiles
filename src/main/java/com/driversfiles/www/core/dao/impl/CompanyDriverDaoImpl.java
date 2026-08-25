package com.driversfiles.www.core.dao.impl;

import java.util.List;

import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.CompanyDriverDao;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.CompanyDriver;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("companyDriverDao")
@Transactional(readOnly=true)
public class CompanyDriverDaoImpl extends DaoImpl<CompanyDriver, Long> implements CompanyDriverDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<CompanyDriver> getEntityClass() {
		return CompanyDriver.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyDriver get(Company company, Driver driver) {
		return first(findByCriteria(criteria()
				.add(Restrictions.eq("company", company))
				.add(Restrictions.eq("driver", driver))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyDriver get(Company company, String driverNumber) {
		return first(findByCriteria(criteria()
				.add(Restrictions.eq("company", company))
				.add(Restrictions.eq("driverNumber", driverNumber))));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyDriver> get(Company company) {
		return findByCriteria(criteria().add(Restrictions.eq("company", company)));
	}

}
