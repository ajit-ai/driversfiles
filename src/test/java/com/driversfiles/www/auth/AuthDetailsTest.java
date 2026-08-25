package com.driversfiles.www.auth;

import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.Role;
import com.driversfiles.www.hibernate.HashedField;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthDetailsTest {

	private Person createPerson() {
		Person p = new Person();
		p.setEmail("driver@example.com");
		Role role = new Role("ROLE_DRIVER");
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		p.setRoles(roles);
		p.setPassword(new HashedField("secret"));
		return p;
	}

	@Test
	void usernameIsEmail() {
		AuthDetails details = new AuthDetails(createPerson(), Collections.emptyList());
		assertEquals("driver@example.com", details.getUsername());
	}

	@Test
	void authoritiesCombineRolesAndDynamicRoles() {
		AuthDetails details = new AuthDetails(createPerson(),
				Collections.singletonList("ROLE_COMPANY"));
		Set<String> names = new HashSet<>();
		for (GrantedAuthority ga : details.getAuthorities()) {
			names.add(ga.getAuthority());
		}
		assertTrue(names.contains("ROLE_DRIVER"));
		assertTrue(names.contains("ROLE_COMPANY"));
		assertEquals(2, names.size());
	}

	@Test
	void passwordIsFoldedAsValueColonSalt() {
		Person p = createPerson();
		AuthDetails details = new AuthDetails(p, Collections.emptyList());
		String expected = p.getPassword().getValue() + ":" + p.getPassword().getSalt();
		assertEquals(expected, details.getPassword());
	}

	@Test
	void passwordNullWhenHashedFieldEmpty() {
		Person p = createPerson();
		p.setPassword((HashedField) null);
		assertNull(new AuthDetails(p, Collections.emptyList()).getPassword());
	}

	@Test
	void lockStateMapsToAccountNonLocked() {
		Person p = createPerson();
		assertTrue(new AuthDetails(p, Collections.emptyList()).isAccountNonLocked());
		p.setLocked(true);
		assertFalse(new AuthDetails(p, Collections.emptyList()).isAccountNonLocked());
	}

	@Test
	void accountFlagsDefaultToTrue() {
		AuthDetails details = new AuthDetails(createPerson(), Collections.emptyList());
		assertTrue(details.isAccountNonExpired());
		assertTrue(details.isCredentialsNonExpired());
		assertTrue(details.isEnabled());
	}
}
