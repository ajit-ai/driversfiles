package com.driversfiles.www.core.data;

import com.driversfiles.www.core.dao.Auditable;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds Truck data.
 *
 * @author Ajit Kumar
 * @author Ajit Kumar
 */
@Entity
@Table(name = "truck")
@SequenceGenerator(name="truckIdSeq", sequenceName = "truck_id_seq")
public class Truck implements Serializable, Auditable, UUIDIdentified {

	private static final long serialVersionUID = 8309163787684104714L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "truckIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;

	@Column(name = "vin", length = 100, nullable = true)
	private String vin;

	@Column(name = "year", nullable = true)
	private Integer year;

	@Column(name = "make", length = 30, nullable = true)
	private String make;

	@Column(name = "model", length = 30, nullable = true)
	private String model;
	
	@Column(name = "license", length = 20, nullable = true)
	private String license;
	
	@Column(name = "license_state", length = 2, columnDefinition = "BPCHAR(2)")
	private String licenseState;

	@Temporal(TemporalType.DATE)
	@Column(name = "registration", nullable = true)
	private Date registration;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "annual_inspection", nullable = true)
	private Date annualInspection;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "bobtail_insurance", nullable = true)
	private Date bobtailInsurance;
	
	@Column(name = "ifta", nullable = true)
	private Boolean ifta;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "quarterly_maintenance", nullable = true)
	private Date quarterlyMaintenance;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "physical_damage_insurance", nullable = true)
	private Date physicalDamageInsurance;

	@Column(name = "lessor_number", length = 100, nullable = true)
	private String lessorNumber;

	@Column(name = "lessor_name", length = 100, nullable = true)
	private String lessorName;

	@Column(name = "lessor_address1", length = 50, nullable = true)
	private String lessorAddress1;

	@Column(name = "lessor_address2", length = 50, nullable = true)
	private String lessorAddress2;

	@Column(name = "lessor_city", length = 50, nullable = true)
	private String lessorCity;

	@Column(name = "lessor_state", length = 2, columnDefinition = "BPCHAR(2)")
	private String lessorState;

	@Column(name = "lessor_postal_code", length = 10, nullable = true)
	private String lessorPostalCode;

	@Column(name = "lessor_phone", length = 20, nullable = true)
	private String lessorPhone;
	
	@Column(name = "lessor_mobile", length = 20, nullable = true)
	private String lessorMobile;
	
	@Column(name = "lessor_gov_id", length = 20, nullable = true)
	private String lessorGovId;
	
	@Column(name = "active", nullable = false)
	private boolean active = true;

	@OneToMany(mappedBy = "truck")
	private Set<CompanyTruck> companyTrucks = new HashSet<CompanyTruck>();

	@OneToMany(mappedBy = "truck")
	private Set<DriverTruck> driverTrucks = new HashSet<DriverTruck>();
	
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

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicenseState() {
		return licenseState;
	}

	public void setLicenseState(String licenseState) {
		this.licenseState = licenseState;
	}

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public Date getAnnualInspection() {
		return annualInspection;
	}

	public void setAnnualInspection(Date annualInspection) {
		this.annualInspection = annualInspection;
	}

	public Date getBobtailInsurance() {
		return bobtailInsurance;
	}

	public void setBobtailInsurance(Date bobtailInsurance) {
		this.bobtailInsurance = bobtailInsurance;
	}

	public Boolean getIfta() {
		return ifta;
	}

	public void setIfta(Boolean ifta) {
		this.ifta = ifta;
	}

	public Date getQuarterlyMaintenance() {
		return quarterlyMaintenance;
	}

	public void setQuarterlyMaintenance(Date quarterlyMaintenance) {
		this.quarterlyMaintenance = quarterlyMaintenance;
	}

	public Date getPhysicalDamageInsurance() {
		return physicalDamageInsurance;
	}

	public void setPhysicalDamageInsurance(Date physicalDamageInsurance) {
		this.physicalDamageInsurance = physicalDamageInsurance;
	}

	public String getLessorNumber() {
		return lessorNumber;
	}

	public void setLessorNumber(String lessorNumber) {
		this.lessorNumber = lessorNumber;
	}

	public String getLessorName() {
		return lessorName;
	}

	public void setLessorName(String lessorName) {
		this.lessorName = lessorName;
	}

	public String getLessorAddress1() {
		return lessorAddress1;
	}

	public void setLessorAddress1(String lessorAddress1) {
		this.lessorAddress1 = lessorAddress1;
	}

	public String getLessorAddress2() {
		return lessorAddress2;
	}

	public void setLessorAddress2(String lessorAddress2) {
		this.lessorAddress2 = lessorAddress2;
	}

	public String getLessorCity() {
		return lessorCity;
	}

	public void setLessorCity(String lessorCity) {
		this.lessorCity = lessorCity;
	}

	public String getLessorState() {
		return lessorState;
	}

	public void setLessorState(String lessorState) {
		this.lessorState = lessorState;
	}

	public String getLessorPostalCode() {
		return lessorPostalCode;
	}

	public void setLessorPostalCode(String lessorPostalCode) {
		this.lessorPostalCode = lessorPostalCode;
	}

	public String getLessorPhone() {
		return lessorPhone;
	}

	public void setLessorPhone(String lessorPhone) {
		this.lessorPhone = lessorPhone;
	}

	public String getLessorMobile() {
		return lessorMobile;
	}

	public void setLessorMobile(String lessorMobile) {
		this.lessorMobile = lessorMobile;
	}

	public String getLessorGovId() {
		return lessorGovId;
	}

	public void setLessorGovId(String lessorGovId) {
		this.lessorGovId = lessorGovId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<CompanyTruck> getCompanyTrucks() {
		return companyTrucks;
	}

	public void setCompanyTrucks(Set<CompanyTruck> companyTrucks) {
		this.companyTrucks = companyTrucks;
	}

	public Set<DriverTruck> getDriverTrucks() {
		return driverTrucks;
	}

	public void setDriverTrucks(Set<DriverTruck> driverTrucks) {
		this.driverTrucks = driverTrucks;
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
