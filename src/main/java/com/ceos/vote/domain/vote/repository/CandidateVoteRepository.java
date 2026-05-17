package com.ceos.vote.domain.vote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceos.vote.domain.vote.entity.CandidateVote;
import com.ceos.vote.global.entity.enums.Part;

public interface CandidateVoteRepository extends JpaRepository<CandidateVote, Long> {

	boolean existsByUserIdAndPart(Long userId, Part part);

	@Query("SELECT cv.candidate.id "
		+ "FROM CandidateVote cv "
		+ "WHERE cv.user.id = :userId")
	List<Long> findVotedCandidateIdsByUserId(@Param("userId") Long userId);
}
