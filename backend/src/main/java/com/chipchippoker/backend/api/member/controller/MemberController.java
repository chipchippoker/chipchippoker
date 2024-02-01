package com.chipchippoker.backend.api.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.auth.model.dto.Token;
import com.chipchippoker.backend.api.auth.model.dto.request.AuthorizationCodeRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SimpleSignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleSignupResponse;
import com.chipchippoker.backend.api.auth.service.AuthService;
import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.UpdateIconRequest;
import com.chipchippoker.backend.api.member.service.MemberService;
import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.util.jwt.dto.TokenResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Member API Controller", description = "API 정보를 제공하는 멤버 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
@Slf4j
public class MemberController {
	private final AuthService authService;
	private final MemberService memberService;
	private final HttpServletRequest httpServletRequest;

	@GetMapping("/jwt-test")
	public String jwtTest() {
		return "jwtTest 요청 성공";
	}

	@Operation(summary = "소셜 연동하기")
	@PostMapping("/social")
	public ResponseEntity<ApiResponse<Void>> socialConnection(
		@RequestBody AuthorizationCodeRequest authorizationCodeRequest) {
		String authorizationCode = authorizationCodeRequest.getAuthorizationCode();
		if (authorizationCode == null || authorizationCode.isEmpty()) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTHORIZATION_CODE);
		}
		Token token = authService.getToken(authorizationCode);
		if (token == null) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTH_TOKEN);
		} else {
			Long kakaoSocialId = authService.getKakaoSocialId(token.getAccess_token());
			authService.socialConnection((Long)httpServletRequest.getAttribute("id"), kakaoSocialId);
		}

		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "소셜 회원가입")
	@PostMapping("/social-signup")
	public ResponseEntity<ApiResponse<SimpleSignupResponse>> signUp(
		@Valid @RequestBody SimpleSignupRequest simpleSignupRequest,
		@RequestHeader(value = "kakao-access-token") String token) throws Exception {
		SimpleSignupResponse socialSignUpResponse = authService.signUp(simpleSignupRequest, token);
		return ResponseEntity.ok(ApiResponse.success(socialSignUpResponse));
	}

	@Operation(summary = "프로필 페이지 조회")
	@GetMapping("/profile/{nickname}")
	public ResponseEntity<ApiResponse<ProfilePageResponse>> getProfilePage(
		@PathVariable(value = "nickname") String nickname) {
		Long id = (Long)httpServletRequest.getAttribute("id");
		return ResponseEntity.ok(ApiResponse.success(memberService.getProfilePage(id, nickname)));
	}

	@Operation(summary = "액세스 토큰, 리프레쉬 토큰 재발급")
	@PostMapping("/reissueToken")
	public ResponseEntity<ApiResponse<TokenResponse>> getToken() {
		Long id = (Long)httpServletRequest.getAttribute("id");
		String originToken = httpServletRequest.getHeader("refresh-token");
		return ResponseEntity.ok(ApiResponse.success(memberService.reissueToken(originToken, id)));
	}

	@Operation(summary = "로그아웃")
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout() {
		Long id = (Long)httpServletRequest.getAttribute("id");
		memberService.logout(id);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "회원 탈퇴")
	@PostMapping("/withdraw")
	public ResponseEntity<ApiResponse<Void>> withdraw() {
		Long id = (Long)httpServletRequest.getAttribute("id");
		memberService.withdraw(id);
		return ResponseEntity.ok(ApiResponse.success());
	}

	@Operation(summary = "회원 아이콘 변경")
	@PostMapping("/icon")
	public ResponseEntity<ApiResponse<Void>> updateIcon(@RequestBody UpdateIconRequest updateIconRequest) {
		Long id = (Long)httpServletRequest.getAttribute("id");
		memberService.updateIcon(id, updateIconRequest.getIcon());
		return ResponseEntity.ok(ApiResponse.success());
	}
}

