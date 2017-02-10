package com.driversfiles.www.quartz;

/**
 * Provides operations to work with quartz jobs.
 *
 * @author Erik R. Jensen
 */
public interface JobService {

	/**
	 * Immediately executes a job.
	 *
	 * @param jobName the job to execute
	 * @return true if the job was triggered successfully, false if otherwise
	 */
	boolean execute(String jobName);

	/**
	 * Checks if a job is currently running.
	 *
	 * @param jobName the job to check
	 * @return true if the job is running, false if otherwise
	 */
	boolean isRunning(String jobName);
}
