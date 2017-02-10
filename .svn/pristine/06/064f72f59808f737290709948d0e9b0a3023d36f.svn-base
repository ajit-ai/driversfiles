package com.driversfiles.www.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.dao.DocumentDao;
import com.driversfiles.www.core.data.Document;
import com.driversfiles.www.core.data.Person;

/**
 * {@inheritDoc}
 *
 * @author Mark Burns
 */
@Service("documentDao")
@Transactional(readOnly=true)
public class DocumentDaoImpl extends DaoImpl<Document, Long> implements DocumentDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Document> getEntityClass() {
		return Document.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocuments(Person person) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("person", person));
		return getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Document getDocument(Person person, String type) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		dc.add(Restrictions.eq("person", person));
		dc.add(Restrictions.eq("typeCode", type));
		List<Document> list = getHibernateTemplate().findByCriteria(dc);
		return (list.size() > 0 ? list.get(0) : null);
	}

}
