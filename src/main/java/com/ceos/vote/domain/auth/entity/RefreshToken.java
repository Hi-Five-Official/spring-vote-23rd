package com.ceos.vote.domain.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "refresh_token", nullable = false, length = 512)
	private String token;

	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;

	public RefreshToken(Long userId, String token, LocalDateTime expiresAt) {
		this.userId = userId;
		this.token = token;
		this.expiresAt = expiresAt;
	}

	public static RefreshToken of(Long userId, String token, LocalDateTime expiresAt) {
		return new RefreshToken(userId, token, expiresAt);
	}

	public void rotate(String newToken, LocalDateTime newExpiresAt) {
		this.token = newToken;
		this.expiresAt = newExpiresAt;
	}
}
