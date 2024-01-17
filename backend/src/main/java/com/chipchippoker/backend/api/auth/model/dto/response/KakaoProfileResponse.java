package com.chipchippoker.backend.api.auth.model.dto.response;

import com.chipchippoker.backend.api.auth.model.dto.KakaoAccount;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoProfileResponse {
	private Long id;
	private String connected_at;
	private KakaoAccount kakao_account;
}
