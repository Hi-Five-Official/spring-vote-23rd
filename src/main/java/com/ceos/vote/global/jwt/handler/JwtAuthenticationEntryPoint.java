package com.ceos.vote.global.jwt.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.ceos.vote.global.apipayload.code.GeneralErrorCode;
import com.ceos.vote.global.apipayload.response.ApiResponse;
import com.ceos.vote.global.jwt.exceptions.JwtErrorType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException, ServletException {

		JwtErrorType errorType = (JwtErrorType)request.getAttribute("exception");

		GeneralErrorCode errorCode = (errorType != null)
			? errorType.getErrorCode()
			: GeneralErrorCode.MISSING_AUTH_INFO;

		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.onFailure(errorCode, null)));
	}
}
