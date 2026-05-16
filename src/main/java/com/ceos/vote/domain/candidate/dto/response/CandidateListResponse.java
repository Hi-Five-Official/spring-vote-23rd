package com.ceos.vote.domain.candidate.dto.response;

import java.util.List;

import com.ceos.vote.domain.candidate.entity.Candidate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀별 팀원 응답")
public record CandidateListResponse(

	@Schema(description = "팀원 배열")
	List<CandidateInfo> candidates
) {

	public static CandidateListResponse from(List<Candidate> candidates) {
		return new CandidateListResponse(
			candidates.stream()
				.map(CandidateInfo::from)
				.toList()
		);
	}

	public record CandidateInfo(

		@Schema(description = "팀원 ID", example = "1")
		Long candidateId,

		@Schema(description = "팀원 이름", example = "홍길동")
		String name
	) {

		public static CandidateInfo from(Candidate candidate) {
			return new CandidateInfo(candidate.getId(), candidate.getName());
		}
	}
}
