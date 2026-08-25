package com.driversfiles.www.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

	private final SecretKey key;
	private final long expirationMillis;

	public JwtService(
			@Value("${app.jwt.secret:driversfiles-dev-secret-key-change-me-in-production-32bytes}") String secret,
			@Value("${app.jwt.expiration-minutes:480}") long expirationMinutes) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMillis = expirationMinutes * 60_000L;
	}

	public String generate(String email, List<String> roles) {
		Date now = new Date();
		return Jwts.builder()
				.subject(email)
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expirationMillis))
				.signWith(key)
				.compact();
	}

	public Claims parse(String token) throws JwtException {
		return Jwts.parser().verifyWith(key).build()
				.parseSignedClaims(token).getPayload();
	}
}
