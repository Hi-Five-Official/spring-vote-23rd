package com.ceos.vote.domain.candidate.entity;

import com.ceos.vote.domain.candidate.entity.enums.University;
import com.ceos.vote.domain.team.entity.Team;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "candidates",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_candidate_team_part_name",
		columnNames = {"team_id", "candidate_part", "name"}
	))
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Candidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "candidate_id")
	private Long id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "university", nullable = false, length = 20)
	private University university;

	@Enumerated(EnumType.STRING)
	@Column(name = "candidate_part", nullable = false, length = 20)
	private Part part;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@Column(name = "vote_count", nullable = false)
	private int voteCount = 0;
}
