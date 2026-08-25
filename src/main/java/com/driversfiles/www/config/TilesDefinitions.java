package com.driversfiles.www.config;

import jakarta.servlet.ServletContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads WEB-INF/tiles.xml and flattens definition inheritance into
 * attribute maps so layouts can be rendered without Apache Tiles.
 */
public class TilesDefinitions {

	public static final String TEMPLATE_KEY = "__template";

	private final Map<String, Map<String, String>> definitions = new HashMap<>();

	public void load(ServletContext servletContext, String location) {
		load(servletContext != null ? servletContext.getResourceAsStream(location) : null, location);
	}

	public void load(java.io.InputStream in, String location) {
		try {
			if (in == null) {
				throw new IllegalStateException("Unable to locate " + location);
			}
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			NodeList defs = doc.getElementsByTagName("definition");
			for (int i = 0; i < defs.getLength(); i++) {
				Element def = (Element) defs.item(i);
				String name = def.getAttribute("name");
				Map<String, String> raw = new LinkedHashMap<>();
				if (def.hasAttribute("template")) {
					raw.put(TEMPLATE_KEY, def.getAttribute("template"));
				}
				String ext = def.getAttribute("extends");
				if (ext != null && !ext.isEmpty()) {
					raw.put("extends", ext);
				}
				NodeList attrs = def.getElementsByTagName("put-attribute");
				for (int j = 0; j < attrs.getLength(); j++) {
					Element attr = (Element) attrs.item(j);
					raw.put(attr.getAttribute("name"), attr.getAttribute("value"));
				}
				definitions.put(name, resolve(raw));
			}
		} catch (Exception x) {
			throw new IllegalStateException("Failed to parse " + location + ": " + x.getMessage(), x);
		}
	}

	private Map<String, String> resolve(Map<String, String> raw) {
		Map<String, String> merged = new LinkedHashMap<>();
		String parentName = raw.get("extends");
		if (parentName != null && definitions.containsKey(parentName)) {
			merged.putAll(definitions.get(parentName));
		}
		for (Map.Entry<String, String> e : raw.entrySet()) {
			if (!"extends".equals(e.getKey())) {
				merged.put(e.getKey(), e.getValue());
			}
		}
		return merged;
	}

	public boolean contains(String name) {
		return definitions.containsKey(name);
	}

	public Map<String, String> get(String name) {
		return definitions.get(name);
	}
}
