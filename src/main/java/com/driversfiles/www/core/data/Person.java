package com.driversfiles.www.core.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.dao.Auditable;
import com.driversfiles.www.hibernate.HashedField;

/**
 * Holds person data.
 *
 * @author Ajit Kumar
 */
@Entity
@Table(name = "person")
@SequenceGenerator(name = "personIdSeq", sequenceName = "person_id_seq")
public class Person implements Serializable, Auditable, UUIDIdentified {

	private static final long serialVersionUID = 7736891528269688713L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "personIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "uuid", unique = true, length = 36, nullable = false)
	private String uuid;

	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;

	@Embedded
	private HashedField password;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 6)
	private PersonType type;

	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Column(name = "middle_name", length = 50)
	private String middleName;

	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Column(name = "locked", nullable = false)
	private boolean locked = false;

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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "person_role", joinColumns = @JoinColumn(name = "person"), inverseJoinColumns = @JoinColumn(name = "role"))
	private Set<Role> roles = new HashSet<Role>();

	@OneToOne(mappedBy = "person")
	private Company company;

	@OneToOne(mappedBy = "person")
	private Driver driver;

	public Person() {}

	public Person(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = new HashedField(password);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person(String email, String password, String firstName, String lastName, Set<Role> roles) {
		this(email, password, firstName, lastName);
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public HashedField getPassword() {
		return password;
	}

	public void setPassword(HashedField password) {
		this.password = password;
	}

	public void setPassword(String password) {
		this.password = new HashedField(password);
	}

	public PersonType getType() {
		return type;
	}

	public void setType(PersonType type) {
		this.type = type;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
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
	public void setCreatedBy(Person person) {
		this.createdBy = person;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isAdmin() {
		for (Role role : roles) {
			if (role.getName().equals(AuthService.ROLE_ADMIN)) {
				return true;
			}
		}
		return false;
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

	@Override
	public int hashCode() {
		return email != null && uuid != null ? email.hashCode() ^ uuid.hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof Person ? email.equals(((Person) o).email) && uuid.equals(((Person) o).uuid)
				: super.equals(o);
	}
}
