package com.driversfiles.www.core.data;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

	private Person createPerson(String email, String uuid) {
		Person p = new Person();
		p.setEmail(email);
		p.setUuid(uuid);
		p.setFirstName("Ajit");
		p.setLastName("Kumar");
		return p;
	}

	@Test
	void equalsBasedOnEmailAndUuid() {
		String uuid = UUID.randomUUID().toString();
		Person a = createPerson("same@example.com", uuid);
		Person b = createPerson("same@example.com", uuid);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void differentUuidMeansNotEqual() {
		Person a = createPerson("same@example.com", UUID.randomUUID().toString());
		Person b = createPerson("same@example.com", UUID.randomUUID().toString());
		assertNotEquals(a, b);
	}

	@Test
	void equalsHandlesNullAndForeignTypes() {
		Person a = createPerson("x@example.com", UUID.randomUUID().toString());
		assertNotEquals(a, null);
		assertNotEquals(a, "string");
		assertEquals(a, a);
	}

	@Test
	void adminFlagDependsOnRoles() {
		Person p = createPerson("a@example.com", UUID.randomUUID().toString());
		Role adminRole = new Role("ROLE_ADMIN");
		p.setRoles(new HashSet<>());
		p.getRoles().add(adminRole);
		assertTrue(p.isAdmin());

		Person nonAdmin = createPerson("b@example.com", UUID.randomUUID().toString());
		nonAdmin.setRoles(new HashSet<>());
		nonAdmin.getRoles().add(new Role("ROLE_DRIVER"));
		assertFalse(nonAdmin.isAdmin());
	}
}
