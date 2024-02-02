package com.chipchippoker.backend.api.auth.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.auth.model.dto.MemberSocialType;
import com.chipchippoker.backend.api.auth.model.dto.Token;
import com.chipchippoker.backend.api.auth.model.dto.request.LoginRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.request.SimpleSignupRequest;
import com.chipchippoker.backend.api.auth.model.dto.response.LoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SignupResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleLoginResponse;
import com.chipchippoker.backend.api.auth.model.dto.response.SimpleSignupResponse;
import com.chipchippoker.backend.api.auth.provider.AuthProvider;
import com.chipchippoker.backend.api.auth.provider.AuthProviderFinder;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.exception.DuplicateException;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.exception.NotFoundException;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;
	private final AuthProviderFinder authProviderFinder;

	@Override
	public SignupResponse signup(SignupRequest signupRequest) throws NoSuchAlgorithmException {

		//아이디 중복 확인
		if (isDuplicateMemberId(signupRequest.getMemberId())) {
			throw new DuplicateException(ErrorBase.E409_DUPLICATE_MEMBER);
		}

		//닉네임 중복 확인
		if (isDuplicateNickname(signupRequest.getNickname())) {
			throw new DuplicateException(ErrorBase.E409_DUPLICATE_NICKNAME);
		}

		//패스워드 암호화
		String password = encryptionPassword(signupRequest.getPassword());
		signupRequest.setPassword(password);

		Member member = Member.newMember(signupRequest);
		Point point = Point.createPoint(member);
		member.createPoint(point);
		memberRepository.save(member);

		String accessToken = jwtUtil.createAccessToken(member.getId(), member.getNickname());
		String refreshToken = jwtUtil.createRefreshToken(member.getId(), member.getNickname());

		updateRefreshToken(member.getMemberId(), refreshToken);
		return new SignupResponse(accessToken, refreshToken, member.getIcon(), member.getNickname());
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException {
		Member member = memberRepository.findByMemberId(loginRequest.getMemberId())
			.orElseThrow(() -> new InvalidException(ErrorBase.E400_INVALID_ENCODING_ID));

		String loginPassword = encryptionPassword(loginRequest.getPassword());
		if (!loginPassword.equals(member.getPassword())) {
			throw new InvalidException(ErrorBase.E400_INVALID_PASSWORD);
		}
		String accessToken = jwtUtil.createAccessToken(member.getId(), member.getNickname());
		String refreshToken = jwtUtil.createRefreshToken(member.getId(), member.getNickname());

		updateRefreshToken(member.getMemberId(), refreshToken);
		return new LoginResponse(accessToken, refreshToken, member.getIcon(), member.getNickname());
	}

	@Override
	public SimpleLoginResponse kakaoLogin(Token token) {
		AuthProvider authProvider = authProviderFinder.findAuthProvider(MemberSocialType.KAKAO);
		Long socialId = authProvider.getSocialId(token.getAccess_token());

		Member member = memberRepository.findByKakaoSocialId(socialId)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		String accessToken = jwtUtil.createAccessToken(member.getId(), member.getNickname());
		String refreshToken = jwtUtil.createRefreshToken(member.getId(), member.getNickname());
		return new SimpleLoginResponse(accessToken, refreshToken, member.getIcon(), member.getNickname());

	}

	@Override
	public Token getToken(String authorizationCode) {
		AuthProvider authProvider = authProviderFinder.findAuthProvider(MemberSocialType.KAKAO);
		return authProvider.getToken(authorizationCode);
	}

	@Override
	public SimpleSignupResponse signUp(SimpleSignupRequest request, String token) {
		AuthProvider authProvider = authProviderFinder.findAuthProvider(MemberSocialType.KAKAO);
		if (token == null || token.isEmpty()) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTH_TOKEN);
		}
		String decodingToken = jwtUtil.decodingToken(token);
		Long socialId = authProvider.getSocialId(decodingToken);
		if (validateExistsUser(socialId)) {
			throw new DuplicateException(ErrorBase.E409_DUPLICATE_MEMBER);
		}
		memberRepository.findByNickname(request.getNickname())
			.ifPresent(member -> {
				throw new DuplicateException(ErrorBase.E409_DUPLICATE_NICKNAME);
			});
		Member member = Member.newKakaoMember(request);
		member.connectSocialId(socialId);
		Point point = Point.createPoint(member);
		member.createPoint(point);
		memberRepository.save(member);
		String accessToken = jwtUtil.createAccessToken(member.getId(), member.getNickname());
		String refreshToken = jwtUtil.createRefreshToken(member.getId(), member.getNickname());
		member.updateRefreshToken(refreshToken);
		return new SimpleSignupResponse(accessToken, refreshToken, member.getIcon(), member.getNickname());
	}

	@Override
	public Long getKakaoSocialId(String accessToken) {
		AuthProvider authProvider = authProviderFinder.findAuthProvider(MemberSocialType.KAKAO);
		return authProvider.getSocialId(accessToken);
	}

	@Override
	public Boolean validateExistsUser(Long socialId) {
		return memberRepository.existsByKakaoSocialId(socialId);
	}

	@Override
	public void socialConnection(Long id, Long kakaoSocialId) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		member.connectSocialId(kakaoSocialId);
	}

	@Override
	public String encryptionToken(String accessToken) {
		return jwtUtil.encryptionToken(accessToken);
	}

	@Override
	public Boolean isDuplicateMemberId(String memberId) {
		return memberRepository.existsByMemberId(memberId);
	}

	@Override
	public Boolean isDuplicateNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	public void updateRefreshToken(String memberId, String refreshToken) {
		Member member = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new InvalidException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		member.updateRefreshToken(refreshToken);
	}

	private String encryptionPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest msd = MessageDigest.getInstance("SHA-256");
		byte[] bytePassword = msd.digest(password.getBytes());

		StringBuilder tmpPassword = new StringBuilder();
		for (byte bp : bytePassword) {
			tmpPassword.append(String.format("%02x", bp));
		}
		return tmpPassword.toString();
	}
}
