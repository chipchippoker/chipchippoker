package com.chipchippoker.backend.api.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationCodeAlreadyAccountResponse implements AuthorizationInformationResponse {
	private Integer code;
	private String message;
	private String accessToken;
	private String refreshToken;
	private String icon;
	private String nickname;
}
