package com.driversfiles.www.core.data;

/**
 * @author Erik R. Jensen 
 */
public enum Country {

	UNITED_STATES("United States", "US");

	private String name;
	private String code;

	private Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return this.code;
	}

	public static boolean isValid(String code) {
		for (Country c: values()) {
			if (c.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static Country getCountry(String code) {
		for (Country c: values()) {
			if (c.getCode().equals(code)) {
				return c;
			}
		}
		return null;
	}
}
