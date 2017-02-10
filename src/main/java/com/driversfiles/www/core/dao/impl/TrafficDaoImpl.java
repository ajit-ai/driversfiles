package com.driversfiles.www.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.TrafficDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Traffic;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("trafficDao")
@Transactional(readOnly=true)
public class TrafficDaoImpl extends DaoImpl<Traffic, Long> implements TrafficDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Traffic> getEntityClass() {
		return Traffic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Traffic> getTrafficRecords(Driver driver) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("driver", driver));
		return getHibernateTemplate().findByCriteria(dc);
	}

}
