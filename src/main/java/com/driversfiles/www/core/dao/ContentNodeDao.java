package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.data.ContentNode;

/**
 * Provides methods for working with content nodes.
 *
 * @author Ajit Kumar
 */
public interface ContentNodeDao extends Dao<ContentNode, Long> {

	/**
	 * Finds a content node by name.
	 *
	 * @param name the name of the content node
	 * @return the content node
	 * @throws com.driversfiles.www.core.NotFoundException if the content node could not be found
	 */
	ContentNode findByName(String name);

	/**
	 * Gets a content node by name.
	 *
	 * @param name the name of the content node
	 * @return the content node or null if not found
	 */
	ContentNode getByName(String name);
}
