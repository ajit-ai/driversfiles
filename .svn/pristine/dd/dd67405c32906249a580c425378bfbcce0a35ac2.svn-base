package com.driversfiles.www.core.dao;

import com.driversfiles.www.core.data.Property;
import com.driversfiles.www.core.data.PropertyName;

/**
 * Provides access to property data.
 */
public interface PropertyDao  extends Dao<Property, PropertyName> {

	boolean update(PropertyName name, String value);

	boolean update(PropertyName name, Boolean value);

	boolean update(PropertyName name, Integer value);

	boolean update(PropertyName name, Float value);

	String getValue(PropertyName name);

	Boolean getValueAsBoolean(PropertyName name);

	Integer getValueAsInt(PropertyName name);

	Float getValueAsFloat(PropertyName name);

}
