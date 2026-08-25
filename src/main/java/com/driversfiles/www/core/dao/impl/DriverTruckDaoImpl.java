package com.driversfiles.www.core.dao.impl;

import java.util.List;

import com.driversfiles.www.core.data.Truck;
import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.DriverTruckDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.DriverTruck;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("driverTruckDao")
@Transactional(readOnly=true)
public class DriverTruckDaoImpl extends DaoImpl<DriverTruck, Long> implements DriverTruckDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<DriverTruck> getEntityClass() {
		return DriverTruck.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DriverTruck get(Driver driver, Truck truck) {
		return first(findByCriteria(criteria()
				.add(Restrictions.eq("driver", driver))
				.add(Restrictions.eq("truck", truck))));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DriverTruck> get(Driver driver) {
		return findByCriteria(criteria().add(Restrictions.eq("driver", driver)));
	}

}
