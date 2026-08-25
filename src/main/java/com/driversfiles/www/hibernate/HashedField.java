package com.driversfiles.www.hibernate;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Holds a hashed value and its salt. The hash is computed as
 * base64(sha-256(base64decode(salt) + utf8(value))).
 */
@Embeddable
public class HashedField implements Serializable {

	private static final long serialVersionUID = -4389276134877236512L;

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String ALGORITHM = "SHA-256";
	private static final int SALT_SIZE = 30;

	@Column(name = "password", length = 46)
	private String value;

	@Column(name = "salt", length = 40)
	private String salt;

	protected HashedField() {}

	public HashedField(String plainText) {
		if (plainText != null) {
			byte[] s = new byte[SALT_SIZE];
			RANDOM.nextBytes(s);
			this.salt = Base64.getEncoder().encodeToString(s);
			this.value = hash(plainText);
		}
	}

	public HashedField(String value, String salt) {
		this.value = value;
		this.salt = salt;
	}

	public String getValue() {
		return value;
	}

	public String getSalt() {
		return salt;
	}

	private String hash(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(Base64.getDecoder().decode(salt));
			md.update(plainText.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(md.digest());
		} catch (NoSuchAlgorithmException x) {
			throw new IllegalStateException("Unable to hash value: " + x.getMessage(), x);
		}
	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof HashedField)) {
			return false;
		}
		HashedField hf = (HashedField) o;
		return value != null ? value.equals(hf.value) : hf.value == null;
	}
}
