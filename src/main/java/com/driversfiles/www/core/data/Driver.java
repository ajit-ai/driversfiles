package com.driversfiles.www.core.data;

import com.driversfiles.www.core.dao.Auditable;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds driver data.
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "driver")
@SequenceGenerator(name="driverIdSeq", sequenceName = "driver_id_seq")
public class Driver implements Serializable, Auditable, UUIDIdentified {

	private static final long serialVersionUID = 2449259050852903054L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "driverIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person", nullable = false)
	private Person person;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;

	@Temporal(TemporalType.DATE)
	@Column(name = "dob")
	private Date dob;

	@Column(name = "ssn", length = 11)
	private String ssn;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "mobile", length = 20)
	private String mobile;

	@Column(name = "fax", length = 20)
	private String fax;

	@Column(name = "address1", length = 50)
	private String address1;

	@Column(name = "address2", length = 50)
	private String address2;

	@Column(name = "city", length = 50)
	private String city;

	@Column(name = "state", length = 2, columnDefinition = "BPCHAR(2)")
	private String state;

	@Column(name = "postal_code", length = 10)
	private String postalCode;
	
	@Column(name = "contact_name", length = 100)
	private String contactName;
	
	@Column(name = "contact_relationship", length = 50)
	private String contactRelationship;

	@Column(name = "contact_phone", length = 20)
	private String contactPhone;

	@Column(name = "contact_mobile", length = 20)
	private String contactMobile;

	@Temporal(TemporalType.DATE)
	@Column(name = "available_date")
	private Date availableDate;

	@Column(name = "felony_conviction")
	private Boolean felonyConviction;

	@Column(name = "felony_conviction_date")
	@Temporal(TemporalType.DATE)
	private Date felonyConvictionDate;

	@Column(name = "felony_conviction_explanation")
	private String felonyConvictionExplanation;

	@Column(name = "dui_conviction")
	private Boolean duiConviction;

	@Column(name = "dui_conviction_date")
	@Temporal(TemporalType.DATE)
	private Date duiConvictionDate;

	@Column(name = "dui_conviction_explanation")
	private String duiConvictionExplanation;

	@Column(name = "license_revoked")
	private Boolean licenseRevoked;

	@Column(name = "license_revoked_date")
	@Temporal(TemporalType.DATE)
	private Date licenseRevokedDate;

	@Column(name = "license_revoked_explanation")
	private String licenseRevokedExplanation;

	@Column(name = "controlled_substance")
	private Boolean controlledSubstance;

	@Column(name = "controlled_substance_date")
	@Temporal(TemporalType.DATE)
	private Date controlledSubstanceDate;

	@Column(name = "controlled_substance_explanation")
	private String controlledSubstanceExplanation;

	@Column(name = "driver_school")
	private Boolean driverSchool;

	@Column(name = "driver_school_name")
	private String driverSchoolName;
	
	@Column(name = "highest_grade_completed")
	private String highestGradeCompleted;
	
	@Column(name = "eligible_employment")
	private Boolean eligibleEmployment;

	@Column(name = "not_eligible_explanation")
	private String notEligibleExplanation;

	@Column(name = "no_additional_addresses")
	private Boolean noAdditionalAddresses;

	@Column(name = "hazmat_expiration")
	@Temporal(TemporalType.DATE)
	private Date hazmatExpiration;

	@Column(name = "mvr_date")
	@Temporal(TemporalType.DATE)
	private Date mvrDate;

	@Column(name = "medical_review_date")
	@Temporal(TemporalType.DATE)
	private Date medicalReviewDate;
	
	@Column(name = "access_code", length = 6, columnDefinition = "BPCHAR(6)")
	private String accessCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "access_code_created_date", nullable = true)
	private Date accessCodeCreatedDate;

	@OneToMany(mappedBy = "driver")
	private Set<CompanyDriver> companyDrivers = new HashSet<CompanyDriver>();

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

	@Override
	public String getUuid() {
		return uuid;
	}

	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactRelationship() {
		return contactRelationship;
	}

	public void setContactRelationship(String contactRelationship) {
		this.contactRelationship = contactRelationship;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}

	public Boolean getFelonyConviction() {
		return felonyConviction;
	}

	public void setFelonyConviction(Boolean felonyConviction) {
		this.felonyConviction = felonyConviction;
	}

	public Date getFelonyConvictionDate() {
		return felonyConvictionDate;
	}

	public void setFelonyConvictionDate(Date felonyConvictionDate) {
		this.felonyConvictionDate = felonyConvictionDate;
	}

	public String getFelonyConvictionExplanation() {
		return felonyConvictionExplanation;
	}

	public void setFelonyConvictionExplanation(String felonyConvictionExplanation) {
		this.felonyConvictionExplanation = felonyConvictionExplanation;
	}

	public Boolean getDuiConviction() {
		return duiConviction;
	}

	public void setDuiConviction(Boolean duiConviction) {
		this.duiConviction = duiConviction;
	}

	public Date getDuiConvictionDate() {
		return duiConvictionDate;
	}

	public void setDuiConvictionDate(Date duiConvictionDate) {
		this.duiConvictionDate = duiConvictionDate;
	}

	public String getDuiConvictionExplanation() {
		return duiConvictionExplanation;
	}

	public void setDuiConvictionExplanation(String duiConvictionExplanation) {
		this.duiConvictionExplanation = duiConvictionExplanation;
	}

	public Boolean getLicenseRevoked() {
		return licenseRevoked;
	}

	public void setLicenseRevoked(Boolean licenseRevoked) {
		this.licenseRevoked = licenseRevoked;
	}

	public Date getLicenseRevokedDate() {
		return licenseRevokedDate;
	}

	public void setLicenseRevokedDate(Date licenseRevokedDate) {
		this.licenseRevokedDate = licenseRevokedDate;
	}

	public String getLicenseRevokedExplanation() {
		return licenseRevokedExplanation;
	}

	public void setLicenseRevokedExplanation(String licenseRevokedExplanation) {
		this.licenseRevokedExplanation = licenseRevokedExplanation;
	}

	public Boolean getControlledSubstance() {
		return controlledSubstance;
	}

	public void setControlledSubstance(Boolean controlledSubstance) {
		this.controlledSubstance = controlledSubstance;
	}

	public Date getControlledSubstanceDate() {
		return controlledSubstanceDate;
	}

	public void setControlledSubstanceDate(Date controlledSubstanceDate) {
		this.controlledSubstanceDate = controlledSubstanceDate;
	}

	public String getControlledSubstanceExplanation() {
		return controlledSubstanceExplanation;
	}

	public void setControlledSubstanceExplanation(String controlledSubstanceExplanation) {
		this.controlledSubstanceExplanation = controlledSubstanceExplanation;
	}

	public Boolean getDriverSchool() {
		return driverSchool;
	}

	public void setDriverSchool(Boolean driverSchool) {
		this.driverSchool = driverSchool;
	}

	public String getDriverSchoolName() {
		return driverSchoolName;
	}

	public void setDriverSchoolName(String driverSchoolName) {
		this.driverSchoolName = driverSchoolName;
	}

	public String getHighestGradeCompleted() {
		return highestGradeCompleted;
	}

	public void setHighestGradeCompleted(String highestGradeCompleted) {
		this.highestGradeCompleted = highestGradeCompleted;
	}

	public Boolean getEligibleEmployment() {
		return eligibleEmployment;
	}

	public void setEligibleEmployment(Boolean eligibleEmployment) {
		this.eligibleEmployment = eligibleEmployment;
	}

	public String getNotEligibleExplanation() {
		return notEligibleExplanation;
	}

	public void setNotEligibleExplanation(String notEligibleExplanation) {
		this.notEligibleExplanation = notEligibleExplanation;
	}

	public Boolean getNoAdditionalAddresses() {
		return noAdditionalAddresses;
	}

	public void setNoAdditionalAddresses(Boolean noAdditionalAddresses) {
		this.noAdditionalAddresses = noAdditionalAddresses;
	}

	public Date getHazmatExpiration() {
		return hazmatExpiration;
	}

	public void setHazmatExpiration(Date hazmatExpiration) {
		this.hazmatExpiration = hazmatExpiration;
	}

	public Date getMvrDate() {
		return mvrDate;
	}

	public void setMvrDate(Date mvrDate) {
		this.mvrDate = mvrDate;
	}

	public Date getMedicalReviewDate() {
		return medicalReviewDate;
	}

	public void setMedicalReviewDate(Date medicalReviewDate) {
		this.medicalReviewDate = medicalReviewDate;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public Set<CompanyDriver> getCompanyDrivers() {
		return companyDrivers;
	}

	public Date getAccessCodeCreatedDate() {
		return accessCodeCreatedDate;
	}

	public void setAccessCodeCreatedDate(Date accessCodeCreatedDate) {
		this.accessCodeCreatedDate = accessCodeCreatedDate;
	}

	public Date getAccessCodeExpireDate() {
		if (accessCodeCreatedDate == null)
			return null;
		Calendar expire = Calendar.getInstance();
		expire.setTime(accessCodeCreatedDate);
		expire.add(Calendar.DAY_OF_YEAR, 30);
		return expire.getTime();
	}

	public void setCompanyDrivers(Set<CompanyDriver> companyDrivers) {
		this.companyDrivers = companyDrivers;
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
