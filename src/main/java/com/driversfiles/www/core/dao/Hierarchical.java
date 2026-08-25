package com.driversfiles.www.core.dao;

/**
 * Contract for heirarchical types.
 * 
 * @author Ajit Kumar
 */
public interface Hierarchical<T extends Hierarchical> {

	public static final String PATH_DELIMITER = ".";

	public Long getId();

	public void setId(long id);

	public T getParent();

	public void setParent(T parent);

	public String getParentPath();

	public void setParentPath(String parentPath);
}
