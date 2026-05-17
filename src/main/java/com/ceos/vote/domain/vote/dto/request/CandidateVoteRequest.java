package com.ceos.vote.domain.vote.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "파트장 후보 투표 요청")
public record CandidateVoteRequest(

	@NotNull
	@Schema(description = "후보자 ID", example = "1")
	Long candidateId
) {
}
