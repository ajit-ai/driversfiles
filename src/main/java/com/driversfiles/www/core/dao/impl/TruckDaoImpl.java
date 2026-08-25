package com.driversfiles.www.core.dao.impl;

import java.util.List;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.MatchMode;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.TruckDao;
import com.driversfiles.www.core.data.Truck;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("truckDao")
@Transactional(readOnly=true)
public class TruckDaoImpl extends DaoImpl<Truck, Long> implements TruckDao {

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Truck> getEntityClass() {
		return Truck.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Truck> getTrucksFilteredByEffectiveUser(String vin, Integer year, String make, String model, Boolean active) {
		DetachedCriteria dc = criteria();
		if (vin != null && !vin.isEmpty()) {
			dc.add(Restrictions.ilike("vin", vin, MatchMode.ANYWHERE));
		}
		if (year != null && year > 100) {
			dc.add(Restrictions.eq("year", year));
		}
		if (make != null && !make.isEmpty()) {
			dc.add(Restrictions.ilike("make", make, MatchMode.ANYWHERE));
		}
		if (model != null && !model.isEmpty()) {
			dc.add(Restrictions.ilike("model", model, MatchMode.ANYWHERE));
		}
		if (active != null) {
			dc.add(Restrictions.eq("active", active));
		}
		Person p = authService.getEffectiveUser();
		if (p != null) {
			if (p.getType() == PersonType.COMPANY) {
				dc.createAlias("companyTrucks", "ct").add(Restrictions.eq("ct.company", p.getCompany()));
			}
			if (p.getType() == PersonType.DRIVER) {
				dc.createAlias("driverTrucks", "dt").add(Restrictions.eq("dt.driver", p.getDriver()));
			}
		}
		return findByCriteria(dc);
	}

	@Override
	public Truck getByTruckNumber(Company company, String truckNumber) {
		return first(findByCriteria(criteria().createAlias("companyTrucks", "ct")
				.add(Restrictions.eq("ct.company", company))
				.add(Restrictions.eq("ct.truckNumber", truckNumber))));
	}

	@Override
	public List<Truck> getTrucks(Person person, Boolean active) {
		
		DetachedCriteria dc = criteria();
		if (active != null) {
			dc.add(Restrictions.eq("active", active));
		}
		
		if (person != null) {
			if (person.getType() == PersonType.COMPANY) {
				dc.createAlias("companyTrucks", "ct").add(Restrictions.eq("ct.company", person.getCompany()));
			}
			if (person.getType() == PersonType.DRIVER) {
				dc.createAlias("driverTrucks", "dt").add(Restrictions.eq("dt.driver", person.getDriver()));
			}
		}
		return findByCriteria(dc);
	}
}
