package com.driversfiles.www.core.dao.criteria;

public class Projections {

	private Projections() {}

	public static Projection count(String propertyName) {
		return Projection.COUNT;
	}
}
