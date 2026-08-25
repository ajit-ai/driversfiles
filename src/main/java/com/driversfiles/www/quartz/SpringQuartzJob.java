package com.driversfiles.www.quartz;

import com.driversfiles.www.spring.SpringUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * Base class for Spring aware Quartz jobs.
 *
 * @author Erik R. Jensen
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class SpringQuartzJob implements Job {

	@Override
	public final void execute(JobExecutionContext ctx) throws JobExecutionException {
		SpringUtil.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
		doExecute(ctx);
	}

	public abstract void doExecute(JobExecutionContext ctx) throws JobExecutionException;
}
