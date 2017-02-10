package com.driversfiles.www.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring related utility methods.
 * 
 * @author Erik R. Jensen
 */
@Component
public class SpringUtil implements ApplicationContextAware, BeanFactoryAware {

	private static ApplicationContext ctx;
	private static BeanFactory bf;

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static BeanFactory getBeanFactory() {
		return bf;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		bf = beanFactory;
	}
}
