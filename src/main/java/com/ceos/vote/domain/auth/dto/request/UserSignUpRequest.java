package com.ceos.vote.domain.auth.dto.request;

import com.ceos.vote.domain.team.entity.enums.TeamName;
import com.ceos.vote.domain.user.entity.enums.Part;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record UserSignUpRequest(

	@Schema(description = "파트", example = "BE")
	@NotNull(message = "파트 선택은 필수입니다.")
	Part part,

	@Schema(description = "팀 이름", example = "DITDA")
	@NotNull(message = "팀 선택은 필수입니다.")
	TeamName team,

	@Schema(description = "이름", example = "홍길동")
	@NotBlank(message = "이름은 필수입니다.")
	@Size(max = 20, message = "올바르지 않은 이름입니다.")
	String name,

	@Schema(description = "아이디 (6 ~ 20자 이내)", example = "testid123")
	@NotBlank(message = "아이디는 필수입니다.")
	@Size(min = 6, max = 20, message = "아이디는 6자 이상 20자 이하여야 합니다.")
	String username,

	@Schema(description = "이메일", example = "test@gmail.com")
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
	String email,

	@Schema(description = "비밀번호", example = "@password1234")
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하여야 합니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
		message = "비밀번호는 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."
	)
	String password
) {
}
