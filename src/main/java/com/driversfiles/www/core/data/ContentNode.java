package com.driversfiles.www.core.data;

import com.driversfiles.www.core.dao.Auditable;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Holds content node data.
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "content_node")
@SequenceGenerator(name = "contentNodeIdSeq", sequenceName = "content_node_id_seq")
public class ContentNode implements Serializable, Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "contentNodeIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "name", length = 50, nullable = false, unique = true)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "content", nullable = false)
	private String content;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
