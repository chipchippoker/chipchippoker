package com.chipchippoker.backend.api.auth.model.dto.request;

import lombok.Data;

@Data
public class AuthorizationCodeRequest {
	String authorizationCode;
}
