package com.driversfiles.www.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.View;

import java.util.Map;

/**
 * Renders a flattened tiles-style definition by exposing the attributes as
 * request attributes and forwarding to the root template JSP.
 */
public class LayoutView implements View {

	public static final String CONTENT_TYPE = "text/html;charset=UTF-8";

	private final String viewName;
	private final Map<String, String> attributes;

	public LayoutView(String viewName, Map<String, String> attributes) {
		this.viewName = viewName;
		this.attributes = attributes;
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		for (Map.Entry<String, String> e : attributes.entrySet()) {
			request.setAttribute(e.getKey(), e.getValue());
		}
		setAlias(request, "title", "title");
		setAlias(request, "active_menu", "activeMenu");
		setAlias(request, "active_left_menu", "activeLeftMenu");
		request.setAttribute("viewName", viewName);
		String template = attributes.get(TilesDefinitions.TEMPLATE_KEY);
		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(template);
		if (dispatcher == null) {
			throw new IllegalStateException("Template not found: " + template);
		}
		dispatcher.forward(request, response);
	}

	private void setAlias(HttpServletRequest request, String name, String alias) {
		String value = attributes.get(name);
		request.setAttribute(alias, value != null ? value : "");
	}
}
