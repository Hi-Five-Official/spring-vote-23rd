package com.ceos.vote.domain.team.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.entity.enums.TeamName;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Optional<Team> findByName(TeamName teamName);

	@Modifying
	@Query("UPDATE Team t "
		+ "SET t.voteCount = t.voteCount + 1 "
		+ "WHERE t.id = :teamId")
	int increaseVoteCount(@Param("teamId") Long teamId);
}
