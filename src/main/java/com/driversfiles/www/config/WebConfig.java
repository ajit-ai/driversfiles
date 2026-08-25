package com.driversfiles.www.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> spaCustomizer() {
		return (factory) -> factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
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
