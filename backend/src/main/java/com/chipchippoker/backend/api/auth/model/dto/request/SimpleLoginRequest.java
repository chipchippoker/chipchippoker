package com.chipchippoker.backend.api.auth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleLoginRequest {
	String authorizationCode;
}
