package com.gregori.config.security;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.gregori.config.jwt.JwtSecurityConfig;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final String[] allowedURL = { "/", "/member/register", "/auth/**", "/items", "/order" };
	private final TokenProvider tokenProvider;
	private final RefreshTokenMapper refreshTokenMapper;

	@Bean
	@Profile("test")
	SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
		log.info("테스트 필터 체인 실행");

		http.csrf(csrf -> csrf
				.ignoringRequestMatchers(toH2Console())
				.disable())

			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

			.authorizeHttpRequests(request -> request
				.requestMatchers(toH2Console()).permitAll()
				.anyRequest().authenticated())

			.securityMatcher(toH2Console())

			.apply(new JwtSecurityConfig(tokenProvider, refreshTokenMapper));

		return http.build();
	}

	@Bean
	@Profile("!test")
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("일반 필터 체인 실행");

		http.csrf(AbstractHttpConfigurer::disable)

			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

			.sessionManagement(configurer -> configurer
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(request -> request
				.requestMatchers(allowedURL).permitAll()
				.anyRequest().authenticated())

			.apply(new JwtSecurityConfig(tokenProvider, refreshTokenMapper));

		return http.build();
	}
}
