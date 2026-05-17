package com.ceos.vote.domain.candidate.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.candidate.dto.response.CandidateDetailListResponse;
import com.ceos.vote.domain.candidate.dto.response.CandidateDetailListResponse.CandidateDetailInfo;
import com.ceos.vote.domain.candidate.dto.response.CandidateListResponse;
import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.domain.candidate.exception.CandidateErrorCode;
import com.ceos.vote.domain.candidate.repository.CandidateRepository;
import com.ceos.vote.domain.team.service.TeamService;
import com.ceos.vote.domain.vote.repository.CandidateVoteRepository;
import com.ceos.vote.global.apipayload.exception.GeneralException;
import com.ceos.vote.global.entity.enums.Part;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateService {

	private final CandidateRepository candidateRepository;
	private final TeamService teamService;
	private final CandidateVoteRepository candidateVoteRepository;

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

	public CandidateDetailListResponse getCandidatesForVoting(Long userId, Part part) {

		// 파트 확인. 파트 없을 시 전체 조회
		List<Candidate> candidates = (part == null)
			? candidateRepository.findAllByOrderByIdAsc()
			: candidateRepository.findByPartOrderByIdAsc(part);

		// 유저가 이미 투표한 후보 id 조회
		Set<Long> myVotedCandidateIds = Set.copyOf(
			candidateVoteRepository.findVotedCandidateIdsByUserId(userId)
		);

		List<CandidateDetailInfo> candidateInfos = candidates.stream()
			.map(candidate -> CandidateDetailInfo.from(
				candidate,
				myVotedCandidateIds.contains(candidate.getId())
			))
			.toList();

		return CandidateDetailListResponse.from(candidateInfos);
	}

	public List<Candidate> getRanking() {
		return candidateRepository.findAllByOrderByVoteCountDescIdAsc();
	}
}
