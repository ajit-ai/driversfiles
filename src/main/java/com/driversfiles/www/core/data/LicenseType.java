package com.driversfiles.www.core.data;

/**
 * Holds CDL license types.
 *
 * @author Erik R. Jensen
 */
public enum LicenseType {
	CLASSA("Class A"),
	CLASSB("Class B"),
	CLASSC("Class C"),
	CLASSD("Class D"),
	CLASSDJ("Class DJ"),
	CLASSE("Class E"),
	CLASSMJ("Class MJ");
	
	private String name;

	private LicenseType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static boolean isValid(String code) {
		for (LicenseType lt: values()) {
			if (lt.getName().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static LicenseType getLicenseType(String code) {
		for (LicenseType lt: values()) {
			if (lt.getName().equals(code)) {
				return lt;
			}
		}
		return null;
	}
}
