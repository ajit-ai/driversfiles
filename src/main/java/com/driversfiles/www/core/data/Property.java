package com.driversfiles.www.core.data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds property data.
 */
@Entity
@Table(name = "property")
@SequenceGenerator(name = "propertyIdSeq", sequenceName = "property_id_seq")
public class Property implements Serializable {

	@Id
	@Column(name = "name", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private PropertyName name;

	@Column(name = "value", nullable = false)
	private String value;

	public Property() {
	}

	public Property(final PropertyName name, String value) {
		this.name = name;
		this.value = value;
	}

	public PropertyName getName() {
		return this.name;
	}

	public void setName(PropertyName name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueAsFormattedDate() {
		if(this.value != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a z");
			Date d = new Date(Long.parseLong(value));
				return formatter.format(d);
		}
		return null;
	}

	public Float getValueAsFloat() {
		return value != null
				? Float.parseFloat(value)
				: null;
	}

	public Integer getValueAsInt() {
		return value != null
				? Integer.parseInt(value)
				: null;
	}

	public Boolean getValueAsBoolean() {
		return value != null
				? Boolean.parseBoolean(value)
				: null;
	}
}
