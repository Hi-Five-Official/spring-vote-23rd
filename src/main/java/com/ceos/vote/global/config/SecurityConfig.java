package com.ceos.vote.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ceos.vote.global.jwt.filter.JwtTokenFilter;
import com.ceos.vote.global.jwt.handler.JwtAccessDeniedHandler;
import com.ceos.vote.global.jwt.handler.JwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenFilter jwtTokenFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(
		HttpSecurity http,
		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		JwtAccessDeniedHandler jwtAccessDeniedHandler
	) throws Exception {
		http
			// csrf 비활성화
			.csrf(AbstractHttpConfigurer::disable)
			// form login 비활성화
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			// cors 설정
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// 세션 stateless
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 인증/인가 예외 처리
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)
			)
			// api 접근 권한 설정
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				.requestMatchers(HttpMethod.POST,
					"/api/v1/auth/signup",
					"/api/v1/auth/login",
					"/api/v1/auth/check-username",
					"/api/v1/auth/check-email",
					"/api/v1/auth/logout",
					"/api/v1/auth/refresh"
				).permitAll()
				// ROLE 없이 인증되면 허가되는 식으로 진행
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://localhost:5173"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
