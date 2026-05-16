package com.ceos.vote.domain.candidate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.candidate.dto.response.CandidateListResponse;
import com.ceos.vote.domain.candidate.service.CandidateService;
import com.ceos.vote.global.apipayload.response.ApiResponse;
import com.ceos.vote.global.entity.enums.Part;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Candidate", description = "후보자 관련 API")
public class CandidateController {

	private final CandidateService candidateService;

	@Operation(summary = "팀별 팀원 조회", description = "각 팀에 속한 팀원을 조회합니다.")
	@GetMapping("/teams/{teamId}/candidates")
	public ApiResponse<CandidateListResponse> getTeamCandidates(
		@PathVariable Long teamId,
		@RequestParam Part part
	) {
		CandidateListResponse response = candidateService.getByTeamIdAndPart(teamId, part);

		return ApiResponse.onSuccess("팀별 팀원 조회 성공", response);
	}
}
