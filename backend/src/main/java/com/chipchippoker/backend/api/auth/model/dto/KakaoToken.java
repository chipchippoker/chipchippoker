package com.chipchippoker.backend.api.auth.model.dto;

public class KakaoToken extends Token {
	private String id_token;
	private String scope;
	private Integer refresh_token_expires_in;
}
