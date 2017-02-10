package com.driversfiles.www.spring;

import org.apache.commons.codec.binary.Base64;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Handles password encoding for Spring Security.
 * 
 * @author Erik R. Jensen
 */
public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String pass, Object salt) throws DataAccessException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(Base64.decodeBase64(salt.toString().getBytes()));
			md.update(pass.getBytes());
			return new String(Base64.encodeBase64(md.digest()));
		} catch (NoSuchAlgorithmException x) {
			throw new DataRetrievalFailureException("Unable to SHA-256 sum the password: " + x.getMessage(), x);
		}
	}

	@Override
	public boolean isPasswordValid(String enc, String pass, Object salt) throws DataAccessException {
		return enc.equals(encodePassword(pass, salt));
	}
}
