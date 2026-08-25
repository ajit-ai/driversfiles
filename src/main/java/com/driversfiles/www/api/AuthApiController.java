package com.driversfiles.www.api;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.auth.JwtService;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Person;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

	private final PersonDao personDao;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;
	private final JwtService jwtService;

	public AuthApiController(PersonDao personDao,
			@Lazy PasswordEncoder passwordEncoder,
			AuthService authService, JwtService jwtService) {
		this.personDao = personDao;
		this.passwordEncoder = passwordEncoder;
		this.authService = authService;
		this.jwtService = jwtService;
	}

	public record LoginRequest(String email, String password) {}
	public record UserInfo(String uuid, String email, String firstName, String lastName,
			String type, List<String> roles) {}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest body) {
		Person p = personDao.findByEmail(body.email() == null ? "" : body.email().trim());
		boolean valid = p != null && p.getPassword() != null && passwordEncoder.matches(
				body.password() == null ? "" : body.password(),
				p.getPassword().getValue() + ":" + p.getPassword().getSalt());
		if (!valid) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Invalid email or password"));
		}
		var details = authService.loadUserByUsername(p.getEmail());
		List<String> roles = details.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).toList();
		String token = jwtService.generate(p.getEmail(), roles);
		return ResponseEntity.ok(Map.of(
				"token", token,
				"user", new UserInfo(p.getUuid(), p.getEmail(), p.getFirstName(),
						p.getLastName(), p.getType().name(), roles)));
	}

	@GetMapping("/me")
	public UserInfo me(Authentication authentication) {
		Person p = personDao.findByEmail(authentication.getName());
		if (p == null) {
			throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return new UserInfo(p.getUuid(), p.getEmail(), p.getFirstName(), p.getLastName(),
				p.getType().name(),
				SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).toList());
	}

	void simulateAuthenticationForTesting(Person person, List<String> roles) {
		var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(person.getEmail(), null, authorities));
	}
}
