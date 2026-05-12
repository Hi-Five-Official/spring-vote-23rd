package com.ceos.vote.domain.team.entity;

import com.ceos.vote.domain.team.entity.enums.TeamName;
import com.ceos.vote.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Team extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "team_name", nullable = false, unique = true, length = 20)
	private TeamName name;

	@Column(name = "description")
	private String description;

	@Column(name = "vote_count", nullable = false)
	private int voteCount = 0;
}
