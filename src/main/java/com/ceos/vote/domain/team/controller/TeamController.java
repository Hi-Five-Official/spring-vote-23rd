package com.ceos.vote.domain.team.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.team.dto.response.TeamResponse;
import com.ceos.vote.domain.team.service.TeamService;
import com.ceos.vote.global.apipayload.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@Tag(name = "Team", description = "팀 관련 API")
public class TeamController {

	private final TeamService teamService;

	@Operation(summary = "팀 조회", description = "팀 이름을 조회합니다.")
	@GetMapping
	public ApiResponse<TeamResponse> getTeams() {
		TeamResponse response = teamService.getTeams();
		return ApiResponse.onSuccess("팀 조회 성공", response);
	}

}
