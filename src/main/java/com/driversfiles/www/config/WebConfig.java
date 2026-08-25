package com.driversfiles.www.config;

import jakarta.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public LayoutViewResolver layoutViewResolver(ServletContext servletContext) {
		LayoutViewResolver resolver = new LayoutViewResolver();
		resolver.initialize(servletContext);
		return resolver;
	}

	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		return new InternalResourceViewResolver();
	}

	@Bean
	public BeanPostProcessor synchronizeOnSessionPostProcessor() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof RequestMappingHandlerAdapter) {
					((RequestMappingHandlerAdapter) bean).setSynchronizeOnSession(true);
				}
				return bean;
			}
		};
	}
}
