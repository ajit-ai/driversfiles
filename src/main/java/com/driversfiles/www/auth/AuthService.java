package com.driversfiles.www.auth;

import com.driversfiles.www.core.data.Person;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provide authentication and authorization services.
 *
 * @author Ajit Kumar
 */
public interface AuthService extends UserDetailsService {

	static final String ROLE_ADMIN = "ROLE_ADMIN";
	static final String ROLE_COMPANY = "ROLE_COMPANY";
	static final String ROLE_DRIVER = "ROLE_DRIVER";

	/**
	 * Returns the authenticated user
	 *
	 * @return the authenticated person or null if not found
	 */
	Person getAuthenticatedUser();

	/**
	 * Returns the effective user.
	 *
	 * @return the effective user or null if not found
	 */
	Person getEffectiveUser();

	/**
	 * Returns the IP address of the authenticated user.
	 *
	 * @return the IP address if the authenticated user or null
	 */
	String getAuthenticatedAddress();

	/**
	 * Reloads the authenticated user details from the database.
	 *
	 * @return the authenticated user or null if there is no user authenticated
	 */
	Person reloadAuthenticatedUser();

	/**
	 * Authenticates a user to the system without validating credentials.
	 *
	 * @param username the username of the user to authenticate
	 * @return the person who was authenticated or null if there is no such user
	 */
	Person authenticate(String username);

	/**
	 * Checks to see if the provided password matches to user's password.
	 *
	 * @param password the password to validate
	 * @param username the username of the user to validate against
	 * @return true if the password is valid, false if the passwords don't match or the user could not be found
	 */
	boolean isPasswordValid(String password, String username);

	/**
	 * Checks if the authenticated user is in the given role. This method mimics the functionality
	 * of HttpServletRequest#isUserInRole so your code doesn't have to pass around request objects
	 * resulting in cleaner code.
	 *
	 * @param role the role to check
	 * @return true if the user is in the role, false if otherwise
	 */
	boolean isUserInRole(String role);
}
