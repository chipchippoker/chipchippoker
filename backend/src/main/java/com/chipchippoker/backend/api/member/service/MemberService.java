package com.chipchippoker.backend.api.member.service;

import java.security.NoSuchAlgorithmException;

import com.chipchippoker.backend.api.member.model.dto.LoginRequest;
import com.chipchippoker.backend.api.member.model.dto.LoginResponse;
import com.chipchippoker.backend.api.member.model.dto.SignupRequest;
import com.chipchippoker.backend.api.member.model.dto.SignupResponse;

public interface MemberService {
	SignupResponse signup(SignupRequest signupRequest) throws NoSuchAlgorithmException;

	Boolean isDuplicateMemberId(String memberId);

	Boolean isDuplicateNickname(String nickname);

	LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException;

	void updateRefreshToken(String userEmail, String refreshToken);
}
