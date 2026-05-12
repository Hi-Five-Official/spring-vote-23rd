package com.ceos.vote.domain.auth.dto.response;

import com.ceos.vote.domain.user.dto.response.UserResponse;
import com.ceos.vote.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(

	@Schema(description = "로그인 유저")
	UserResponse user
) {
	public static LoginResponse from(User user) {
		return new LoginResponse(UserResponse.from(user));
	}
}
