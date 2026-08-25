package com.driversfiles.www.core.dao.impl;

import java.util.List;

import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.EmploymentDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Employment;

/**
 * {@inheritDoc}
 *
 * @author Ajit Kumar
 */
@Service("employmentDao")
@Transactional(readOnly=true)
public class EmploymentDaoImpl extends DaoImpl<Employment, Long> implements EmploymentDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Employment> getEntityClass() {
		return Employment.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Employment> getEmployments(Driver driver) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("driver", driver));
		return findByCriteria(dc);
	}

}
