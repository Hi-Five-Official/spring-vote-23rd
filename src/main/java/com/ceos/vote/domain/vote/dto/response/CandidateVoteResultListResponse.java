package com.ceos.vote.domain.vote.dto.response;

import java.util.List;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파트장 투표 결과 조회 응답")
public record CandidateVoteResultListResponse(

	@Schema(description = "파트장 투표 결과 배열")
	List<CandidateVoteResultInfo> candidates
) {

	public static CandidateVoteResultListResponse from(List<CandidateVoteResultInfo> candidates) {
		return new CandidateVoteResultListResponse(candidates);
	}

	public record CandidateVoteResultInfo(

		@Schema(description = "후보자 ID", example = "1")
		Long candidateId,

		@Schema(description = "후보자 이름", example = "홍길동")
		String name,

		@Schema(description = "투표 수", example = "10")
		int voteCount,

		@JsonProperty("isMyVote")
		@Schema(description = "유저가 투표한 후보자 여부", example = "true")
		boolean isMyVote
	) {

		public static CandidateVoteResultInfo from(Candidate candidate, boolean isMyVote) {
			return new CandidateVoteResultInfo(
				candidate.getId(),
				candidate.getName(),
				candidate.getVoteCount(),
				isMyVote
			);
		}
	}
}
