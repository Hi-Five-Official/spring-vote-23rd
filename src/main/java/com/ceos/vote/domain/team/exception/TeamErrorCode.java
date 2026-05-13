package com.ceos.vote.domain.team.exception;

import org.springframework.http.HttpStatus;

import com.ceos.vote.global.apipayload.code.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements BaseErrorCode {

	TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM_404_01", "존재하지 않는 팀입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
