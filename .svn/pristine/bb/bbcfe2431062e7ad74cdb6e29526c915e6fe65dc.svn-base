package com.driversfiles.www.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.LicenseDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.License;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("licenseDao")
@Transactional(readOnly=true)
public class LicenseDaoImpl extends DaoImpl<License, Long> implements LicenseDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<License> getEntityClass() {
		return License.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public License getCurrentLicense(Driver driver) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("driver", driver));
		dc.add(Restrictions.eq("current", true));
		List<License> list = getHibernateTemplate().findByCriteria(dc);
		return (list.size() > 0 ? list.get(0) : null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<License> getLicenses(Driver driver) {
		return findByCriteria(criteria().add(Restrictions.eq("driver", driver)));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<License> getAdditionalLicenses(Driver driver) {
		return findByCriteria(criteria()
				.add(Restrictions.eq("driver", driver))
				.add(Restrictions.eq("current", false)));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public License getLicense(Driver driver, String state, String number) {
		return first(findByCriteria(criteria()
				.add(Restrictions.eq("driver", driver))
				.add(Restrictions.eq("state", state))
				.add(Restrictions.eq("number", number))));
	}

}
