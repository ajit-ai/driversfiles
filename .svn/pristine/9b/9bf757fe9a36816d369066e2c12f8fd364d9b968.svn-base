package com.driversfiles.www.auth;

import com.driversfiles.www.core.dao.EventDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.EventType;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service("authService")
@Transactional
public class AuthServiceImpl implements AuthService, ApplicationListener {

	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private PersonDao personDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuthDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException, DataAccessException {
		log.trace("Attempting to load user [" + username + "].");

		// Validate the parameters
		if (username == null || username.trim().equals("")) {
			throw new UsernameNotFoundException("Username cannot be null or empty.");
		}

		// Find the person
		final Person p = personDao.findByEmail(username);
		if (p == null) {
			throw new UsernameNotFoundException("No user with username [" + username + "].");
		}
		log.trace("Found user [" + username + "].");

		// Add dynamic roles
		final List<String> dynamicRoles = new ArrayList<String>();
		if (p.getType() == PersonType.ADMIN) {
			dynamicRoles.add(AuthService.ROLE_ADMIN);
		} else if (p.getType() == PersonType.COMPANY) {
			dynamicRoles.add(AuthService.ROLE_COMPANY);
		} else if (p.getType() == PersonType.DRIVER) {
			dynamicRoles.add(AuthService.ROLE_DRIVER);
		}
		return new AuthDetails(p, dynamicRoles);
	}

	private AuthDetails getAuthDetails() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getPrincipal() instanceof AuthDetails
				? (AuthDetails) auth.getPrincipal()
				: null;
	}

	private AuthDetails setAuthentication(final AuthDetails details) {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(details, details.getPerson(), details.getAuthorities()));
		return details;
	}

	private Authentication getEffectiveAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private Authentication getAuthenticatedAuthentication() {
		final Authentication auth = getEffectiveAuthentication();
		if (auth != null) {
			for (GrantedAuthority authority: auth.getAuthorities()) {
				if (authority instanceof SwitchUserGrantedAuthority) {
					return ((SwitchUserGrantedAuthority)authority).getSource();
				}
			}
		}
		return auth;
	}

	@Override
	public Person getAuthenticatedUser() {
		Authentication auth = getAuthenticatedAuthentication();
		if (auth != null) {
			return personDao.findByEmail(auth.getName());
		}
		return null;
	}

	@Override
	public Person getEffectiveUser() {
		Authentication auth = getEffectiveAuthentication();
		if (auth != null) {
			return personDao.findByEmail(auth.getName());
		}
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthenticatedAddress() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (auth.getDetails() != null && auth.getDetails() instanceof WebAuthenticationDetails) {
				return ((WebAuthenticationDetails) auth.getDetails()).getRemoteAddress();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Person reloadAuthenticatedUser() {
		AuthDetails details = getAuthDetails();
		return details != null
				? authenticate(details.getPerson().getEmail())
				: null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Person authenticate(final String username) {
		try {
			final AuthDetails details = loadUserByUsername(username);
			if (details != null) {
				return setAuthentication(details).getPerson();
			}
		} catch (UsernameNotFoundException x) {
			return null;
		} catch (DataAccessException x) {
			return null;
		}
		return null;
	}

	@Override
	public boolean isPasswordValid(String password, String username) {
		Person p = personDao.findByEmail(username);
		return p != null &&  passwordEncoder.isPasswordValid(
				p.getPassword().getValue(), password, p.getPassword().getSalt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUserInRole(final String role) {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null || auth.getAuthorities() == null) {
			return false;
		}
		for (GrantedAuthority ga : auth.getAuthorities()) {
			if (role.equals(ga.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof AuthenticationSuccessEvent) {
			AuthenticationSuccessEvent e = (AuthenticationSuccessEvent)event;
			Authentication auth = e.getAuthentication();
			AuthDetails authDetails = auth != null && auth.getPrincipal() instanceof AuthDetails
					? (AuthDetails) auth.getPrincipal() : null;
			if (authDetails != null) {
				WebAuthenticationDetails wad = (WebAuthenticationDetails)auth.getDetails();
				if (wad != null) {
					eventDao.log(EventType.AUTH_SUCCESS, "Login successfull for username : " + auth.getCredentials(),
							authDetails.getPerson(), wad.getRemoteAddress());
				}
			}
		} else if (event instanceof AbstractAuthenticationFailureEvent) {
			AbstractAuthenticationFailureEvent e = (AbstractAuthenticationFailureEvent) event;
			Authentication auth = e.getAuthentication();
			String ip = ((WebAuthenticationDetails) auth.getDetails()).getRemoteAddress();
			eventDao.log(EventType.AUTH_FAILURE, "Login failed for username : " + auth.getPrincipal(), ip);
		}
	}
}
