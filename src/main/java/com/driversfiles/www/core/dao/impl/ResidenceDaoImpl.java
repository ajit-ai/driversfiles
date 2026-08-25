package com.driversfiles.www.core.dao.impl;

import java.util.List;

import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.ResidenceDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Residence;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("residenceDao")
@Transactional(readOnly=true)
public class ResidenceDaoImpl extends DaoImpl<Residence, Long> implements ResidenceDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Residence> getEntityClass() {
		return Residence.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Residence> getResidences(Driver driver) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("driver", driver));
		return findByCriteria(dc);
	}

}
