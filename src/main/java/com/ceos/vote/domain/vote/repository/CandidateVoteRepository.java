package com.ceos.vote.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos.vote.domain.vote.entity.CandidateVote;
import com.ceos.vote.global.entity.enums.Part;

public interface CandidateVoteRepository extends JpaRepository<CandidateVote, Long> {

	boolean existsByUserIdAndPart(Long userId, Part part);
}
