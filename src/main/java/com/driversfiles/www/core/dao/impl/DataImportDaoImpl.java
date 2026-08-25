package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.dao.DataImportDao;
import com.driversfiles.www.core.data.DataImport;
import com.driversfiles.www.core.dao.criteria.Order;
import com.driversfiles.www.core.dao.criteria.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * {@inheritDoc}
 *
 * @author Erik R. Jensen
 */
@Repository("dataImportDao")
@Transactional(readOnly = true)
public class DataImportDaoImpl extends DaoImpl<DataImport, Long> implements DataImportDao {

	@Override
	public Class<DataImport> getEntityClass() {
		return DataImport.class;
	}

	@Override
	public DataImport getImportToProcess() {
		return first(findByCriteria(criteria().add(Restrictions.isNull("success")).add(Restrictions.isNull("startTime"))));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	@Transactional(readOnly = false)
	public void update(Long id, Date startTime) {
		bulkUpdate("update DataImport set startTime = ? where id = ?", startTime, id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	@Transactional(readOnly = false)
	public void update(Long id, String log) {
		bulkUpdate("update DataImport set log = ? where id = ?", log, id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	@Transactional(readOnly = false)
	public void update(Long id, Date endTime, boolean success) {
		bulkUpdate("update DataImport set success = ?, endTime = ? where id = ?", success, endTime, id);
	}
}