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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.driversfiles.www.core.dao.Auditable;

/**
 * Holds DriverTruck data.
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "driver_truck")
@SequenceGenerator(name="driverTruckIdSeq", sequenceName = "driver_truck_id_seq")
public class DriverTruck implements Serializable, Auditable, UUIDIdentified {

	private static final long serialVersionUID = -8780752493782764000L;

	public DriverTruck() {}

	public DriverTruck(Driver driver, Truck truck) {
		this.driver = driver;
		this.truck = truck;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "driverTruckIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver")
	private Driver driver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "truck")
	private Truck truck;

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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
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
