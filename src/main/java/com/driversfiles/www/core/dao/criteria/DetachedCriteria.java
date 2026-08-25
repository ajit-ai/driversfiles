package com.driversfiles.www.core.dao.criteria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DetachedCriteria {

	private final Class<?> entityClass;
	private final List<Criterion> criterions = new ArrayList<Criterion>();
	private final List<Order> orders = new ArrayList<Order>();
	private final Map<String, String> aliasPaths = new LinkedHashMap<String, String>();
	private boolean countProjection = false;
	private Integer maxResults;

	protected DetachedCriteria(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public static DetachedCriteria forClass(Class<?> entityClass) {
		return new DetachedCriteria(entityClass);
	}

	public DetachedCriteria add(Criterion criterion) {
		criterions.add(criterion);
		return this;
	}

	public DetachedCriteria addOrder(Order order) {
		orders.add(order);
		return this;
	}

	public DetachedCriteria createAlias(String path, String alias) {
		aliasPaths.put(alias, path);
		return this;
	}

	public DetachedCriteria setProjection(Projection projection) {
		countProjection = projection == Projection.COUNT;
		return this;
	}

	public DetachedCriteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public Class<?> getEntityClass() { return entityClass; }
	public List<Criterion> getCriterions() { return criterions; }
	public List<Order> getOrders() { return orders; }
	public Map<String, String> getAliasPaths() { return aliasPaths; }
	public boolean isCountProjection() { return countProjection; }
	public Integer getMaxResults() { return maxResults; }
}
