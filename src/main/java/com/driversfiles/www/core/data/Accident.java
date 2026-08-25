package com.driversfiles.www.core.data;

import com.driversfiles.www.core.dao.Auditable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Holds Accident data.
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "accident")
@SequenceGenerator(name="accidentIdSeq", sequenceName = "accident_id_seq")
public class Accident implements Serializable, UUIDIdentified, Auditable {

	private static final long serialVersionUID = -1873392188189605508L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "accidentIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver")
	private Driver driver;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "accident_date", nullable = false)
	private Date accidentDate;

	@Column(name = "type", length = 200, nullable = false)
	private String type;

	@Column(name = "nature", length = 200, nullable = false)
	private String nature;

	@Column(name = "at_fault", nullable = true)
	private Boolean atFault;

	
	@Column(name = "fatalities", nullable = true)
	private Boolean fatalities;

	@Column(name = "injuries", nullable = true)
	private Boolean injuries;

	@Column(name = "damages", nullable = true)
	private BigDecimal damages;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Person createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modified_date", nullable = false)
	private Date lastModifiedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_modified_by")
	private Person lastModifiedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public Boolean getAtFault() {
		return atFault;
	}

	public void setAtFault(Boolean atFault) {
		this.atFault = atFault;
	}

	public Boolean getFatalities() {
		return fatalities;
	}

	public void setFatalities(Boolean fatalities) {
		this.fatalities = fatalities;
	}

	public Boolean getInjuries() {
		return injuries;
	}

	public void setInjuries(Boolean injuries) {
		this.injuries = injuries;
	}

	public BigDecimal getDamages() {
		return damages;
	}

	public void setDamages(BigDecimal damages) {
		this.damages = damages;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Person getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(Person createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public Person getLastModifiedBy() {
		return lastModifiedBy;
	}

	@Override
	public void setLastModifiedBy(Person lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
