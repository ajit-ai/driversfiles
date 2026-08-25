package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.NotFoundException;
import com.driversfiles.www.core.dao.Auditable;
import com.driversfiles.www.core.dao.Dao;
import com.driversfiles.www.core.dao.Hierarchical;
import com.driversfiles.www.core.dao.criteria.Criterion;
import com.driversfiles.www.core.dao.criteria.DetachedCriteria;
import com.driversfiles.www.core.dao.criteria.JunctionCriterion;
import com.driversfiles.www.core.dao.criteria.MatchMode;
import com.driversfiles.www.core.dao.criteria.Order;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import com.driversfiles.www.core.dao.criteria.SimpleCriterion;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.UUIDIdentified;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Transactional(readOnly = true)
public abstract class DaoImpl<T extends Serializable, P extends Serializable> implements Dao<T, P> {

	@Autowired
	@Qualifier("batchSize")
	private Integer batchSize;

	@Autowired
	@Qualifier("authService")
	@org.springframework.context.annotation.Lazy
	private AuthService authService;

	@Autowired
	@Qualifier("entityManagerFactory")
	@org.springframework.context.annotation.Lazy
	private jakarta.persistence.EntityManagerFactory entityManagerFactory;

	protected org.hibernate.Session getSession() {
		return entityManagerFactory.unwrap(org.hibernate.SessionFactory.class).getCurrentSession();
	}

	@Override
	@Transactional(readOnly = false)
	public T saveOrUpdate(T obj) {
		preSaveOrUpdate(obj);
		getSession().saveOrUpdate(obj);
		return obj;
	}

	@Override
	@Transactional(readOnly = false)
	public T save(T obj) {
		preSaveOrUpdate(obj);
		getSession().save(obj);
		return obj;
	}

	@Override
	@Transactional(readOnly = false)
	public T update(T obj) {
		preSaveOrUpdate(obj);
		getSession().update(obj);
		return obj;
	}

	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdateBatch(final Collection<T> objs) {
		saveOrUpdateBatch(objs, batchSize);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdateBatch(final Collection<T> objs, final int batchSize) {
		final Iterator<T> it = objs.iterator();
		for (int i = 0; i < objs.size(); i++) {
			T obj = it.next();
			saveOrUpdate(obj);
			if (i % batchSize == 0) {
				flush();
				clear();
			}
		}
		flush();
		clear();
	}

	@Override
	@Transactional(readOnly = false)
	public void saveBatch(final Collection<T> objs) {
		saveBatch(objs, batchSize);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveBatch(final Collection<T> objs, final int batchSize) {
		final Iterator<T> it = objs.iterator();
		for (int i = 0; i >= 0 && i < objs.size(); i++) {
			T obj = it.next();
			save(obj);
			if (i % batchSize == 0) {
				flush();
				clear();
			}
		}
		flush();
		clear();
	}

	@Override
	@Transactional(readOnly = false)
	public void updateBatch(final Collection<T> objs) {
		updateBatch(objs, batchSize);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateBatch(final Collection<T> objs, final int batchSize) {
		final Iterator<T> it = objs.iterator();
		for (int i = 0; i >= 0 && i < objs.size(); i++) {
			T obj = it.next();
			update(obj);
			if (i % batchSize == 0) {
				flush();
				clear();
			}
		}
		flush();
		clear();
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(T obj) {
		getSession().delete(obj);
	}

	@Override
	public T find(P pk) {
		T t = get(pk);
		if (t == null) {
			throw new NotFoundException();
		}
		return t;
	}

	@Override
	public T get(P pk) {
		return getSession().get(getEntityClass(), pk);
	}

	@Override
	public List<T> find(String orderBy, boolean ascendingOrder) {
		return findByCriteria(criteria().addOrder(
				ascendingOrder ? Order.asc(orderBy) : Order.desc(orderBy)));
	}

	@Override
	public List<T> find(String orderBy, boolean ascendingOrder, int firstResult, int maxResults) {
		return findByCriteria(criteria().addOrder(
				ascendingOrder ? Order.asc(orderBy) : Order.desc(orderBy)), firstResult, maxResults);
	}

	@Override
	public T findByUuid(UUID uuid) {
		return findByUuid(uuid.toString());
	}

	@Override
	public T findByUuid(String uuid) {
		T t = getByUuid(uuid);
		if (t != null) {
			return t;
		}
		throw new NotFoundException();
	}

	@Override
	public T getByUuid(UUID uuid) {
		return getByUuid(uuid.toString());
	}

	@Override
	public T getByUuid(String uuid) {
		return first(findByCriteria(criteria().add(Restrictions.eq("uuid", uuid))));
	}

	@Override
	public T load(P pk) {
		return getSession().load(getEntityClass(), pk);
	}

	@Override
	public T refresh(T obj) {
		getSession().refresh(obj);
		return obj;
	}

	@Override
	public T merge(T obj) {
		return (T) getSession().merge(obj);
	}

	@Override
	@Transactional(readOnly = false)
	public void flush() {
		getSession().flush();
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public T evict(T obj) {
		getSession().evict(obj);
		return obj;
	}

	@Override
	public List<T> evict(List<T> lst) {
		for (T o : lst) {
			evict(o);
		}
		return lst;
	}

	private void preSaveOrUpdate(final T o) {
		if (o instanceof Auditable) {
			final Auditable a = (Auditable)o;
			final Person person = authService.getAuthenticatedUser();
			if (a.getCreatedDate() == null) {
				a.setCreatedDate(new Date());
			}
			if (a.getCreatedBy() == null) {
				a.setCreatedBy(person);
			}
			a.setLastModifiedDate(new Date());
			a.setLastModifiedBy(person);
		}
		if (o instanceof UUIDIdentified) {
			final UUIDIdentified u = (UUIDIdentified)o;
			if (u.getUuid() == null) {
				u.setUuid(UUID.randomUUID().toString());
			}
		}
		if (o instanceof Hierarchical) {
			final Hierarchical h = (Hierarchical)o;
			if (h.getParent() != null) {
				final Hierarchical p = h.getParent();
				final StringBuilder sb = new StringBuilder();
				if (p.getParentPath() != null) {
					sb.append(p.getParentPath()).append(Hierarchical.PATH_DELIMITER);
				}
				sb.append(p.getId()).append(Hierarchical.PATH_DELIMITER);
				h.setParentPath(sb.toString());
			} else {
				h.setParentPath(null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <X> List<X> findByCriteria(DetachedCriteria dc) {
		return (List<X>) execute(dc, -1, -1);
	}

	@SuppressWarnings("unchecked")
	protected <X> List<X> findByCriteria(DetachedCriteria dc, int firstResult, int maxResults) {
		return (List<X>) execute(dc, firstResult, maxResults);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private List<?> execute(DetachedCriteria dc, int firstResult, int maxResults) {
		CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
		boolean count = dc.isCountProjection();
		@SuppressWarnings("unchecked")
		CriteriaQuery<Object> cq = (CriteriaQuery<Object>) (count
				? cb.createQuery(Long.class) : cb.createQuery(Object.class));
		Root root = cq.from(dc.getEntityClass());

		Map<String, From<?, ?>> aliases = new HashMap<String, From<?, ?>>();
		for (Map.Entry<String, String> e : dc.getAliasPaths().entrySet()) {
			String[] parts = e.getValue().split("\\.");
			From<?, ?> f = root;
			for (int i = 1; i < parts.length; i++) {
				f = f.join(parts[i]);
			}
			aliases.put(e.getKey(), f);
		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Criterion c : dc.getCriterions()) {
			Predicate p = toPredicate(cb, root, aliases, c);
			if (p != null) {
				predicates.add(p);
			}
		}
		if (!predicates.isEmpty()) {
			cq.where(predicates.toArray(new Predicate[0]));
		}

		if (count) {
			cq.select(cb.count(root));
		} else {
			cq.select(root);
			if (!dc.getOrders().isEmpty()) {
				List<jakarta.persistence.criteria.Order> orders = new ArrayList<jakarta.persistence.criteria.Order>();
				for (Order o : dc.getOrders()) {
					Path<?> path = resolvePath(root, aliases, o.getPropertyName());
					orders.add(o.isAscending() ? cb.asc(path) : cb.desc(path));
				}
				cq.orderBy(orders);
			}
		}

		org.hibernate.query.Query<Object> query = getSession().createQuery(cq);
		int effectiveMax = dc.getMaxResults() != null && dc.getMaxResults() > 0
				? dc.getMaxResults() : maxResults;
		if (firstResult > 0) {
			query.setFirstResult(firstResult);
		}
		if (effectiveMax > 0) {
			query.setMaxResults(effectiveMax);
		}
		return query.getResultList();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Predicate toPredicate(CriteriaBuilder cb, Root<?> root,
			Map<String, From<?, ?>> aliases, Criterion c) {
		if (c instanceof SimpleCriterion) {
			SimpleCriterion sc = (SimpleCriterion) c;
			String prop = sc.getPropertyName();
			Object val = sc.getValue();
			switch (sc.getOp()) {
				case "eq": return cb.equal(resolvePath(root, aliases, prop), val);
				case "ne": return cb.notEqual(resolvePath(root, aliases, prop), val);
				case "gt": return cb.greaterThan((Expression) resolvePath(root, aliases, prop), (Comparable) val);
				case "ge": return cb.greaterThanOrEqualTo((Expression) resolvePath(root, aliases, prop), (Comparable) val);
				case "lt": return cb.lessThan((Expression) resolvePath(root, aliases, prop), (Comparable) val);
				case "le": return cb.lessThanOrEqualTo((Expression) resolvePath(root, aliases, prop), (Comparable) val);
				case "ilike":
					return cb.like(cb.lower((jakarta.persistence.criteria.Expression<String>) resolvePath(root, aliases, prop)),
							val.toString());
				case "in":
					if (val instanceof Object[]) {
						return resolvePath(root, aliases, prop).in((Object[]) val);
					} else if (val instanceof Collection) {
						Collection<?> col = (Collection<?>) val;
						if (col.isEmpty()) {
							return cb.disjunction();
						}
						return resolvePath(root, aliases, prop).in(col);
					}
					throw new IllegalArgumentException("Unsupported 'in' value type");
				case "isNull": return cb.isNull(resolvePath(root, aliases, prop));
				case "isNotNull": return cb.isNotNull(resolvePath(root, aliases, prop));
				default: throw new IllegalArgumentException("Unknown op " + sc.getOp());
			}
		} else if (c instanceof JunctionCriterion) {
			JunctionCriterion jc = (JunctionCriterion) c;
			List<Predicate> preds = new ArrayList<Predicate>();
			for (Criterion inner : jc.getCriterions()) {
				Predicate p = toPredicate(cb, root, aliases, inner);
				if (p != null) {
					preds.add(p);
				}
			}
			return jc.isConjunction() ? cb.and(preds.toArray(new Predicate[0]))
					: cb.or(preds.toArray(new Predicate[0]));
		}
		throw new IllegalArgumentException("Unknown criterion " + c.getClass());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Path<?> resolvePath(Root<?> root, Map<String, From<?, ?>> aliases, String prop) {
		int idx = prop.indexOf('.');
		if (idx < 0) {
			return root.get(prop);
		}
		String head = prop.substring(0, idx);
		String tail = prop.substring(idx + 1);
		From<?, ?> base = aliases.get(head);
		if (base == null) {
			base = root.join(head);
		}
		String[] parts = tail.split("\\.");
		Path<?> path = base.get(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			path = ((From) path).join(parts[i]);
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	protected <X> List<X> find(String query, Object... values) {
		var q = getSession().createQuery(query);
		int i = 1;
		for (Object o : values) {
			q.setParameter(i++, o);
		}
		return (List<X>) q.list();
	}

	@SuppressWarnings("unchecked")
	protected <X> List<X> find(String query, int firstResult, int maxResults, Object... values) {
		var q = getSession().createQuery(query).setFirstResult(firstResult).setMaxResults(maxResults);
		int i = 1;
		for (Object o : values) {
			q.setParameter(i++, o);
		}
		return (List<X>) q.list();
	}

	protected T first(List<?> list) {
		return (T) firstElement(list);
	}

	@Transactional(readOnly = false)
	protected int bulkUpdate(String query, Object... values) {
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (int i = 0; i < query.length(); i++) {
			char ch = query.charAt(i);
			if (ch == '?') {
				sb.append('?').append(++n);
			} else {
				sb.append(ch);
			}
		}
		org.hibernate.query.MutationQuery q = getSession().createMutationQuery(sb.toString());
		for (int i = 1; i <= values.length; i++) {
			q.setParameter(i, values[i - 1]);
		}
		return q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	protected <X> X firstElement(List<?> list) {
		return list.isEmpty() ? null : (X) list.get(0);
	}

	@SuppressWarnings("unchecked")
	protected Number count(List<?> list) {
		return list.isEmpty() ? null : (Number) list.get(0);
	}

	protected DetachedCriteria criteria() {
		return DetachedCriteria.forClass(getEntityClass());
	}
}
