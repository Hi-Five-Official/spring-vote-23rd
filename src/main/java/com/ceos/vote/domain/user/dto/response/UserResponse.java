package com.ceos.vote.domain.user.dto.response;

import com.ceos.vote.domain.team.entity.enums.TeamName;
import com.ceos.vote.domain.user.entity.User;
import com.ceos.vote.global.entity.enums.Part;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 정보 응답")
public record UserResponse(

	@Schema(description = "유저 ID", example = "1")
	Long userId,

	@Schema(description = "아이디", example = "testid123")
	String username,

	@Schema(description = "이름", example = "홍길동")
	String name,

	@Schema(description = "이메일", example = "test@gmail.com")
	String email,

	@Schema(description = "소속 파트", example = "BE")
	Part part,

	@Schema(description = "소속 팀 ID", example = "1")
	Long teamId,

	@Schema(description = "소속 팀 이름", example = "DITDA")
	TeamName teamName
) {
	public static UserResponse from(User user) {
		return new UserResponse(
			user.getId(),
			user.getUsername(),
			user.getName(),
			user.getEmail(),
			user.getPart(),
			user.getTeam().getId(),
			user.getTeam().getName()
		);
	}
}
