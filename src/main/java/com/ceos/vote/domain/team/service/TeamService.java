package com.ceos.vote.domain.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.team.dto.response.TeamListResponse;
import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.exception.TeamErrorCode;
import com.ceos.vote.domain.team.repository.TeamRepository;
import com.ceos.vote.global.apipayload.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

	private final TeamRepository teamRepository;

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
}
