package com.driversfiles.www.core.dao.criteria;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CriteriaShimTest {

	@Test
	void fluentChainingReturnsSameInstance() {
		DetachedCriteria dc = DetachedCriteria.forClass(Object.class);
		assertSame(dc, dc.add(Restrictions.eq("a", 1)));
		assertSame(dc, dc.addOrder(Order.asc("b")));
		assertSame(dc, dc.createAlias("companyTrucks", "ct"));
	}

	@Test
	void criterionsOrdersAndAliasesAreCollected() {
		DetachedCriteria dc = DetachedCriteria.forClass(Object.class)
				.add(Restrictions.eq("driver", 7))
				.add(Restrictions.ilike("vin", "abc", MatchMode.ANYWHERE))
				.createAlias("companyDrivers", "cd")
				.addOrder(Order.desc("id"))
				.setMaxResults(50);

		assertEquals(2, dc.getCriterions().size());
		assertEquals(1, dc.getOrders().size());
		assertTrue(dc.getOrders().get(0).isAscending() == false);
		assertEquals("companyDrivers", dc.getAliasPaths().get("cd"));
		assertEquals(50, dc.getMaxResults());
		assertFalse(dc.isCountProjection());
	}

	@Test
	void countProjectionFlagSet() {
		DetachedCriteria dc = DetachedCriteria.forClass(Object.class)
				.setProjection(Projections.count("id"));
		assertTrue(dc.isCountProjection());
	}

	@Test
	void restrictionOpsCarryPropertyAndValue() {
		SimpleCriterion eq = (SimpleCriterion) Restrictions.eq("email", "x@y.z");
		assertEquals("eq", eq.getOp());
		assertEquals("email", eq.getPropertyName());
		assertEquals("x@y.z", eq.getValue());
	}

	@Test
	void matchModesWrapPattern() {
		assertEquals("abc", MatchMode.EXACT.toMatchString("abc"));
		assertEquals("abc%", MatchMode.START.toMatchString("abc"));
		assertEquals("%abc", MatchMode.END.toMatchString("abc"));
		assertEquals("%abc%", MatchMode.ANYWHERE.toMatchString("abc"));
	}

	@Test
	void junctionsNestCriterions() {
		JunctionCriterion disjunction = Restrictions.disjunction()
				.add(Restrictions.like("firstName", "jo", MatchMode.ANYWHERE))
				.add(Restrictions.like("lastName", "do", MatchMode.ANYWHERE));
		assertFalse(disjunction.isConjunction());
		assertEquals(2, disjunction.getCriterions().length);

		JunctionCriterion conjunction = Restrictions.conjunction();
		assertTrue(conjunction.isConjunction());
		conjunction.add(disjunction);
		conjunction.add(Restrictions.isNotNull("id"));
		assertEquals(2, conjunction.getCriterions().length);
	}

	@Test
	void orAndVarargsBuildJunctions() {
		Criterion or = Restrictions.or(Restrictions.eq("a", 1), Restrictions.eq("b", 2));
		JunctionCriterion jc = (JunctionCriterion) or;
		assertFalse(jc.isConjunction());

		Criterion and = Restrictions.and(Restrictions.eq("c", 3), Restrictions.eq("d", 4));
		assertTrue(((JunctionCriterion) and).isConjunction());
	}

	@Test
	void inSupportsArraysAndCollections() {
		SimpleCriterion arr = (SimpleCriterion) Restrictions.in("type", new Object[]{"A", "B"});
		assertEquals(2, ((Object[]) arr.getValue()).length);

		SimpleCriterion col = (SimpleCriterion) Restrictions.in("ids", Arrays.asList(1, 2, 3));
		assertEquals(3, ((java.util.Collection<?>) col.getValue()).size());

		SimpleCriterion empty = (SimpleCriterion) Restrictions.in("ids", Collections.emptyList());
		assertEquals(0, ((java.util.Collection<?>) empty.getValue()).size());
	}

	@Test
	void nullChecksUseCorrectOpNames() {
		assertEquals("isNull", ((SimpleCriterion) Restrictions.isNull("x")).getOp());
		assertEquals("isNotNull", ((SimpleCriterion) Restrictions.isNotNull("x")).getOp());
	}
}
