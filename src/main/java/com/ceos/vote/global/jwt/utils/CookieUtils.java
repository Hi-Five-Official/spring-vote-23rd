package com.ceos.vote.global.jwt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

	public static final String ACCESS_TOKEN_COOKIE = "access_token";

	@Value("${jwt.access-token-expiration}")
	private Long accessTokenExpiration;

	@Value("${jwt.cookie-secure}")
	private boolean secure;

	public ResponseCookie createAccessTokenCookie(String token) {
		return ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
			.httpOnly(true)
			.secure(secure)
			.sameSite("Lax")
			.path("/")
			.maxAge(accessTokenExpiration / 1000)
			.build();
	}

	public ResponseCookie deleteAccessTokenCookie() {
		return ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
			.httpOnly(true)
			.secure(secure)
			.sameSite("Lax")
			.path("/")
			.maxAge(0)
			.build();
	}
}
