package com.chipchippoker.backend.api.auth.service;

import java.security.NoSuchAlgorithmException;

import com.chipchippoker.backend.api.auth.model.dto.LoginRequest;
import com.chipchippoker.backend.api.auth.model.dto.LoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.SignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.SignupResponse;

public interface AuthService {
	SignupResponse signup(SignupRequest signupRequest) throws NoSuchAlgorithmException;

	Boolean isDuplicateMemberId(String memberId);

	Boolean isDuplicateNickname(String nickname);

	LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException;
}
