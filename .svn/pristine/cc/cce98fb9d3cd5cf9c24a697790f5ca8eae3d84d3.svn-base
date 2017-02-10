package com.driversfiles.www.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.CompanyTruckDao;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.CompanyTruck;
import com.driversfiles.www.core.data.Truck;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("companyTruckDao")
@Transactional(readOnly=true)
public class CompanyTruckDaoImpl extends DaoImpl<CompanyTruck, Long> implements CompanyTruckDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<CompanyTruck> getEntityClass() {
		return CompanyTruck.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CompanyTruck get(Company company, Truck truck) {
		return first(findByCriteria(criteria()
				.add(Restrictions.eq("company", company))
				.add(Restrictions.eq("truck", truck))));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyTruck> get(Company company) {
		return findByCriteria(criteria().add(Restrictions.eq("company", company)));
	}
}
