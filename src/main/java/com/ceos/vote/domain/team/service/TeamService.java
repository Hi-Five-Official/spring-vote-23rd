package com.ceos.vote.domain.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.team.dto.response.TeamResponse;
import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

	private final TeamRepository teamRepository;

	public TeamResponse getTeams() {
		List<Team> teams = teamRepository.findAll();

		return TeamResponse.from(teams);
	}
}
