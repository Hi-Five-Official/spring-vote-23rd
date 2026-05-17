package com.ceos.vote.domain.vote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceos.vote.domain.vote.entity.TeamVote;

public interface TeamVoteRepository extends JpaRepository<TeamVote, Long> {

	boolean existsByUserId(Long userId);

	@Query("SELECT tv.team.id "
		+ "FROM TeamVote tv "
		+ "WHERE tv.user.id = :userId")
	Optional<Long> findVotedTeamIdByUserId(@Param("userId") Long userId);
}
