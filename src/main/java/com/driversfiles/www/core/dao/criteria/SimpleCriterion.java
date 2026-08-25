package com.driversfiles.www.core.dao.criteria;

public class SimpleCriterion implements Criterion {

	private static final long serialVersionUID = 1L;

	private final String op;
	private final String propertyName;
	private final Object value;

	SimpleCriterion(String op, String propertyName, Object value) {
		this.op = op;
		this.propertyName = propertyName;
		this.value = value;
	}

	public String getOp() { return op; }
	public String getPropertyName() { return propertyName; }
	public Object getValue() { return value; }

	public SimpleCriterion ignoreCase() {
		return this;
	}
}
