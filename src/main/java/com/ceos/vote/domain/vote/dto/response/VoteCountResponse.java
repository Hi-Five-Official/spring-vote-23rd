package com.ceos.vote.domain.vote.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "총 투표 수 조회 응답")
public record VoteCountResponse(

	@Schema(description = "진행된 총 투표 수", example = "18")
	long totalCount
) {

	public static VoteCountResponse from(long totalCount) {
		return new VoteCountResponse(totalCount);
	}
}
