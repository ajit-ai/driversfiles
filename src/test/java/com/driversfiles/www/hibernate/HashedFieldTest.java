package com.driversfiles.www.hibernate;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class HashedFieldTest {

	@Test
	void hashesPlainTextWithRandomSalt() {
		HashedField hf = new HashedField("secret");
		assertNotNull(hf.getValue());
		assertNotNull(hf.getSalt());
		assertEquals(44, hf.getValue().length());
		assertEquals(40, hf.getSalt().length());
		assertDoesNotThrow(() -> Base64.getDecoder().decode(hf.getValue()));
		assertDoesNotThrow(() -> Base64.getDecoder().decode(hf.getSalt()));
	}

	@Test
	void saltsAreRandomPerInstance() {
		HashedField a = new HashedField("same-password");
		HashedField b = new HashedField("same-password");
		assertNotEquals(a.getSalt(), b.getSalt());
		assertNotEquals(a.getValue(), b.getValue());
	}

	@Test
	void valueVerifiesAgainstKnownPassword() {
		HashedField hf = new HashedField("secret");
		byte[] salt = Base64.getDecoder().decode(hf.getSalt());
		java.security.MessageDigest md;
		try {
			md = java.security.MessageDigest.getInstance("SHA-256");
		} catch (java.security.NoSuchAlgorithmException x) {
			throw new IllegalStateException(x);
		}
		md.update(salt);
		md.update("secret".getBytes(java.nio.charset.StandardCharsets.UTF_8));
		String expected = Base64.getEncoder().encodeToString(md.digest());
		assertEquals(expected, hf.getValue());
	}

	@Test
	void nullInputProducesNullFields() {
		HashedField hf = new HashedField((String) null);
		assertNull(hf.getValue());
		assertNull(hf.getSalt());
	}

	@Test
	void equalsAndHashCodeFollowValue() {
		HashedField a = new HashedField("v1", "s1");
		HashedField b = new HashedField("v1", "ignored-different-salt");
		HashedField c = new HashedField("other", "s1");
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a, c);
		assertNotEquals(a, null);
		assertNotEquals(a, "not-a-hashed-field");
	}
}
