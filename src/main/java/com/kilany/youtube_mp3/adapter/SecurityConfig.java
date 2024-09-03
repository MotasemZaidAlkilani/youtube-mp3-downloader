package com.kilany.youtube_mp3.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				authorizeRequests -> authorizeRequests.requestMatchers("/", "/convert", "/progress").permitAll() // Allow
																													// access
																													// to
																													// these
																													// paths
																													// without
																													// authentication
						.anyRequest().authenticated() // Allow access to all other paths without authentication
		).formLogin().disable().httpBasic().disable().csrf().disable();;
		; // Disable CSRF protection if not using forms (optional, based on your use case)

		return http.build();
	}
}
