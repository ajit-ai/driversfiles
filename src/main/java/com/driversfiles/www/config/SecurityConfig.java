package com.driversfiles.www.config;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.spring.CustomPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(AuthService authService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(authService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider) {
		return new ProviderManager(provider);
	}

	@Bean
	public SwitchUserFilter switchUserFilter(AuthService authService, UserDetailsChecker switchCheckerService) {
		SwitchUserFilter filter = new SwitchUserFilter();
		filter.setUserDetailsService(authService);
		filter.setUserDetailsChecker(switchCheckerService);
		filter.setUsernameParameter("j_username");
		filter.setSwitchUserUrl("/secure/admin/enter");
		filter.setSwitchFailureUrl("/secure/admin/dashboard?switch=false");
		filter.setExitUserUrl("/secure/exit");
		filter.setTargetUrl("/secure/entry");
		return filter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, SwitchUserFilter switchUserFilter) throws Exception {
		http
			.addFilterAt(switchUserFilter, SwitchUserFilter.class)
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/login**", "/signup**", "/application*").permitAll()
				.requestMatchers("/secure/admin/**").hasRole("ADMIN")
				.requestMatchers("/secure/company/**").hasRole("COMPANY")
				.requestMatchers("/secure/driver/**").hasRole("DRIVER")
				.requestMatchers("/secure/**").authenticated()
				.anyRequest().permitAll())
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/auth")
				.defaultSuccessUrl("/secure/entry", true)
				.failureUrl("/login?error=true")
				.permitAll())
			.logout(logout -> logout
				.logoutSuccessUrl("/login?logout=true")
				.invalidateHttpSession(true));
		return http.build();
	}
}
