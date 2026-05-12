package com.ceos.vote.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "아이디 중복 확인 요청")
public record CheckUsernameRequest(

	@Schema(description = "확인할 아이디", example = "testid123")
	@NotBlank(message = "아이디는 필수입니다.")
	String username
) {
}
