package com.driversfiles.www.core.data;

import com.driversfiles.www.core.dao.Auditable;
import com.driversfiles.www.core.service.ImportType;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Holds data import information.
 *
 * @author Erik R. Jensen
 */
@Entity
@Table(name = "data_import")
@SequenceGenerator(name = "dataImportIdSeq", sequenceName = "data_import_id_seq")
public class DataImport implements Serializable, Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "dataImportIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "import_type", nullable = false)
	private ImportType importType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company", nullable = false)
	private Company company;

	@Column(name = "import_data", nullable = false)
	private String data;

	@Column(name = "overwrite", nullable = false)
	private boolean overwrite;

	@Column(name = "import_log")
	private String log;

	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "success")
	private Boolean success;

	@Column(name = "created_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Person createdBy;

	@Column(name = "last_modified_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
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

	public ImportType getImportType() {
		return importType;
	}

	public void setImportType(ImportType importType) {
		this.importType = importType;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isOvewrite() {
		return overwrite;
	}

	public void setOvewrite(boolean ovewrite) {
		this.overwrite = ovewrite;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
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
