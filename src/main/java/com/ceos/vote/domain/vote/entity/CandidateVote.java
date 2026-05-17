package com.ceos.vote.domain.vote.entity;

import com.ceos.vote.domain.candidate.entity.Candidate;
import com.ceos.vote.domain.user.entity.User;
import com.ceos.vote.global.entity.BaseEntity;
import com.ceos.vote.global.entity.enums.Part;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "candidate_votes",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_candidate_vote_user_part",
		columnNames = {"user_id", "part"}
	))
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidateVote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "candidate_vote_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidate_id", nullable = false)
	private Candidate candidate;

	@Enumerated(EnumType.STRING)
	@Column(name = "part", nullable = false, length = 10)
	private Part part;

	public static CandidateVote of(User user, Candidate candidate) {
		return CandidateVote.builder()
			.user(user)
			.candidate(candidate)
			.part(candidate.getPart())
			.build();
	}
}
