package com.driversfiles.www.template;

import java.util.Locale;
import java.util.Map;

/**
 * Service used to provide all template processing and related functionality.
 * 
 * @author Ajit Kumar
 */
public interface TemplateService {

	/**
	 * Processes a template and returns the results.
	 *
	 * @param templateName the name of the template
	 * @param locale the locale to use
	 * @param arguments the arguments to pass to the template
	 * @return the result or null if there was a problem processing the template
	 */
	public String process(String templateName, Locale locale, Map<String, Object> arguments) throws TemplateException;
}
