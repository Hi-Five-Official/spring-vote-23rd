package com.ceos.vote.global.jwt.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ceos.vote.global.jwt.JwtTokenProvider;
import com.ceos.vote.global.jwt.exceptions.JwtErrorType;
import com.ceos.vote.global.jwt.utils.CookieUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String token = resolveToken(request);

		if (StringUtils.hasText(token)) {
			try {
				Claims claims = jwtTokenProvider.validateToken(token);
				Long memberId = Long.parseLong(claims.getSubject());

				Authentication authentication =
					new UsernamePasswordAuthenticationToken(
						memberId,
						null,
						List.of(new SimpleGrantedAuthority("ROLE_USER"))
					);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (ExpiredJwtException e) {
				request.setAttribute("exception", JwtErrorType.TOKEN_EXPIRED);
			} catch (JwtException | IllegalArgumentException e) {
				request.setAttribute("exception", JwtErrorType.INVALID_TOKEN);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		// Swagger & Postman 전용
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(CookieUtils.ACCESS_TOKEN_COOKIE)) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}
}
