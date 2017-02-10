package com.driversfiles.www.core.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.data.AccessCode;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.service.AccessCodeService;

/**
 * {@inheritDoc}
 */
@Service("accessCodeService")
public class AccessCodeServiceImpl implements AccessCodeService {

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateNewAccessCode(Driver driver) {

		String accessCode = null;
		int attempts = 0;
		do {
			accessCode = AccessCode.generateCode();
			if (driverDao.getDriverByAccessCode(accessCode) != null)
				accessCode = null;
			attempts++;
		} while (accessCode == null && attempts <= 10);
		
		if (accessCode != null) {
			driver.setAccessCode(accessCode);
			driver.setAccessCodeCreatedDate(new Date());
		} else {
			driver.setAccessCode(null);
			driver.setAccessCodeCreatedDate(null);
		}
		driverDao.update(driver);
	}

}
