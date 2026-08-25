package com.driversfiles.www.core.data;

import java.io.Serializable;
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

import com.driversfiles.www.core.dao.Auditable;

/**
 * Holds CompanyDriver data.
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "company_driver")
@SequenceGenerator(name="companyDriverIdSeq", sequenceName = "company_driver_id_seq")
public class CompanyDriver implements Serializable, Auditable, UUIDIdentified {

	private static final long serialVersionUID = 4601533289067400685L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "companyDriverIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company")
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver")
	private Driver driver;

	@Column(name = "driver_number", length = 100, nullable = false)
	private String driverNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "hire_date")
	private Date hireDate;
	
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

	public CompanyDriver() {}

	public CompanyDriver(Company company, Driver driver) {
		this.company = company;
		this.driver = driver;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getDriverNumber() {
		return driverNumber;
	}

	public void setDriverNumber(String driverNumber) {
		if (driverNumber != null) {
			driverNumber = driverNumber.toUpperCase();
		}
		this.driverNumber = driverNumber;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Person getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Person createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Person getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Person lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
