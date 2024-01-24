package com.chipchippoker.backend.api.auth.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.auth.model.dto.Token;
import com.chipchippoker.backend.api.auth.model.dto.request.LoginRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SimpleSignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.response.AuthorizationCodeAlreadyAccountResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.AuthorizationCodeMoreInformationResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.AuthorizationInformationResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.LoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SignupResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleLoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleSignupResponse;
import com.chipchippoker.backend.api.auth.service.AuthService;
import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Auth API Controller", description = "인증 인가를 처리하는 Auth Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody SignupRequest signupRequest) throws
		NoSuchAlgorithmException {
		return ResponseEntity.ok(ApiResponse.success(authService.signup(signupRequest)));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) throws
		NoSuchAlgorithmException {
		return ResponseEntity.ok(ApiResponse.success(authService.login(loginRequest)));
	}

	@PostMapping("/duplication/id")
	@Operation(operationId = "IsDuplicateId", summary = "아이디 중복 조회", description = "사용자 아이디가 중복인지 조회한다.")
	public ResponseEntity<ApiResponse<Boolean>> isDuplicateMemberId(
		@ModelAttribute(value = "memberId") String memberId) {
		return ResponseEntity.ok(ApiResponse.success(authService.isDuplicateMemberId(memberId)));
	}

	@PostMapping("/duplication/nickname")
	public ResponseEntity<ApiResponse<Boolean>> isDuplicateNickname(
		@ModelAttribute(value = "memberId") String nickname) {
		return ResponseEntity.ok(ApiResponse.success(authService.isDuplicateNickname(nickname)));
	}

	@Operation(summary = "인가코드 전달")
	@PostMapping("/authorization")
	public ResponseEntity<ApiResponse<AuthorizationInformationResponse>> passAuthorizationCode(
		String authorizationCode) {
		if (authorizationCode == null || authorizationCode.isEmpty()) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTHORIZATION_CODE);
		}
		Token token = authService.getToken(authorizationCode);
		// 3 인가 코드로 발급이 불가능한 경우
		if (token == null) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTH_TOKEN);
		} else {
			// 인가 코드로 토큰을 발급 받았음
			Long kakaoSocialId = authService.getKakaoSocialId(token.getAccess_token());

			// 이미 계정이 있는 유저
			if (authService.validateExistsUser(kakaoSocialId)) {
				// 로그인을 진행한다.
				SimpleLoginResponse loginResponse = authService.kakaoLogin(token);
				AuthorizationCodeAlreadyAccountResponse authorizationCodeAlreadyAccountResponse = new AuthorizationCodeAlreadyAccountResponse(
					200, "로그인을 완료했습니다.",
					loginResponse.getAccessToken(), loginResponse.getRefreshToken(), loginResponse.getNickname());
				return new ResponseEntity<>(ApiResponse.success(authorizationCodeAlreadyAccountResponse),
					HttpStatus.valueOf(200));
			} else {
				// 계정이 없는 유저
				AuthorizationCodeMoreInformationResponse authorizationCodeMoreInformationResponse = new AuthorizationCodeMoreInformationResponse(
					202, "추가 정보가 필요합니다.", token.getAccess_token());
				return new ResponseEntity<>(ApiResponse.success(authorizationCodeMoreInformationResponse),
					HttpStatus.valueOf(202));
			}
		}
	}

	@Operation(summary = "소셜 회원가입")
	@PostMapping("/social-signup")
	public ResponseEntity<ApiResponse<SimpleSignupResponse>> signUp(
		@Valid @RequestBody SimpleSignupRequest simpleSignupRequest,
		@RequestHeader(value = "kakao-access-token") String token) throws Exception {
		SimpleSignupResponse socialSignUpResponse = authService.signUp(simpleSignupRequest, token);
		return ResponseEntity.ok(ApiResponse.success(socialSignUpResponse));
	}

}
