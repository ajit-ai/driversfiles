package com.driversfiles.www.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Service("jobService")
public class JobServiceImpl implements JobService {

	private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Override
	public boolean execute(final String jobName) {
		try {
			schedulerFactory.getScheduler().triggerJob(jobName, Scheduler.DEFAULT_GROUP);
			return true;
		} catch (SchedulerException x) {
			log.error("Error executing job " + jobName + ": " + x.getMessage(), x);
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isRunning(final String jobName) {
		try {
			List<JobExecutionContext> jobs = schedulerFactory.getScheduler().getCurrentlyExecutingJobs();
			for (JobExecutionContext job: jobs) {
				if (job.getJobDetail().getName().equals(jobName)) {
					return true;
				}
			}
		} catch (SchedulerException x) {
			log.error("Error checking if job " + jobName + " is running: " + x.getMessage(), x);
		}
		return false;
	}
}
