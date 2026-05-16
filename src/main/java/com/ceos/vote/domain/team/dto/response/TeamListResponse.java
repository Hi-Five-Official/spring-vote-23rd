package com.ceos.vote.domain.team.dto.response;

import java.util.List;

import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.entity.enums.TeamName;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀명 응답")
public record TeamListResponse(

	@Schema(description = "팀 배열")
	List<TeamInfo> teams
) {
	public static TeamListResponse from(List<Team> teams) {
		return new TeamListResponse(
			teams.stream()
				.map(TeamInfo::from)
				.toList()
		);
	}

	public record TeamInfo(

		@Schema(description = "팀 ID", example = "1")
		Long teamId,

		@Schema(description = "팀명", example = "IPX")
		TeamName name
	) {
		public static TeamInfo from(Team team) {
			return new TeamInfo(team.getId(), team.getName());
		}

	}
}
