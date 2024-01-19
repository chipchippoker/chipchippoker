package com.chipchippoker.backend.api.member.service;

import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;

public interface MemberService {
	ProfilePageResponse getProfilePage(Long id, String nickname);
}
