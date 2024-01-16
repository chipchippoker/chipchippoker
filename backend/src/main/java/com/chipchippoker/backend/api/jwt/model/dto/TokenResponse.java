package com.chipchippoker.backend.api.jwt.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
	String accessToken;
	String refreshToken;
}
