package com.ceos.vote.domain.team.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.candidate.dto.response.CandidateListResponse;
import com.ceos.vote.domain.candidate.service.CandidateService;
import com.ceos.vote.domain.team.dto.response.TeamDetailListResponse;
import com.ceos.vote.domain.team.dto.response.TeamListResponse;
import com.ceos.vote.domain.team.service.TeamService;
import com.ceos.vote.global.apipayload.response.ApiResponse;
import com.ceos.vote.global.entity.enums.Part;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@Tag(name = "Team", description = "팀 관련 API")
public class TeamController {

	private final TeamService teamService;
	private final CandidateService candidateService;

	@Operation(summary = "팀 조회", description = "**[회원가입]** 팀 이름을 조회합니다.")
	@GetMapping
	public ApiResponse<TeamListResponse> getTeams() {
		TeamListResponse response = teamService.getTeams();
		return ApiResponse.onSuccess("팀 조회 성공", response);
	}

	@Operation(summary = "팀별 팀원 조회", description = "**[회원가입]** 각 팀에 속한 팀원을 조회합니다.")
	@GetMapping("/{teamId}/candidates")
	public ApiResponse<CandidateListResponse> getTeamCandidates(
		@Parameter(description = "팀 ID", example = "1") @PathVariable Long teamId,
		@Parameter(description = "파트 구분 ") @RequestParam Part part
	) {
		CandidateListResponse response = candidateService.getByTeamIdAndPart(teamId, part);

		return ApiResponse.onSuccess("팀별 팀원 조회 성공", response);
	}

	@Operation(summary = "팀 후보 조회", description = "**[팀 투표]** 팀 후보 목록을 조회합니다.")
	@GetMapping("/voting")
	public ApiResponse<TeamDetailListResponse> getTeamsForVoting(
		@AuthenticationPrincipal Long userId
	) {
		TeamDetailListResponse response = teamService.getTeamsForVoting(userId);
		return ApiResponse.onSuccess("투표할 팀 조회 성공", response);
	}

}
