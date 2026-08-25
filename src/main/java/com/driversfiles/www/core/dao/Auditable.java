package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.data.Person;

import java.util.Date;

/**
 * Contract for auditable data objects.
 *
 * @author Ajit Kumar
 */
public interface Auditable {

	Date getCreatedDate();

	void setCreatedDate(Date createdDate);

	Person getCreatedBy();

	void setCreatedBy(Person person);

	Date getLastModifiedDate();

	void setLastModifiedDate(Date lastModifiedDate);

	Person getLastModifiedBy();

	void setLastModifiedBy(Person lastModifiedBy);
}
