package com.chipchippoker.backend.api.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleSignupResponse {
	private String accessToken;
	private String refreshToken;
	private String icon;
	private String nickname;
}
