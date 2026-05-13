package com.ceos.vote.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.auth.dto.AuthResult;
import com.ceos.vote.domain.auth.dto.request.CheckEmailRequest;
import com.ceos.vote.domain.auth.dto.request.CheckUsernameRequest;
import com.ceos.vote.domain.auth.dto.request.LoginRequest;
import com.ceos.vote.domain.auth.dto.request.SignUpRequest;
import com.ceos.vote.domain.auth.dto.response.LoginResponse;
import com.ceos.vote.domain.auth.dto.response.ReissueResponse;
import com.ceos.vote.domain.auth.dto.response.SignUpResponse;
import com.ceos.vote.domain.auth.service.AuthService;
import com.ceos.vote.global.apipayload.response.ApiResponse;
import com.ceos.vote.global.jwt.utils.CookieUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "회원 인증 API")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "아이디 중복 확인", description = "사용 가능한 아이디인지 확인합니다.")
	@PostMapping("/check-username")
	public ApiResponse<Void> checkUsername(
		@Valid @RequestBody CheckUsernameRequest request
	) {

		authService.validateUsernameAvailable(request.username());
		return ApiResponse.onSuccess("아이디 사용 가능 여부 조회 성공");
	}

	@Operation(summary = "이메일 중복 확인", description = "사용 가능한 이메일인지 확인합니다.")
	@PostMapping("/check-email")
	public ApiResponse<Void> checkEmail(
		@Valid @RequestBody CheckEmailRequest request
	) {

		authService.validateEmailAvailable(request.email());
		return ApiResponse.onSuccess("이메일 사용 가능 여부 조회 성공");
	}

	@Operation(summary = "회원가입", description = "회원가입 후 자동 로그인 처리됩니다.")
	@PostMapping("/signup")
	public ApiResponse<SignUpResponse> signup(
		@Valid @RequestBody SignUpRequest request,
		HttpServletResponse response
	) {

		AuthResult result = authService.signup(request);
		response.addHeader(HttpHeaders.SET_COOKIE, result.refreshTokenCookie().toString());

		return ApiResponse.onSuccess("회원가입 성공",
			SignUpResponse.of(result.accessToken()));
	}

	@Operation(summary = "로그인", description = "아이디/비밀번호로 로그인합니다.")
	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(
		@Valid @RequestBody LoginRequest request,
		HttpServletResponse response
	) {

		AuthResult result = authService.login(request);
		response.addHeader(HttpHeaders.SET_COOKIE, result.refreshTokenCookie().toString());

		return ApiResponse.onSuccess("로그인 성공",
			LoginResponse.of(result.userId(), result.accessToken()));
	}

	@Operation(summary = "로그아웃", description = "쿠키를 만료시켜 로그아웃 처리합니다.")
	@PostMapping("/logout")
	public ApiResponse<Void> logout(
		@Parameter(hidden = true)
		@CookieValue(value = CookieUtils.REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
		HttpServletResponse response
	) {

		ResponseCookie deleted = authService.logout(refreshToken);
		response.addHeader(HttpHeaders.SET_COOKIE, deleted.toString());

		return ApiResponse.onSuccess("로그아웃 성공");
	}

	@Operation(summary = "토큰 재발급", description = "refresh 토큰으로 새 access 토큰을 발급받습니다.")
	@PostMapping("/refresh")
	public ApiResponse<ReissueResponse> refresh(
		@Parameter(hidden = true)
		@CookieValue(value = CookieUtils.REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
		HttpServletResponse response
	) {
		AuthResult result = authService.reissue(refreshToken);
		response.addHeader(HttpHeaders.SET_COOKIE, result.refreshTokenCookie().toString());

		return ApiResponse.onSuccess("토큰 재발급 성공",
			ReissueResponse.of(result.accessToken()));
	}
}
