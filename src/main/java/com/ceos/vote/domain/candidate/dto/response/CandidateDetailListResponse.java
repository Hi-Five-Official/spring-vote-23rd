package com.ceos.vote.domain.candidate.dto.response;

import java.util.List;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.domain.candidate.entity.enums.University;
import com.ceos.vote.global.entity.enums.Part;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파트장 후보 조회 응답")
public record CandidateDetailListResponse(

	@Schema(description = "파트장 후보 배열")
	List<CandidateDetailInfo> candidates
) {

	public static CandidateDetailListResponse from(List<CandidateDetailInfo> candidates) {
		return new CandidateDetailListResponse(candidates);
	}

	public record CandidateDetailInfo(

		@Schema(description = "파트장 후보 ID", example = "1")
		Long candidateId,

		@Schema(description = "파트장 후보 이름", example = "홍길동")
		String name,

		@Schema(description = "파트", example = "FE")
		Part part,

		@Schema(description = "파트장 후보 대학", example = "HONGIK")
		University university,

		@JsonProperty("isMyVote")
		@Schema(description = "유저가 투표한 후보 여부", example = "false")
		boolean isMyVote
	) {

		public static CandidateDetailInfo from(Candidate candidate, boolean isMyVote) {
			return new CandidateDetailInfo(
				candidate.getId(),
				candidate.getName(),
				candidate.getPart(),
				candidate.getUniversity(),
				isMyVote
			);
		}
	}
}
