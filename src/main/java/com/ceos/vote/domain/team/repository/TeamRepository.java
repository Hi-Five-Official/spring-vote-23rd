package com.ceos.vote.domain.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.entity.enums.TeamName;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Optional<Team> findByName(TeamName teamName);
}
