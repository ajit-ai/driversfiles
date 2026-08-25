package com.driversfiles.www.auth;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;

/**
 * @author Ajit Kumar
 *
 */
@Service("switchCheckerService")
@Transactional
public class SwitchUserCheckerService implements UserDetailsChecker {

	@Override
	public void check(UserDetails user) {
		
		// Check for an ADMIN trying to switch to another ADMIN (not allowed)
		if (user instanceof AuthDetails) {
			Person person = ((AuthDetails) user).getPerson();
			if (person.getType() == PersonType.ADMIN)
				throw new org.springframework.security.authentication.DisabledException("An admin cannot enter as another admin");
		}
		
		// Code from AccountStatusUserDetailsChecker
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }

	}

}
