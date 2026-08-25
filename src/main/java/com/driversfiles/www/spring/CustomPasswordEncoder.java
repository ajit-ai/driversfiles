package com.driversfiles.www.spring;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Verifies passwords stored as "hash:salt" where hash = base64(sha-256(base64decode(salt) + utf8(password))),
 * matching the legacy HashedField format used by the original application.
 */
public class CustomPasswordEncoder implements PasswordEncoder {

	private static final SecureRandom RANDOM = new SecureRandom();

	@Override
	public String encode(CharSequence rawPassword) throws DataAccessException {
		byte[] salt = new byte[30];
		RANDOM.nextBytes(salt);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		return digest(encodedSalt, rawPassword.toString()) + ":" + encodedSalt;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) throws DataAccessException {
		if (encodedPassword == null || !encodedPassword.contains(":")) {
			return false;
		}
		int idx = encodedPassword.indexOf(':');
		String value = encodedPassword.substring(0, idx);
		String salt = encodedPassword.substring(idx + 1);
		return MessageDigest.isEqual(
				value.getBytes(StandardCharsets.US_ASCII),
				digest(salt, rawPassword == null ? "" : rawPassword.toString()).getBytes(StandardCharsets.US_ASCII));
	}

	private String digest(String base64Salt, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(Base64.getDecoder().decode(base64Salt));
			md.update(password.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(md.digest());
		} catch (NoSuchAlgorithmException x) {
			throw new IllegalStateException("Unable to hash password: " + x.getMessage(), x);
		}
	}
}
