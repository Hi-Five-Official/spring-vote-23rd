package com.ceos.vote.domain.team.dto.response;

import java.util.List;
import java.util.Objects;

import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.entity.enums.TeamName;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 목록 응답")
public record TeamDetailListResponse(

	@Schema(description = "팀 배열")
	List<TeamDetailInfo> teams
) {

	public static TeamDetailListResponse from(List<Team> teams, Long myVotedTeamId) {
		List<TeamDetailInfo> infos = teams.stream()
			.map(team -> TeamDetailInfo.from(
				team,
				Objects.equals(myVotedTeamId, team.getId())
			))
			.toList();
		return new TeamDetailListResponse(infos);
	}

	public record TeamDetailInfo(
		@Schema(description = "팀 ID", example = "1")
		Long teamId,

		@Schema(description = "팀 이름", example = "IPX")
		TeamName name,

		@JsonProperty("isMyVote")
		@Schema(description = "유저가 투표한 팀 여부", example = "false")
		boolean isMyVote
	) {
		public static TeamDetailInfo from(Team team, boolean isMyVote) {
			return new TeamDetailInfo(team.getId(), team.getName(), isMyVote);
		}
	}
}
