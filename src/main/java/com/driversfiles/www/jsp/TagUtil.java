package com.driversfiles.www.jsp;

import com.driversfiles.www.core.dao.ContentNodeDao;
import com.driversfiles.www.core.data.ContentNode;
import com.driversfiles.www.core.data.Country;
import com.driversfiles.www.core.data.Month;
import com.driversfiles.www.core.data.State;
import com.driversfiles.www.spring.SpringUtil;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Calendar;

/**
 * JSP tag library for utility functions.
 * 
 * @author Ajit Kumar
 */
public class TagUtil {

	private static ContentNodeDao contentNodeDao;

	private static ContentNodeDao getContentNodeDao() {
		if (contentNodeDao == null) {
			contentNodeDao = (ContentNodeDao)SpringUtil.getApplicationContext().getBean("contentNodeDao");
		}
		return contentNodeDao;
	}

	private TagUtil() { /* do not instantiate */ }

	public static State[] getStates() {
		return State.values();
	}

	public static Country[] getCountries() {
		return Country.values();
	}

	public static Month[] getMonths() {
		return Month.values();
	}

	public static int[] getYears() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int[] years = new int[year - 1980 + 1];
		for (int i = 0; i < years.length; i++) {
			years[i] = year - i;
		}
		return years;
	}

	@Deprecated
	public static int[] getPageLimits() {
		return new int[] {5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100};
	}

	/**
	 * Escapes a string for Javascript.
	 *
	 * @param str the string to escape
	 * @return the escaped string, or null if provided string is null
	 */
	public static String escapeJavascript(String str) {
		return StringEscapeUtils.escapeJavaScript(str);
	}

	/**
	 * Returns content from a content node.
	 *
	 * @param name the name of the content node
	 * @return the content to display
	 */
	public static String getContent(String name) {
		ContentNode cn = getContentNodeDao().getByName(name);
		return cn != null ? cn.getContent() : "<div class=\"error\">Content node not found!</div>";
	}
}
