package com.chipchippoker.backend.api.auth.provider;

import com.chipchippoker.backend.api.auth.model.dto.MemberSocialType;

public interface AuthProviderFinder {
	AuthProvider findAuthProvider(MemberSocialType socialType);
}
