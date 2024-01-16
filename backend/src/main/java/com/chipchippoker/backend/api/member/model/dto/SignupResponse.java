package com.chipchippoker.backend.api.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
	private String accessToken;
	private String refreshToken;
	private String icon;
	private String nickname;
}
