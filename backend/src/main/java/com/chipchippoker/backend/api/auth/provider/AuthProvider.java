package com.chipchippoker.backend.api.auth.provider;

import com.chipchippoker.backend.api.auth.model.dto.Token;

public interface AuthProvider {
	Long getSocialId(String token);

	Token getToken(String authorizationCode);
}
