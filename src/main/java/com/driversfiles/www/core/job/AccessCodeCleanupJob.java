package com.driversfiles.www.core.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.service.AccessCodeService;
import com.driversfiles.www.quartz.SpringQuartzJob;

public class AccessCodeCleanupJob extends SpringQuartzJob {

	private static final Logger log = LoggerFactory.getLogger(AccessCodeCleanupJob.class);
	
	@Autowired
	@Qualifier("accessCodeService")
	private AccessCodeService accessCodeService;

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@Override
	public void doExecute(JobExecutionContext ctx) throws JobExecutionException {
		
		List<Driver> drivers = driverDao.getDriversWithExpiredAccessCode();
		log.info(String.format("Cleaning up AccessCode for %d drivers", drivers.size()));
		
		for (Driver driver : drivers) {
			accessCodeService.generateNewAccessCode(driver);
		}

	}

}
