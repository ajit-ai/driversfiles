package com.driversfiles.www.core.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.ApplicationAccessDao;
import com.driversfiles.www.core.data.ApplicationAccess;
import com.driversfiles.www.core.data.Driver;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("applicationAccessDao")
@Transactional(readOnly=true)
public class ApplicationAccessDaoImpl extends DaoImpl<ApplicationAccess, Long> implements ApplicationAccessDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<ApplicationAccess> getEntityClass() {
		return ApplicationAccess.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ApplicationAccess> findByDrivers(List<Driver> drivers,
			int startIndex, int maxResult) {
		
		DetachedCriteria dc = criteria();
		dc.add(Restrictions.in("driver", drivers));

		return findByCriteria(dc, startIndex, maxResult);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ApplicationAccess> findByFilter(List<Driver> drivers,
			String viewerEmail, String viewerCompany, Date startDate,
			Date endDate, int startIndex, int maxResult) {
		
		DetachedCriteria dc = criteria();
		
		if (drivers != null && drivers.size() > 0) {
			dc.add(Restrictions.in("driver", drivers));
		}
		
		if (viewerEmail != null && !viewerEmail.isEmpty()) {
			dc.add(Restrictions.ilike("email", viewerEmail, MatchMode.ANYWHERE));
		}
		
		if (viewerCompany != null && !viewerCompany.isEmpty()) {
			dc.add(Restrictions.ilike("company", viewerCompany, MatchMode.ANYWHERE));
		}
		
		if (startDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 1);
			dc.add(Restrictions.ge("createdDate", cal.getTime()));
		}
		
		if (endDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			dc.add(Restrictions.le("createdDate", cal.getTime()));
		}

		return findByCriteria(dc, startIndex, maxResult);
	}


}
