package com.driversfiles.www.core.data;

import java.util.Calendar;

/**
 * Holds month information.
 */
public enum Month {

	JANUARY("January", 0),
	FEBRUARY("February", 1),
	MARCH("March", 2),
	APRIL("April", 3),
	MAY("May", 4),
	JUNE("June", 5),
	JULY("July", 6),
	AUGUST("August", 7),
	SEPTEMBER("September", 8),
	OCTOBER("October", 9),
	NOVEMBER("November", 10),
	DECEMBER("December", 11);
	private final String name;
	private final Integer value;

	private Month(final String name, final int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name;
	}

	public static boolean isValid(String name) {
		for (Month s : values()) {
			if (s.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static Month getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		int currentMonth = cal.get(Calendar.MONTH);
		return Month.values()[currentMonth];
	}

	public static Month getLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int currentMonth = cal.get(Calendar.MONTH);
		return Month.values()[currentMonth];
	}

	public static Month getMonth(int month) {
		switch (month) {
			case 0:
				return JANUARY;
			case 1:
				return FEBRUARY;
			case 2:
				return MARCH;
			case 3:
				return APRIL;
			case 4:
				return MAY;
			case 5:
				return JUNE;
			case 6:
				return JULY;
			case 7:
				return AUGUST;
			case 8:
				return SEPTEMBER;
			case 9:
				return OCTOBER;
			case 10:
				return NOVEMBER;
			case 11:
				return DECEMBER;
		}
		return null;
	}
}
