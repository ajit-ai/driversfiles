package com.driversfiles.www.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service("jobService")
public class JobServiceImpl implements JobService {

	private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	private Scheduler scheduler;

	@Override
	public boolean execute(final String jobName) {
		try {
			scheduler.triggerJob(JobKey.jobKey(jobName, Scheduler.DEFAULT_GROUP));
			return true;
		} catch (SchedulerException x) {
			log.error("Error executing job " + jobName + ": " + x.getMessage(), x);
			return false;
		}
	}

	@Override
	public boolean isRunning(final String jobName) {
		try {
			for (JobExecutionContext job : scheduler.getCurrentlyExecutingJobs()) {
				if (job.getJobDetail().getKey().getName().equals(jobName)) {
					return true;
				}
			}
		} catch (SchedulerException x) {
			log.error("Error checking if job " + jobName + " is running: " + x.getMessage(), x);
		}
		return false;
	}
}
