package com.driversfiles.www.core.dao.impl;

import java.util.List;

import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.AccidentDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Accident;

/**
 * {@inheritDoc}
 *
 * @author Ajit Kumar
 */
@Service("accidentDao")
@Transactional(readOnly=true)
public class AccidentDaoImpl extends DaoImpl<Accident, Long> implements AccidentDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Accident> getEntityClass() {
		return Accident.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Accident> getAccidents(Driver driver) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("driver", driver));
		return findByCriteria(dc);
	}

}
