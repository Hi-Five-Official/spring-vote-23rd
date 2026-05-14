package com.ceos.vote.domain.auth.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.auth.dto.AuthResult;
import com.ceos.vote.domain.auth.dto.request.LoginRequest;
import com.ceos.vote.domain.auth.dto.request.SignUpRequest;
import com.ceos.vote.domain.auth.entity.RefreshToken;
import com.ceos.vote.domain.auth.repository.RefreshTokenRepository;
import com.ceos.vote.domain.team.entity.Team;
import com.ceos.vote.domain.team.exception.TeamErrorCode;
import com.ceos.vote.domain.team.repository.TeamRepository;
import com.ceos.vote.domain.user.entity.User;
import com.ceos.vote.domain.user.exception.UserErrorCode;
import com.ceos.vote.domain.user.repository.UserRepository;
import com.ceos.vote.global.apipayload.code.GeneralErrorCode;
import com.ceos.vote.global.apipayload.exception.GeneralException;
import com.ceos.vote.global.jwt.JwtTokenProvider;
import com.ceos.vote.global.jwt.utils.CookieUtils;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieUtils cookieUtils;

	@Transactional(readOnly = true)
	public void validateUsernameAvailable(String username) {

		if (userRepository.existsByUsername(username)) {
			throw new GeneralException(UserErrorCode.USERNAME_ALREADY_EXISTS);
		}
	}

	@Transactional(readOnly = true)
	public void validateEmailAvailable(String email) {

		if (userRepository.existsByEmail(email)) {
			throw new GeneralException(UserErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}

	@Transactional
	public AuthResult signup(SignUpRequest request) {

		validateUsernameAvailable(request.username());
		validateEmailAvailable(request.email());

		Team team = teamRepository.findByName(request.team())
			.orElseThrow(() -> new GeneralException(TeamErrorCode.TEAM_NOT_FOUND));

		User user = User.createUser(
			team,
			request.username(),
			passwordEncoder.encode(request.password()),
			request.name(),
			request.email(),
			request.part()
		);

		User saved = userRepository.save(user);

		return issueTokens(saved.getId());
	}

	@Transactional
	public AuthResult login(LoginRequest request) {

		User user = userRepository.findByUsername(request.username())
			.orElseThrow(() -> new GeneralException(GeneralErrorCode.INVALID_LOGIN));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new GeneralException(GeneralErrorCode.INVALID_LOGIN);
		}

		return issueTokens(user.getId());
	}

	@Transactional
	public ResponseCookie logout(Long userId) {

		refreshTokenRepository.deleteByUserId(userId);
		return cookieUtils.deleteRefreshTokenCookie();
	}

	@Transactional
	public AuthResult reissue(String refreshToken) {

		if (refreshToken == null || refreshToken.isBlank()) {
			throw new GeneralException(GeneralErrorCode.INVALID_TOKEN);
		}

		Long userId;
		try {
			userId = Long.parseLong(
				jwtTokenProvider.validateRefreshToken(refreshToken).getSubject());
		} catch (JwtException | IllegalArgumentException exception) {
			throw new GeneralException(GeneralErrorCode.INVALID_TOKEN);
		}

		RefreshToken stored = refreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> new GeneralException(GeneralErrorCode.INVALID_TOKEN));

		if (!stored.getToken().equals(refreshToken)) {
			throw new GeneralException(GeneralErrorCode.INVALID_TOKEN);
		}

		return issueTokens(userId);
	}

	private AuthResult issueTokens(Long userId) {

		String accessToken = jwtTokenProvider.generateAccessToken(userId);
		String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

		LocalDateTime expiresAt = jwtTokenProvider.getExpiration(refreshToken);

		refreshTokenRepository.findByUserId(userId)
			.ifPresentOrElse(
				rt -> rt.rotate(refreshToken, expiresAt),
				() -> refreshTokenRepository.save(
					RefreshToken.createRefreshToken(userRepository.getReferenceById(userId), refreshToken, expiresAt))
			);

		ResponseCookie cookie = cookieUtils.createRefreshTokenCookie(refreshToken);
		return new AuthResult(userId, accessToken, cookie);
	}
}
