package com.ceos.vote.domain.auth.dto;

import org.springframework.http.ResponseCookie;

public record AuthResult(

	Long userId,

	String accessToken,

	ResponseCookie refreshTokenCookie
) {
}
