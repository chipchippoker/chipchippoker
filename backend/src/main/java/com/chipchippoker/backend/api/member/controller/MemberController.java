package com.chipchippoker.backend.api.member.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.member.model.dto.LoginRequest;
import com.chipchippoker.backend.api.member.model.dto.LoginResponse;
import com.chipchippoker.backend.api.member.model.dto.SignupRequest;
import com.chipchippoker.backend.api.member.model.dto.SignupResponse;
import com.chipchippoker.backend.api.member.service.MemberService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Member API Controller", description = "API 정보를 제공하는 멤버 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
@Slf4j
public class MemberController {
	private final HttpServletRequest request;
	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody SignupRequest signupRequest) throws
		NoSuchAlgorithmException {
		return ResponseEntity.ok(ApiResponse.success(memberService.signup(signupRequest)));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) throws
		NoSuchAlgorithmException {
		return ResponseEntity.ok(ApiResponse.success(memberService.login(loginRequest)));
	}

	@PostMapping("/duplication/id")
	@Operation(operationId = "IsDuplicateId", summary = "아이디 중복 조회", description = "사용자 아이디가 중복인지 조회한다.")
	public ResponseEntity<ApiResponse<Boolean>> isDuplicateMemberId(String memberId) {
		return ResponseEntity.ok(ApiResponse.success(memberService.isDuplicateMemberId(memberId)));
	}

	@PostMapping("/duplication/nickname")
	public ResponseEntity<ApiResponse<Boolean>> isDuplicateNickname(String nickname) {
		return ResponseEntity.ok(ApiResponse.success(memberService.isDuplicateNickname(nickname)));
	}

	@GetMapping("/jwt-test")
	public String jwtTest() {
		return "jwtTest 요청 성공";
	}
}

