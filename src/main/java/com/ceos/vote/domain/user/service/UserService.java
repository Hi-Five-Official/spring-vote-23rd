package com.ceos.vote.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos.vote.domain.user.entity.User;
import com.ceos.vote.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	public User getReferenceById(Long userId) {
		return userRepository.getReferenceById(userId);
	}
}
