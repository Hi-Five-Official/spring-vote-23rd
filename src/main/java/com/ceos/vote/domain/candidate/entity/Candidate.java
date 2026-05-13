package com.ceos.vote.domain.candidate.entity;

import com.ceos.vote.domain.user.entity.enums.Part;
import com.ceos.vote.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidates")
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
	@Column(name = "candidate_part", nullable = false, length = 20)
	private Part part;

	@Column(name = "vote_count", nullable = false)
	private int voteCount = 0;
}
