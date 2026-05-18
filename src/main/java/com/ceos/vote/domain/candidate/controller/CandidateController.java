package com.ceos.vote.domain.candidate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.candidate.dto.response.CandidateDetailListResponse;
import com.ceos.vote.domain.candidate.service.CandidateService;
import com.ceos.vote.global.apipayload.response.ApiResponse;
import com.ceos.vote.global.entity.enums.Part;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candidates")
@Tag(name = "Candidate", description = "후보 관련 API")
public class CandidateController {

	private final CandidateService candidateService;

	@Operation(summary = "파트장 후보 조회", description = "**[파트장 투표]** 파트장 후보의 상세 내용을 조회합니다.")
	@GetMapping("/voting")
	public ApiResponse<CandidateDetailListResponse> getCandidatesForVoting(
		@AuthenticationPrincipal Long userId,
		@Parameter(description = "파트 구분") @RequestParam(required = false) Part part
	) {

		CandidateDetailListResponse response = candidateService.getCandidatesForVoting(userId, part);

		return ApiResponse.onSuccess("파트장 후보 상세 내용 조회 성공", response);
	}
}
