package com.ceos.vote.domain.candidate.exception;

import org.springframework.http.HttpStatus;

import com.ceos.vote.global.apipayload.code.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CandidateErrorCode implements BaseErrorCode {

	CANDIDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "CANDIDATE_404_01", "존재하지 않는 후보입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
