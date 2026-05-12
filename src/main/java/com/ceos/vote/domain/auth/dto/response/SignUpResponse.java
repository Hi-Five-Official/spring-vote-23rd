package com.ceos.vote.domain.auth.dto.response;

import com.ceos.vote.domain.user.dto.response.UserResponse;
import com.ceos.vote.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignUpResponse(

	@Schema(description = "회원가입 유저")
	UserResponse user
) {
	public static SignUpResponse from(User user) {
		return new SignUpResponse(UserResponse.from(user));
	}
}
