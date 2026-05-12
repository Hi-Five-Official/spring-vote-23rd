package com.ceos.vote.domain.auth.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.auth.dto.request.UserLoginRequest;
import com.ceos.vote.domain.auth.dto.request.UserSignUpRequest;
import com.ceos.vote.domain.auth.dto.response.LoginResponse;
import com.ceos.vote.domain.auth.dto.response.SignUpResponse;
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

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieUtils cookieUtils;

	@Transactional(readOnly = true)
	public boolean isUsernameAvailable(String username) {

		return !userRepository.existsByUsername(username);
	}

	@Transactional(readOnly = true)
	public boolean isEmailAvailable(String email) {

		return !userRepository.existsByEmail(email);
	}

	@Transactional
	public SignUpResponse signup(UserSignUpRequest request, HttpServletResponse response) {

		validateUsernameAndEmail(request.username(), request.email());

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

		issueAccessTokenCookie(saved.getId(), response);

		return SignUpResponse.from(saved);
	}

	@Transactional(readOnly = true)
	public LoginResponse login(UserLoginRequest request, HttpServletResponse response) {

		User user = userRepository.findByUsername(request.username())
			.orElseThrow(() -> new GeneralException(GeneralErrorCode.INVALID_LOGIN));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new GeneralException(GeneralErrorCode.INVALID_LOGIN);
		}

		issueAccessTokenCookie(user.getId(), response);

		return LoginResponse.from(user);
	}

	public void logout(HttpServletResponse response) {

		ResponseCookie cookie = cookieUtils.deleteAccessTokenCookie();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	private void validateUsernameAndEmail(String username, String email) {
		if (userRepository.existsByUsername(username)) {
			throw new GeneralException(UserErrorCode.USERNAME_ALREADY_EXISTS);
		}

		if (userRepository.existsByEmail(email)) {
			throw new GeneralException(UserErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}

	private void issueAccessTokenCookie(Long userId, HttpServletResponse response) {

		String token = jwtTokenProvider.generateAccessToken(userId);
		ResponseCookie cookie = cookieUtils.createAccessTokenCookie(token);
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
