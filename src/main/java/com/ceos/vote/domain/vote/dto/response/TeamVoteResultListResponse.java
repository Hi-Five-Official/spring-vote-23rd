package com.ceos.vote.domain.vote.dto.response;

import java.util.List;

import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.entity.enums.TeamName;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 투표 결과 조회 응답")
public record TeamVoteResultListResponse(

	@Schema(description = "팀 투표 결과 배열")
	List<TeamVoteResultInfo> teams
) {

	public static TeamVoteResultListResponse from(List<TeamVoteResultInfo> teams) {
		return new TeamVoteResultListResponse(teams);
	}

	public record TeamVoteResultInfo(

		@Schema(description = "팀 ID", example = "1")
		Long teamId,

		@Schema(description = "팀 이름", example = "IPX")
		TeamName name,

		@Schema(description = "투표 수", example = "10")
		int voteCount,

		@JsonProperty("isMyVote")
		@Schema(description = "유저가 투표한 팀 여부", example = "true")
		boolean isMyVote
	) {

		public static TeamVoteResultInfo from(Team team, boolean isMyVote) {
			return new TeamVoteResultInfo(
				team.getId(),
				team.getName(),
				team.getVoteCount(),
				isMyVote
			);
		}
	}
}
