package com.driversfiles.www.core.data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Holds event data.
 * 
 * @author Erik R. Jensen
 */
@Entity
@Table(name = "event")
@SequenceGenerator(name = "eventIdSeq", sequenceName = "event_id_seq")
public class Event implements UUIDIdentified, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eventIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 50, nullable = false)
	private EventType type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_date", nullable = false)
	private Date eventDate;

	@Column(name = "ip", length = 39)
	private String ip;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject", referencedColumnName = "id")
	private Person subject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person", referencedColumnName = "id")
	private Person person;

	@Column(name = "message")
	private String message;

	public Event(EventType type, Date eventDate, String message) {
		this.type = type;
		this.eventDate = eventDate;
		this.message = message;
	}

	public Event() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUuid() {
		return this.uuid;
	}

	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Person getSubject() {
		return subject;
	}

	public void setSubject(Person subject) {
		this.subject = subject;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
