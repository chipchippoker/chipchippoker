package com.chipchippoker.backend.api.auth.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.auth.model.dto.LoginRequest;
import com.chipchippoker.backend.api.auth.model.dto.LoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.SignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.SignupResponse;
import com.chipchippoker.backend.api.auth.service.AuthService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	public ResponseEntity<ApiResponse<Boolean>> isDuplicateMemberId(String memberId) {
		return ResponseEntity.ok(ApiResponse.success(authService.isDuplicateMemberId(memberId)));
	}

	@PostMapping("/duplication/nickname")
	public ResponseEntity<ApiResponse<Boolean>> isDuplicateNickname(String nickname) {
		return ResponseEntity.ok(ApiResponse.success(authService.isDuplicateNickname(nickname)));
	}
}
