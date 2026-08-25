package com.driversfiles.www.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableHelperTest {

	@Test
	void stackTraceContainsExceptionClassAndMessage() {
		IllegalStateException x = new IllegalStateException("something broke");
		String trace = ThrowableHelper.getStackTrace(x);
		assertNotNull(trace);
		assertTrue(trace.contains("java.lang.IllegalStateException"));
		assertTrue(trace.contains("something broke"));
		assertTrue(trace.contains("at " + getClass().getName()));
	}

	@Test
	void stackTraceIncludesCauses() {
		try {
			try {
				throw new NumberFormatException("inner");
			} catch (NumberFormatException inner) {
				throw new IllegalStateException("outer", inner);
			}
		} catch (IllegalStateException outer) {
			String trace = ThrowableHelper.getStackTrace(outer);
			assertTrue(trace.contains("Caused by: java.lang.NumberFormatException"));
		}
	}
}
