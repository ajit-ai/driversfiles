package com.driversfiles.www.spring;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class CustomPasswordEncoderTest {

	private final PasswordEncoder encoder = new CustomPasswordEncoder();

	@Test
	void encodeProducesFoldedHashSaltFormat() {
		String encoded = encoder.encode("my-password");
		assertNotNull(encoded);
		assertTrue(encoded.contains(":"));
		String[] parts = encoded.split(":");
		assertEquals(2, parts.length);
		assertEquals(44, parts[0].length());
		assertEquals(40, parts[1].length());
	}

	@Test
	void matchesRoundTrip() {
		String encoded = encoder.encode("my-password");
		assertTrue(encoder.matches("my-password", encoded));
		assertFalse(encoder.matches("wrong-password", encoded));
	}

	@Test
	void matchesLegacyDatabaseFormat() {
		String legacyHash = "FW8F2r6PILFuUHDShsodRBuuxYj+cFYjSHXTpbAQ06I=";
		String legacySalt = "dCoY8zNjn1UsBNoZbVNtE0qoptZrS+4t4B2vFjbx";
		String stored = legacyHash + ":" + legacySalt;
		assertTrue(encoder.matches("password1", stored), "legacy seeded credential must remain valid");
		assertFalse(encoder.matches("password2", stored));
	}

	@Test
	void malformedEncodedPasswordsAreRejectedNotThrown() {
		assertDoesNotThrow(() -> assertFalse(encoder.matches("x", null)));
		assertDoesNotThrow(() -> assertFalse(encoder.matches("x", "")));
		assertDoesNotThrow(() -> assertFalse(encoder.matches("x", "no-delimiter-here")));
	}
}
