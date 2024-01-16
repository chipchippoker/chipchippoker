package com.chipchippoker.backend.api.member.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.jwt.serivce.JwtUtil;
import com.chipchippoker.backend.api.member.model.dto.LoginRequest;
import com.chipchippoker.backend.api.member.model.dto.LoginResponse;
import com.chipchippoker.backend.api.member.model.dto.SignupRequest;
import com.chipchippoker.backend.api.member.model.dto.SignupResponse;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.exception.DuplicateException;
import com.chipchippoker.backend.common.exception.InvalidException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;

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
		memberRepository.save(member);

		String accessToken = jwtUtil.createAccessToken(member.getId());
		String refreshToken = jwtUtil.createRefreshToken(member.getId());

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
		String accessToken = jwtUtil.createAccessToken(member.getId());
		String refreshToken = jwtUtil.createRefreshToken(member.getId());

		updateRefreshToken(member.getMemberId(), refreshToken);
		return new LoginResponse(accessToken, refreshToken, member.getIcon(), member.getNickname());
	}

	@Override
	public Boolean isDuplicateMemberId(String memberId) {
		return memberRepository.existsByMemberId(memberId);
	}

	@Override
	public Boolean isDuplicateNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	@Transactional
	public void updateRefreshToken(String memberId, String refreshToken) {
		Member member = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new InvalidException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		member.updateRefreshToken(refreshToken);
	}

	private static String encryptionPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest msd = MessageDigest.getInstance("SHA-256");
		byte[] bytePassword = msd.digest(password.getBytes());

		StringBuilder tmpPassword = new StringBuilder();
		for (byte bp : bytePassword) {
			tmpPassword.append(String.format("%02x", bp));
		}
		return tmpPassword.toString();
	}

}
