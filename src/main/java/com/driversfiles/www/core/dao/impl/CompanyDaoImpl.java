package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.dao.CompanyDao;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Person;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 * @author Erik R. Jensen
 */
@Service("companyDao")
@Transactional(readOnly=true)
public class CompanyDaoImpl extends DaoImpl<Company, Long> implements CompanyDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Company> getEntityClass() {
		return Company.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Company> getCompanies() {
		return findByCriteria(criteria().addOrder(Order.asc("name")));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Company getCompany(Person person) {
		return first(findByCriteria(criteria().add(Restrictions.eq("person", person))));
	}

	@Override
	public Company getByName(String name) {
		return first(findByCriteria(criteria().add(Restrictions.eq("name", name))));
	}

	@Override
	public Company getByNumber(String number) {
		return first(findByCriteria(criteria().add(Restrictions.eq("companyNumber", number))));
	}
}
