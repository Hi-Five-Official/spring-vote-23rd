package com.ceos.vote.domain.candidate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.global.entity.enums.Part;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	List<Candidate> findByTeamIdAndPart(Long teamId, Part part);
}
