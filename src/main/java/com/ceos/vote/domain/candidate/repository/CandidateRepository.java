package com.ceos.vote.domain.candidate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.global.entity.enums.Part;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	List<Candidate> findByTeamIdAndPart(Long teamId, Part part);

	List<Candidate> findByPart(Part part);

	@Modifying
	@Query("UPDATE Candidate c "
		+ "SET c.voteCount = c.voteCount + 1 "
		+ "WHERE  c.id = :candidateId")
	int increaseVoteCount(@Param("candidateId") Long candidateId);
}
