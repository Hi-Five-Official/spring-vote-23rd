package com.ceos.vote.domain.vote.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.vote.dto.request.CandidateVoteRequest;
import com.ceos.vote.domain.vote.dto.request.TeamVoteRequest;
import com.ceos.vote.domain.vote.service.VoteService;
import com.ceos.vote.global.apipayload.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/votes")
@Tag(name = "Vote", description = "투표 관련 API")
public class VoteController {

	private final VoteService voteService;

	@Operation(summary = "파트장 투표", description = "FE/BE 각 파트의 파트장을 투표합니다. (FE/BE 1표씩 가능)")
	@PostMapping("/candidates")
	public ApiResponse<Void> voteCandidate(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody CandidateVoteRequest request
	) {
		voteService.voteCandidate(userId, request.candidateId());
		return ApiResponse.onSuccess("파트장 투표 성공");
	}

	@Operation(summary = "데모데이 팀 투표", description = "데모데이 팀을 투표합니다. (1인 1팀)")
	@PostMapping("/teams")
	public ApiResponse<Void> voteTeam(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody TeamVoteRequest request
	) {
		voteService.voteTeam(userId, request.teamId());
		return ApiResponse.onSuccess("팀 투표 성공");
	}
}
