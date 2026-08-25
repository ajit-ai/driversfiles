package com.driversfiles.www.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ThrowableHelper {

	private ThrowableHelper() {}

	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
