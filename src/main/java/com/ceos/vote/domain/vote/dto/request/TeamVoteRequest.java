package com.ceos.vote.domain.vote.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "팀 투표 요청")
public record TeamVoteRequest(

	@NotNull
	@Schema(description = "팀 ID", example = "1")
	Long teamId
) {
}
