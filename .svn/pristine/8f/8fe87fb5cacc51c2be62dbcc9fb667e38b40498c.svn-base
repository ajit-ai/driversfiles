package com.driversfiles.www.quartz;

import com.driversfiles.www.spring.SpringUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/** Base class for Spring aware Quartz jobs. 
 *
 * @author Erik R. Jensen
 */
public abstract class SpringQuartzJob implements StatefulJob {
	
	@Override
	public final void execute(JobExecutionContext ctx) throws JobExecutionException {
		SpringUtil.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
		doExecute(ctx);
	}

	public abstract void doExecute(JobExecutionContext ctx) throws JobExecutionException;
}
