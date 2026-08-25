package com.driversfiles.www.core.dao.impl;

import com.driversfiles.www.core.dao.PropertyDao;
import com.driversfiles.www.core.data.Property;
import com.driversfiles.www.core.data.PropertyName;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.driversfiles.www.core.dao.criteria.Restrictions.eq;

@Repository("propertyDao")
@Transactional
public class PropertyDaoImpl extends DaoImpl<Property, PropertyName> implements PropertyDao {

	@Override
	public Class<Property> getEntityClass() {
		return Property.class;
	}

	@Override
	public boolean update(PropertyName name, String value) {
		return bulkUpdate("update Property p set value = ? where name = ?", value, name) > 0;
	}

	@Override
	public boolean update(PropertyName name, Boolean value) {
		return update(name, value == null ? null : value.toString());
	}

	@Override
	public boolean update(PropertyName name, Integer value) {
		return update(name, value == null ? null : value.toString());
	}

	@Override
	public boolean update(PropertyName name, Float value) {
		return update(name, value == null ? null : value.toString());
	}

	@Override
	public String getValue(PropertyName name) {
		Property p = first(findByCriteria(criteria().add(eq("name", name))));
		return p != null ? p.getValue() : null;
	}

	@Override
	public Boolean getValueAsBoolean(PropertyName name) {
		Property p = first(findByCriteria(criteria().add(eq("name", name))));
		return p != null ? p.getValueAsBoolean() : null;
	}

	@Override
	public Integer getValueAsInt(PropertyName name) {
		Property p = first(findByCriteria(criteria().add(eq("name", name))));
		return p != null ? p.getValueAsInt() : null;
	}

	@Override
	public Float getValueAsFloat(PropertyName name) {
		Property p = first(findByCriteria(criteria().add(eq("name", name))));
		return p != null ? p.getValueAsFloat() : null;
	}
}
