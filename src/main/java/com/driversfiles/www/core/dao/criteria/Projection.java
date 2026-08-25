package com.driversfiles.www.core.dao.criteria;

public enum Projection {

	COUNT;

	public static Projection count(String propertyName) {
		return COUNT;
	}
}
