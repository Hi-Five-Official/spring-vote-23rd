package com.ceos.vote.domain.vote.exception;

import org.springframework.http.HttpStatus;

import com.ceos.vote.global.apipayload.code.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteErrorCode implements BaseErrorCode {

	ALREADY_VOTED(HttpStatus.CONFLICT, "VOTE_409_01", "이미 투표하셨습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
