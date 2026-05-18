package com.ceos.vote.domain.vote.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.domain.candidate.service.CandidateService;
import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.service.TeamService;
import com.ceos.vote.domain.user.entity.User;
import com.ceos.vote.domain.user.service.UserService;
import com.ceos.vote.domain.vote.dto.response.CandidateVoteResultListResponse;
import com.ceos.vote.domain.vote.dto.response.CandidateVoteResultListResponse.CandidateVoteResultInfo;
import com.ceos.vote.domain.vote.dto.response.TeamVoteResultListResponse;
import com.ceos.vote.domain.vote.dto.response.TeamVoteResultListResponse.TeamVoteResultInfo;
import com.ceos.vote.domain.vote.entity.CandidateVote;
import com.ceos.vote.domain.vote.entity.TeamVote;
import com.ceos.vote.domain.vote.exception.VoteErrorCode;
import com.ceos.vote.domain.vote.repository.CandidateVoteRepository;
import com.ceos.vote.domain.vote.repository.TeamVoteRepository;
import com.ceos.vote.global.apipayload.exception.GeneralException;
import com.ceos.vote.global.entity.enums.Part;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final CandidateVoteRepository candidateVoteRepository;
	private final TeamVoteRepository teamVoteRepository;
	private final CandidateService candidateService;
	private final TeamService teamService;
	private final UserService userService;

	@Transactional
	public void voteCandidate(Long userId, Long candidateId) {
		// 유저 및 후보자 조회
		User user = userService.getReferenceById(userId);
		Candidate candidate = candidateService.getById(candidateId);

		// 중복 투표 검사
		if (candidateVoteRepository.existsByUserIdAndPart(userId, candidate.getPart())) {
			throw new GeneralException(VoteErrorCode.ALREADY_VOTED);
		}

		// 투표 저장
		try {
			candidateVoteRepository.save(CandidateVote.of(user, candidate));
		} catch (DataIntegrityViolationException e) {
			throw new GeneralException(VoteErrorCode.ALREADY_VOTED);
		}

		// 후보 투표수 증가
		candidateService.incrementVoteCount(candidateId);
	}

	@Transactional
	public void voteTeam(Long userId, Long teamId) {
		// 유저 및 팀 조회
		User user = userService.getReferenceById(userId);
		Team team = teamService.getById(teamId);

		// 중복 투표 검사
		if (teamVoteRepository.existsByUserId(userId)) {
			throw new GeneralException(VoteErrorCode.ALREADY_VOTED);
		}

		// 투표 저장
		try {
			teamVoteRepository.save(TeamVote.of(user, team));
		} catch (DataIntegrityViolationException e) {
			throw new GeneralException(VoteErrorCode.ALREADY_VOTED);
		}

		// 팀 투표수 증가
		teamService.incrementVoteCount(teamId);
	}

	public TeamVoteResultListResponse getTeamVoteResult(Long userId) {

		// 팀 투표수 기준 내림차순
		List<Team> teams = teamService.getRanking();

		// 투표 안했을경우 빈값
		Long myVotedTeamId = teamVoteRepository.findVotedTeamIdByUserId(userId)
			.orElse(null);

		// 투표한 팀 있는지 조회
		List<TeamVoteResultInfo> teamInfos = teams.stream()
			.map(team -> TeamVoteResultInfo.from(
				team,
				Objects.equals(myVotedTeamId, team.getId())
			))
			.toList();

		return TeamVoteResultListResponse.from(teamInfos);
	}

	public CandidateVoteResultListResponse getCandidateVoteResult(Long userId, Part part) {

		// 파트장 후보 투표수 기준 내림차순
		List<Candidate> candidates = candidateService.getRanking(part);

		Set<Long> myVotedCandidateIds = Set.copyOf(
			candidateVoteRepository.findVotedCandidateIdsByUserId(userId)
		);

		List<CandidateVoteResultInfo> candidateInfos = candidates.stream()
			.map(candidate -> CandidateVoteResultInfo.from(
				candidate,
				myVotedCandidateIds.contains(candidate.getId())
			))
			.toList();

		return CandidateVoteResultListResponse.from(candidateInfos);
	}
}
