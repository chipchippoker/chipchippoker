package com.chipchippoker.backend.api.auth.model.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KakaoSecret {
	@Value("${kakao.client_id}")
	private String client_id;

	@Value("${kakao.redirect_uri}")
	private String redirect_uri;

	public String getClient_id() {
		return client_id;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}
}
