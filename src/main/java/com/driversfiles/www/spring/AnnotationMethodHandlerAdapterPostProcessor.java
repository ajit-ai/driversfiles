package com.driversfiles.www.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 * Since we are using mvc:annotation driven in the spring-servlet.xml, this class provides a way
 * for us to set the synchronize on session attribute of the AnnotationMethodHandlerAdapter so
 * we can synchronize requests on the session. This helps with HttpSession thread safety.
 *
 * This class can be removed once https://jira.springsource.org/browse/SPR-7857 is fixed.
 *
 * @author Erik R. Jensen
 */
public class AnnotationMethodHandlerAdapterPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof AnnotationMethodHandlerAdapter) {
			((AnnotationMethodHandlerAdapter)bean).setSynchronizeOnSession(true);
		}
		return bean;
	}
}
