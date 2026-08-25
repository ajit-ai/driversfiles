package com.driversfiles.www.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

	@Bean(name = "batchSize")
	public Integer batchSize(@Value("${app.db.batch-size:1000}") Integer batchSize) {
		return batchSize;
	}

	@Bean(name = "externalResourcePath")
	public String externalResourcePath(@Value("${app.external-file-store:/opt/driversfiles/}") String path) {
		return path;
	}

	@Bean(name = "mailRecipient")
	public String mailRecipient(@Value("${spring.mail.recipient:test@driversfiles.com}") String recipient) {
		return recipient;
	}

	@Bean(name = "mailFrom")
	public String mailFrom(@Value("${spring.mail.from:noreply@driversfiles.com}") String from) {
		return from;
	}
}
