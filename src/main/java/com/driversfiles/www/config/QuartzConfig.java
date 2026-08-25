package com.driversfiles.www.config;

import com.driversfiles.www.core.job.AccessCodeCleanupJob;
import com.driversfiles.www.core.job.DataImportJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

	@Bean
	public JobDetail dataImportJobDetail() {
		return JobBuilder.newJob(DataImportJob.class)
				.withIdentity("dataImportJob")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger dataImportJobTrigger(JobDetail dataImportJobDetail) {
		return TriggerBuilder.newTrigger()
				.forJob(dataImportJobDetail)
				.withIdentity("dataImportJobTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 */15 * * * ?"))
				.build();
	}

	@Bean
	public JobDetail accessCodeCleanupJobDetail() {
		return JobBuilder.newJob(AccessCodeCleanupJob.class)
				.withIdentity("accessCodeCleanupJob")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger accessCodeCleanupJobTrigger(JobDetail accessCodeCleanupJobDetail) {
		return TriggerBuilder.newTrigger()
				.forJob(accessCodeCleanupJobDetail)
				.withIdentity("accessCodeCleanupJobTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 */5 * * * ?"))
				.build();
	}
}
