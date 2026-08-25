package com.driversfiles.www.core.dao.criteria;

public final class Restrictions {

	private Restrictions() {}

	public static Criterion eq(String propertyName, Object value) {
		return new SimpleCriterion("eq", propertyName, value);
	}

	public static Criterion ne(String propertyName, Object value) {
		return new SimpleCriterion("ne", propertyName, value);
	}

	public static Criterion gt(String propertyName, Object value) {
		return new SimpleCriterion("gt", propertyName, value);
	}

	public static Criterion ge(String propertyName, Object value) {
		return new SimpleCriterion("ge", propertyName, value);
	}

	public static Criterion lt(String propertyName, Object value) {
		return new SimpleCriterion("lt", propertyName, value);
	}

	public static Criterion le(String propertyName, Object value) {
		return new SimpleCriterion("le", propertyName, value);
	}

	public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
		return new SimpleCriterion("ilike", propertyName, matchMode.toMatchString(value.toLowerCase()));
	}

	public static SimpleCriterion like(String propertyName, String value, MatchMode matchMode) {
		return new SimpleCriterion("ilike", propertyName, matchMode.toMatchString(value.toLowerCase()));
	}

	public static JunctionCriterion disjunction() {
		return new JunctionCriterion(false, null);
	}

	public static JunctionCriterion conjunction() {
		return new JunctionCriterion(true, null);
	}

	public static Criterion in(String propertyName, Object[] values) {
		return new SimpleCriterion("in", propertyName, values);
	}

	public static Criterion in(String propertyName, java.util.Collection<?> values) {
		return new SimpleCriterion("in", propertyName, values);
	}

	public static Criterion isNull(String propertyName) {
		return new SimpleCriterion("isNull", propertyName, null);
	}

	public static Criterion isNotNull(String propertyName) {
		return new SimpleCriterion("isNotNull", propertyName, null);
	}

	public static Criterion or(Criterion... criterions) {
		return new JunctionCriterion(false, criterions);
	}

	public static Criterion and(Criterion... criterions) {
		return new JunctionCriterion(true, criterions);
	}
}
