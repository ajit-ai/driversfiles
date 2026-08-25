package com.driversfiles.www.core.dao.criteria;

public class Order {

	private final String propertyName;
	private final boolean ascending;

	protected Order(String propertyName, boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}

	public static Order asc(String propertyName) {
		return new Order(propertyName, true);
	}

	public static Order desc(String propertyName) {
		return new Order(propertyName, false);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isAscending() {
		return ascending;
	}

	public Order ignoreCase() {
		return this;
	}
}
