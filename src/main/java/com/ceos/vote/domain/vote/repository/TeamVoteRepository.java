package com.ceos.vote.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos.vote.domain.vote.entity.TeamVote;

public interface TeamVoteRepository extends JpaRepository<TeamVote, Long> {

	boolean existsByUserId(Long userId);
}
