package com.chipchippoker.backend.api.auth.provider;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.chipchippoker.backend.api.auth.model.dto.MemberSocialType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthProviderFinderImpl implements AuthProviderFinder{
	private static final Map<MemberSocialType, AuthProvider> authProviderMap = new EnumMap<>(MemberSocialType.class);

	private final KakaoAuthProvider kakaoAuthProvider;

	@PostConstruct
	void initializeAuthProviders() {
		authProviderMap.put(MemberSocialType.KAKAO, kakaoAuthProvider);
	}

	@Override
	public AuthProvider findAuthProvider(MemberSocialType socialType) {
		AuthProvider authProvider = authProviderMap.get(socialType);

		// 지원하지 않는 소셜타입에 대한 예외처리 적용 -> 세부 에러로 나중에 조정 필요
		if (authProvider == null) {
			throw new IllegalArgumentException("지원하지 않는 소셜로그인입니다.");
		}
		return authProvider;
	}
}
