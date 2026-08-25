package com.driversfiles.www.auth;

import com.driversfiles.www.core.data.Role;
import com.driversfiles.www.core.data.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Holds authentication details for an authenticated user.
 *
 * @author Ajit Kumar
 */
public class AuthDetails implements UserDetails {

	private Person person;
	private String salt;
	private List<String> dynamicRoles;

	public AuthDetails(final Person person, final List<String> dynamicRoles) {
		this.person = person;
		this.dynamicRoles = dynamicRoles;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role r: person.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(r.getName()));
		}
		for (String r: dynamicRoles) {
			authorities.add(new SimpleGrantedAuthority(r));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return person.getPassword().getSalt() != null && person.getPassword().getValue() != null
				? person.getPassword().getValue() + ":" + person.getPassword().getSalt()
				: null;
	}

	public String getSalt() {
		return person.getPassword().getSalt();
	}

	@Override
	public String getUsername() {
		return person.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !person.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
