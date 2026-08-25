package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.dao.RoleDao;
import com.driversfiles.www.core.data.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.driversfiles.www.core.dao.criteria.Restrictions.eq;
import static com.driversfiles.www.core.dao.criteria.Restrictions.isNotNull;

/**
 * {@inheritDoc}
 */
@Repository("roleDao")
@Transactional
public class RoleDaoImpl extends DaoImpl<Role, Long> implements RoleDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Role> getEntityClass() {
		return Role.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Role findByName(String name) {
		return first(findByCriteria(criteria().add(eq("name", name))));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Role> getAllRoles() {
		return findByCriteria(criteria().add(isNotNull("id")));
	}

}
