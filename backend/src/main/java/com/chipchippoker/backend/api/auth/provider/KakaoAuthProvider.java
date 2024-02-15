package com.chipchippoker.backend.api.auth.provider;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.chipchippoker.backend.api.auth.model.dto.KakaoAuthApiClient;
import com.chipchippoker.backend.api.auth.model.dto.KakaoToken;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class KakaoAuthProvider implements AuthProvider {
	private final KakaoAuthApiClient kakaoAuthApiClient;

	@Override
	public Long getSocialId(String token) {
		try {
			return kakaoAuthApiClient.getProfileInfo("Bearer " + token).getId();
		} catch (Exception e) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTH_TOKEN);
		}
	}

	@Override
	public KakaoToken getToken(String authorizationCode) {
		try {
			return kakaoAuthApiClient.getKakaoToken(authorizationCode);
		} catch (HttpClientErrorException e) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTHORIZATION_CODE);
		}
	}

	@Override
	public KakaoToken getConnectionToken(String authorizationCode) {
		try {
			return kakaoAuthApiClient.getKakaoConnectionToken(authorizationCode);
		} catch (HttpClientErrorException e) {
			throw new InvalidException(ErrorBase.E400_INVALID_AUTHORIZATION_CODE);
		}
	}
}
