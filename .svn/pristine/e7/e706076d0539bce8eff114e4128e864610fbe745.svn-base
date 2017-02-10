package com.driversfiles.www.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service("templateService")
public class FreemarkerTemplateServiceImpl implements TemplateService {

	private static final Logger log = LoggerFactory.getLogger(TemplateService.class);
	@Autowired private FreeMarkerConfig config;

	@Override
	public String process(String templateName, Locale locale, Map<String, Object> arguments) throws TemplateException {
		try {
			StringWriter writer = new StringWriter();
			config.getConfiguration().getTemplate(templateName + ".ftl", locale).process(arguments, writer);
			return writer.toString();
		} catch (FileNotFoundException x) {
			log.error("Template [" + templateName + "] not found.", x);
			throw new TemplateException("Template [" + templateName + "] not found.", x);
		} catch (IOException x) {
			log.error("Error processing template [" + templateName + "]: " + x.getMessage(), x);
			throw new TemplateException("Error processing template [" + templateName + "]: " + x.getMessage(), x);
		} catch (freemarker.template.TemplateException x) {
			log.error("Template [" + templateName + "] not found.");
			log.error("Error processing template [" + templateName + "]: " + x.getMessage(), x);
			throw new TemplateException("Error processing template [" + templateName + "]: " + x.getMessage(), x);
		}
	}
}
