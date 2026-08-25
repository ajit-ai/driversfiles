package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.data.DataImport;

import java.util.Date;
import java.util.List;

/**
 * Handles data import data access.
 *
 * @author Ajit Kumar
 */
public interface DataImportDao extends Dao<DataImport, Long> {

	DataImport getImportToProcess();

	void update(Long id, Date startTime);

	void update(Long id, String log);

	void update(Long id, Date endTime, boolean success);
}
