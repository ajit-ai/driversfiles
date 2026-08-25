package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.NotFoundException;
import com.driversfiles.www.core.dao.ContentNodeDao;
import com.driversfiles.www.core.data.ContentNode;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 *
 * @author Erik R. Jensen
 */
@Repository("contentNodeDao")
@Transactional
public class ContentNodeDaoImpl extends DaoImpl<ContentNode, Long> implements ContentNodeDao {

	@Override
	public Class<ContentNode> getEntityClass() {
		return ContentNode.class;
	}

	@Override
	public ContentNode findByName(String name) {
		ContentNode cn = getByName(name);
		if (cn != null) {
			return cn;
		}
		throw new NotFoundException();
	}

	@Override
	public ContentNode getByName(String name) {
		return first(findByCriteria(criteria().add(Restrictions.eq("name", name))));
	}
}
