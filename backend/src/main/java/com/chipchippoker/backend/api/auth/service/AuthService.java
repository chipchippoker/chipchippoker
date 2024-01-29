package com.chipchippoker.backend.api.auth.service;

import java.security.NoSuchAlgorithmException;

import com.chipchippoker.backend.api.auth.model.dto.Token;
import com.chipchippoker.backend.api.auth.model.dto.request.LoginRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SimpleSignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.response.LoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SignupResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleLoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleSignupResponse;

public interface AuthService {
	SignupResponse signup(SignupRequest signupRequest) throws NoSuchAlgorithmException;

	Boolean isDuplicateMemberId(String memberId);

	Boolean isDuplicateNickname(String nickname);

	LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException;

	SimpleLoginResponse kakaoLogin(Token token);

	Token getToken(String authorizationCode);

	SimpleSignupResponse signUp(SimpleSignupRequest request, String token) throws Exception;

	Long getKakaoSocialId(String accessToken);

	Boolean validateExistsUser(Long socialId);

	void socialConnection(Long id, Long kakaoSocialId);

	String encryptionToken(String accessToken);
}
