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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Holds records of access to driver application
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "application_access")
@SequenceGenerator(name="applicationAccessIdSeq", sequenceName = "application_access_id_seq")
public class ApplicationAccess implements Serializable {

	private static final long serialVersionUID = -1845821768784610823L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "applicationAccessIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "name", length = 100)
	private String name;
	
	@Column(name = "email", length = 100)
	private String email;
	
	@Column(name = "company", length = 100)
	private String company;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver", nullable = false)
	private Driver driver;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
