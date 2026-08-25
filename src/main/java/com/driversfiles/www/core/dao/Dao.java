package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.NotFoundException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Common contract for all data access objects.
 *
 * @author Ajit Kumar
 */
public interface Dao<T extends Serializable, P extends Serializable> {

	/**
	 * Returns the class of the entity this object facilitates data access to.
	 *
	 * @return the entity class
	 */
	Class<T> getEntityClass();

	/**
	 * Saves or updates the given object.
	 *
	 * @param obj the object to save or update
	 * @return the saved or updated object
	 */
	T saveOrUpdate(T obj);

	/**
	 * Saves the given object.
	 *
	 * @param obj the object to save
	 * @return the saved object
	 */
	T save(T obj);

	/**
	 * Updates the given object.
	 *
	 * @param obj the object to update
	 * @return the updated object
	 */
	T update(T obj);

	/**
	 * Saves or updates a batch of objects.
	 *
	 * @param objs the objects to save or update
	 */
	void saveOrUpdateBatch(Collection<T> objs);

	/**
	 * Saves or updates a batch of objects.
	 *
	 * @param objs the objects to save or update
	 * @param batchSize the batch size
	 */
	void saveOrUpdateBatch(Collection<T> objs, int batchSize);

	/**
	 * Saves a batch of objects.
	 *
	 * @param objs the objects to save or update
	 */
	void saveBatch(Collection<T> objs);

	/**
	 * Saves a batch of objects.
	 *
	 * @param objs the objects to save or update
	 * @param batchSize the batch size
	 */
	void saveBatch(Collection<T> objs, int batchSize);

	/**
	 * Updates a batch of objects.
	 *
	 * @param objs the objects to update
	 */
	void updateBatch(Collection<T> objs);

	/**
	 * Updates a batch of objects.
	 *
	 * @param objs the objects to update
	 * @param batchSize the batch size
	 */
	void updateBatch(Collection<T> objs, int batchSize);

	/**
	 * Deletes the given object.
	 *
	 * @param obj the object to delete
	 */
	void delete(T obj);

	/**
	 * Finds an instance given its identifier.
	 *
	 * @param pk the primary key of the object
	 * @return the object
	 * @throws NotFoundException if the instance could not be found
	 */
	T find(P pk);

	/**
	 * Finds an instance given its identifier.
	 *
	 * @param pk the primary key of the object
	 * @return the object or null if not found
	 */
	T get(P pk);

	/**
	 * Finds all instances.
	 *
	 * @param orderBy the field to order by
	 * @param ascending true to return the results in ascending order, false to return the results in descending order
	 * @return the list of instances
	 */
	List<T> find(String orderBy, boolean ascending);

	/**
	 * Finds all instances.
	 * 
	 * @param orderBy the field to order by
	 * @param ascending true to return the results in ascending order, false to return the results in descending order
	 * @param firstResult the index of the firstResult to be returned (the offset)
	 * @param maxResults the maximum number of results to return (the limit)
	 * @return the list of instances
	 */
	List<T> find(String orderBy, boolean ascending, int firstResult, int maxResults);

	/**
	 * Finds an instance given its UUID.
	 *
	 * @param uuid the UUID of the instance
	 * @return the found instance
	 * @throws NotFoundException if the instance could not be found
	 * @throws IllegalArgumentException if the instance of not of type UUIDIdentified
	 */
	T findByUuid(UUID uuid);

	/**
	 * Finds an instance given its UUID.
	 *
	 * @param uuid the UUID of the instance
	 * @return the found instance
	 * @throws NotFoundException if the instance could not be found
	 * @throws IllegalArgumentException if the instance of not of type UUIDIdentified
	 */
	T findByUuid(String uuid);

	/**
	 * Finds an instance by its UUID.
	 *
	 * @param uuid the UUID of the instance
	 * @return the found instance, or null if not found
	 * @throws IllegalArgumentException if the instance of not of type UUIDIdentified
	 */
	T getByUuid(UUID uuid);

	/**
	 * Finds an instance by its UUID.
	 *
	 * @param uuid the UUID of the instance
	 * @return the found instance, or null if not found
	 * @throws IllegalArgumentException if the instance of not of type UUIDIdentified
	 */
	T getByUuid(String uuid);

	/**
	 * Loads an instance given its identifier.
	 *
	 * @param pk the primary key of the object
	 * @return the object or null if not found
	 */
	T load(P pk);

	/**
	 * Re-reads the state of the given persistance instance.
	 *
	 * @param obj the object to refresh
	 * @return the refreshed object
	 */
	T refresh(T obj);

	/**
	 * Copies the state of the given object onto the persistent object with the same identifier.
	 *
	 * @param obj the object to merge
	 * @return the merged object
	 */
	T merge(T obj);

	/**
	 * Flushes all pending saves, updates and deleted to the database.
	 */
	void flush();

	/**
	 * Removes all objects from the Session cache and cancels all pending saves, updates and deletes.
	 */
	void clear();

	/**
	 * Evicts an object from the hibernate session.
	 *
	 * @param obj the object to evict
	 * @return the evicted object
	 */
	T evict(T obj);

	/**
	 * Evicts a list of objects from the hiberate session.
	 *
	 * @param lst the list of objects to evict
	 * @return the evicted objects
	 */
	List<T> evict(List<T> lst);
}

