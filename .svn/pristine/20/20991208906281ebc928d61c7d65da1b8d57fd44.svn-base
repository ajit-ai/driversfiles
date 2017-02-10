package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.data.Role;

import java.util.List;

/**
 * Provides access to role data.
 * 
 * @author Jagadesh Varada
 * @author Erik R. Jensen
 */
public interface RoleDao extends Dao<Role, Long> {
	
	/**
	 * Finds a role by its name.
	 *
	 * @param name the name of the role
	 * @return the role or null if not found
	 */
	Role findByName(String name);

	/**
	 * Returns all roles for a person
	 *
	 * @return list of Role instances
	 */
	List<Role> getAllRoles();
}
