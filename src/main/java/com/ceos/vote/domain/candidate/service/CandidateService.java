package com.ceos.vote.domain.candidate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.candidate.dto.response.CandidateListResponse;
import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.domain.candidate.exception.CandidateErrorCode;
import com.ceos.vote.domain.candidate.repository.CandidateRepository;
import com.ceos.vote.domain.team.service.TeamService;
import com.ceos.vote.global.apipayload.exception.GeneralException;
import com.ceos.vote.global.entity.enums.Part;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateService {

	private final CandidateRepository candidateRepository;
	private final TeamService teamService;

	public CandidateListResponse getByTeamIdAndPart(Long teamId, Part part) {

		// 존재하는 팀ID인지 검증
		teamService.validateTeamExists(teamId);

		List<Candidate> candidates = candidateRepository.findByTeamIdAndPart(teamId, part);

		return CandidateListResponse.from(candidates);
	}

	public Candidate getById(Long candidateId) {
		return candidateRepository.findById(candidateId)
			.orElseThrow(() -> new GeneralException(CandidateErrorCode.CANDIDATE_NOT_FOUND));
	}

	@Transactional
	public void incrementVoteCount(Long candidateId) {
		candidateRepository.increaseVoteCount(candidateId);
	}
}
