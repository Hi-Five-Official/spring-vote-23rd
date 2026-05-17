package com.ceos.vote.domain.vote.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.domain.candidate.service.CandidateService;
import com.ceos.vote.domain.user.entity.User;
import com.ceos.vote.domain.user.service.UserService;
import com.ceos.vote.domain.vote.entity.CandidateVote;
import com.ceos.vote.domain.vote.exception.VoteErrorCode;
import com.ceos.vote.domain.vote.repository.CandidateVoteRepository;
import com.ceos.vote.global.apipayload.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final CandidateVoteRepository candidateVoteRepository;
	private final CandidateService candidateService;
	private final UserService userService;

	@Transactional
	public void voteCandidate(Long userId, Long candidateId) {
		// 유저 및 후보자 조회
		User user = userService.getReferenceById(userId);
		Candidate candidate = candidateService.getById(candidateId);

		// 중복 투표 검사
		if (candidateVoteRepository.existsByUserIdAndPart(user.getId(), candidate.getPart())) {
			throw new GeneralException(VoteErrorCode.ALREADY_VOTED);
		}

		// 투표 저장
		candidateVoteRepository.save(CandidateVote.of(user, candidate));

		// 후보 투표수 증가
		candidateService.incrementVoteCount(candidateId);
	}
}
