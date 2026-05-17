package com.ceos.vote.domain.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.team.dto.response.TeamDetailListResponse;
import com.ceos.vote.domain.team.dto.response.TeamListResponse;
import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.exception.TeamErrorCode;
import com.ceos.vote.domain.team.repository.TeamRepository;
import com.ceos.vote.domain.vote.repository.TeamVoteRepository;
import com.ceos.vote.global.apipayload.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

	private final TeamRepository teamRepository;
	private final TeamVoteRepository teamVoteRepository;

	public TeamListResponse getTeams() {
		List<Team> teams = teamRepository.findAll();

		return TeamListResponse.from(teams);
	}

	public void validateTeamExists(Long teamId) {
		if (!teamRepository.existsById(teamId)) {
			throw new GeneralException(TeamErrorCode.TEAM_NOT_FOUND);
		}
	}

	public Team getById(Long teamId) {
		return teamRepository.findById(teamId)
			.orElseThrow(() -> new GeneralException(TeamErrorCode.TEAM_NOT_FOUND));
	}

	@Transactional
	public void incrementVoteCount(Long teamId) {
		teamRepository.increaseVoteCount(teamId);
	}

	public List<Team> getRanking() {
		return teamRepository.findAllByOrderByVoteCountDescIdAsc();
	}

	// 투표할 팀 목록 조회
	public TeamDetailListResponse getTeamsForVoting(Long userId) {

		// 전체 팀 조회
		List<Team> teams = teamRepository.findAll();

		// 유저가 이미 투표한 팀 id 조회
		Long myVotedTeamId = teamVoteRepository.findVotedTeamIdByUserId(userId)
			.orElse(null);

		return TeamDetailListResponse.from(teams, myVotedTeamId);
	}

}
