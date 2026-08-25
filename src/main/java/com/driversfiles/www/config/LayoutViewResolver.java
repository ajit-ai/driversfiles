package com.driversfiles.www.config;

import jakarta.servlet.ServletContext;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class LayoutViewResolver implements ViewResolver, Ordered {

	private final TilesDefinitions definitions = new TilesDefinitions();
	private int order = Ordered.LOWEST_PRECEDENCE - 10;

	public void initialize(ServletContext servletContext) {
		definitions.load(servletContext, "/WEB-INF/tiles.xml");
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) {
		if (viewName.startsWith("redirect:") || viewName.startsWith("forward:")
				|| !definitions.contains(viewName)) {
			return null;
		}
		return new LayoutView(viewName, definitions.get(viewName));
	}

	@Override
	public int getOrder() {
		return order;
	}
}
