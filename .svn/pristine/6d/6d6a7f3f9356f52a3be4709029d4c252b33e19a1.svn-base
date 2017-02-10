package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.NotFoundException;
import com.driversfiles.www.core.dao.Auditable;
import com.driversfiles.www.core.dao.Dao;
import com.driversfiles.www.core.dao.Hierarchical;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.UUIDIdentified;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;

/**
 * {@inheritDoc}
 */
@Transactional(readOnly = true)
public abstract class DaoImpl<T extends Serializable, P extends Serializable> extends HibernateDaoSupport implements Dao<T, P> {

	@Autowired
	@Qualifier("batchSize")
	private Integer batchSize;

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	/**
	 * Sets the session factory to be used by this data access object.
	 *
	 * @param sessionFactory the session factory to be used
	 */
	@Autowired
	public void setSessionFactoryImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public T saveOrUpdate(T obj) {
		preSaveOrUpdate(obj);
		getHibernateTemplate().saveOrUpdate(obj);
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public T save(T obj) {
		preSaveOrUpdate(obj);
		getHibernateTemplate().save(obj);
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public T update(T obj) {
		preSaveOrUpdate(obj);
		getHibernateTemplate().update(obj);
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdateBatch(final Collection<T> objs) {
		saveOrUpdateBatch(objs, batchSize);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveBatch(final Collection<T> objs) {
		saveBatch(objs, batchSize);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateBatch(final Collection<T> objs) {
		updateBatch(objs, batchSize);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(T obj) {
		getHibernateTemplate().delete(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public T find(P pk) {
		T t = get(pk);
		if (t == null) {
			throw new NotFoundException();
		}
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(P pk) {
		return getHibernateTemplate().get(getEntityClass(), pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> find(String orderBy, boolean ascendingOrder) {
		return findByCriteria(criteria().addOrder(
				ascendingOrder ? asc(orderBy) : desc(orderBy)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> find(String orderBy, boolean ascendingOrder, int firstResult, int maxResults) {
		return findByCriteria(criteria().addOrder(
				ascendingOrder ? asc(orderBy) : desc(orderBy)), firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T findByUuid(UUID uuid) {
		return findByUuid(uuid.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T findByUuid(String uuid) {
		T t = getByUuid(uuid);
		if (t != null) {
			return t;
		}
		throw new NotFoundException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getByUuid(UUID uuid) {
		return getByUuid(uuid.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getByUuid(String uuid) {
		// TODO Check T is UUIDIdentified
		return first(findByCriteria(criteria().add(Restrictions.eq("uuid", uuid))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T load(P pk) {
		return getHibernateTemplate().load(getEntityClass(), pk);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T refresh(T obj) {
		getHibernateTemplate().refresh(obj);
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T merge(T obj) {
		getHibernateTemplate().merge(obj);
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public T evict(T obj) {
		getHibernateTemplate().evict(obj);
		return obj;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public List<T> evict(List<T> lst) {
		for (T o: lst) {
			evict(o);
		}
		return lst;
	}

	/**
	 * Updates automatic fields prior to the save or update of a data object.
	 *
	 * @param o the object to check
	 */
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

	/**
	 * Helper method to find hibernate beans by criteria.
	 * 
	 * @param criteria the criteria to search
	 * @return the results
	 */
	@SuppressWarnings("unchecked")
	protected <X> List<X> findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * Helper method to find hibernate beans by criteria.
	 * 
	 * @param criteria the criteria to search
	 * @param firstResult the index of the first result to return
	 * @param maxResults the maximum number of results to return
	 * @return the results
	 */
	@SuppressWarnings("unchecked")
	protected <X> List<X> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
	}

	/**
	 * Helper method to run an HQL query.
	 *
	 * @param query the query
	 * @param values the query arguments
	 * @return the results
	 */
	@SuppressWarnings("unchecked")
	protected <X> List<X> find(String query, Object... values) {
		return getHibernateTemplate().find(query, values);
	}

	/**
	 * Helper method to run an HQL query.
	 *
	 * @param query the query
	 * @param firstResult the index of the first result to return
	 * @param maxResults the maximum number of results to return
	 * @param values the query arguments
	 * @return the results
	 */
	@SuppressWarnings("unchecked")
	protected <X> List<X> find(String query, int firstResult, int maxResults, Object... values) {
		final Query q = getSession().createQuery(query).setFirstResult(firstResult).setMaxResults(maxResults);
		int i = 1;
		for (Object o: values) {
			q.setParameter(i++, o);
		}
		return q.list();
	}

	/**
	 * Helper method to return the first element of a list. If the list is empty, this
	 * method will return a null.
	 *
	 * @param list the list
	 * @return the first element of the list, or null if there is none
	 */
	@SuppressWarnings("unchecked")
	protected T first(List<?> list) {
		return (T)firstElement(list);
	}

	/**
	 * Helper method to return the first element of a list. If the list is empty, this
	 * method will return a null.
	 *
	 * @param list the list
	 * @return the first element of the list, or null if there is none
	 */
	@SuppressWarnings("unchecked")
	protected <X> X firstElement(List<?> list) {
		return list.isEmpty() ? null : (X)list.get(0);
	}

	/**
	 * Helper method to return the first element of a list returned from a hibernate count. If the
	 * list is empty, this method will return a null.
	 *
	 * @param list the list
	 * @return the first element of the list, or null if there is none
	 */
	@SuppressWarnings("unchecked")
	protected Number count(List<?> list) {
		return list.isEmpty() ? null : (Number)list.get(0);
	}

	/**
	 * Helper method to return a DetachedCriteria for use by this data access object.
	 *
	 * @return a DetachedCriteria
	 */
	protected DetachedCriteria criteria() {
		return DetachedCriteria.forClass(getEntityClass());
	}
}

