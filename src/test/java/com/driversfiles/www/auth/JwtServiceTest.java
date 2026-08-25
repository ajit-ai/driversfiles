package com.driversfiles.www.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

	private final JwtService jwtService = new JwtService(
			"driversfiles-dev-secret-key-change-me-in-production-32bytes", 30);

	@Test
	void roundTripPreservesSubjectAndRoles() {
		String token = jwtService.generate("driver@example.com", List.of("ROLE_DRIVER", "ROLE_ADMIN"));
		Claims claims = jwtService.parse(token);
		assertEquals("driver@example.com", claims.getSubject());
		assertEquals(List.of("ROLE_DRIVER", "ROLE_ADMIN"), claims.get("roles", List.class));
	}

	@Test
	void tamperedTokenIsRejected() {
		String token = jwtService.generate("a@b.c", List.of("ROLE_DRIVER"));
		String tampered = token.substring(0, token.length() - 3) + "xxx";
		assertThrows(io.jsonwebtoken.JwtException.class, () -> jwtService.parse(tampered));
	}

	@Test
	void differentSecretRejectsForeignTokens() {
		JwtService other = new JwtService(
				"another-secret-key-that-is-long-enough-32bytes!!", 30);
		String foreign = other.generate("a@b.c", List.of());
		assertThrows(io.jsonwebtoken.JwtException.class, () -> jwtService.parse(foreign));
	}
}
