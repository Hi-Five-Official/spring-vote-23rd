package com.ceos.vote.domain.user.entity;

import com.ceos.vote.domain.team.entity.Team;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@Column(name = "username", nullable = false, unique = true, length = 20)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "part", nullable = false)
	private Part part;

	public static User createUser(Team team, String username, String password, String name, String email, Part part) {
		return User.builder()
			.team(team)
			.username(username)
			.password(password)
			.name(name)
			.email(email)
			.part(part)
			.build();
	}
}
