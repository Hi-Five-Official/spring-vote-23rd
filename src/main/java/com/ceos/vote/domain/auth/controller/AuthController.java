package com.ceos.vote.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos.vote.domain.auth.dto.request.CheckEmailRequest;
import com.ceos.vote.domain.auth.dto.request.CheckUsernameRequest;
import com.ceos.vote.domain.auth.dto.request.UserLoginRequest;
import com.ceos.vote.domain.auth.dto.request.UserSignUpRequest;
import com.ceos.vote.domain.auth.dto.response.LoginResponse;
import com.ceos.vote.domain.auth.dto.response.SignUpResponse;
import com.ceos.vote.domain.auth.service.AuthService;
import com.ceos.vote.global.apipayload.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
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
	public ResponseEntity<ApiResponse<Boolean>> checkUsername(
		@Valid @RequestBody CheckUsernameRequest request
	) {
		boolean isAvailable = authService.isUsernameAvailable(request.username());
		return ResponseEntity.ok(ApiResponse.onSuccess("아이디 사용 가능 여부 조회 성공", isAvailable));
	}

	@Operation(summary = "이메일 중복 확인", description = "사용 가능한 이메일인지 확인합니다.")
	@PostMapping("/check-email")
	public ResponseEntity<ApiResponse<Boolean>> checkEmail(
		@Valid @RequestBody CheckEmailRequest request
	) {
		boolean isAvailable = authService.isEmailAvailable(request.email());
		return ResponseEntity.ok(ApiResponse.onSuccess("이메일 사용 가능 여부 조회 성공", isAvailable));
	}

	@Operation(summary = "회원가입", description = "회원가입 후 자동 로그인 처리됩니다.")
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signup(
		@Valid @RequestBody UserSignUpRequest request,
		HttpServletResponse response
	) {
		SignUpResponse result = authService.signup(request, response);
		return ResponseEntity.ok(ApiResponse.onSuccess("회원가입 성공", result));
	}

	@Operation(summary = "로그인", description = "아이디/비밀번호로 로그인합니다.")
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(
		@Valid @RequestBody UserLoginRequest request,
		HttpServletResponse response
	) {
		LoginResponse result = authService.login(request, response);
		return ResponseEntity.ok(ApiResponse.onSuccess("로그인 성공", result));
	}

	@Operation(summary = "로그아웃", description = "쿠키를 만료시켜 로그아웃 처리합니다.")
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
		authService.logout(response);
		return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃 성공"));
	}

}
