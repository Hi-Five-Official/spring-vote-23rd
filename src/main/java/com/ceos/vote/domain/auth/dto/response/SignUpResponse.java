package com.ceos.vote.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignUpResponse(

	@Schema(description = "Access Token")
	String accessToken
) {
	public static SignUpResponse of(String accessToken) {
		return new SignUpResponse(accessToken);
	}
}
