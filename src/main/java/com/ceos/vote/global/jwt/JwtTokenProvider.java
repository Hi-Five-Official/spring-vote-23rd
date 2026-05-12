package com.ceos.vote.global.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.access-token-expiration}")
	private Long accessTokenExpiration;

	private SecretKey key;

	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64URL.decode(secret);
		key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(Long userId) {
		Date now = new Date();
		Date expiryTime = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
			.subject(userId.toString())
			.issuedAt(now)
			.expiration(expiryTime)
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public Claims validateToken(String token) {
		try {
			return getClaims(token);
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰: {}", e.getMessage());
			throw e;
		} catch (UnsupportedJwtException e) {
			log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
			throw e;
		} catch (MalformedJwtException e) {
			log.warn("잘못된 형식의 JWT 토큰: {}", e.getMessage());
			throw e;
		} catch (SignatureException e) {
			log.warn("JWT 서명 검증 실패: {}", e.getMessage());
			throw e;
		} catch (IllegalArgumentException e) {
			log.warn("JWT 토큰이 비어있음: {}", e.getMessage());
			throw e;
		}
	}

	public LocalDateTime getExpiration(String token) {
		return getClaims(token).getExpiration()
			.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
