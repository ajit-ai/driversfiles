package com.driversfiles.www.core.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;

/**
 * Holds role data.
 * 
 * @author Jagadesh Varada
 * @author Erik R. Jensen
 */
@Entity
@Table(name = "role")
@SequenceGenerator(name = "roleIdSeq", sequenceName = "role_id_seq")
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "roleIdSeq")
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "name", nullable = false, unique = true, length = 50)
	private String name;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<Person> people = new HashSet<Person>();

	public Role() {}

	public Role(String name) {
		this.name = name;
	}

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

	public Set<Person> getPeople() {
		return people;
	}

	public void setPeople(Set<Person> people) {
		this.people = people;
	}

	@Override
	public String toString() {
		return name != null ? name.replaceAll("ROLE_", "") : super.toString();
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return name != null && o instanceof Role ? name.equals(((Role)o).name) : super.equals(o);
	}
}
