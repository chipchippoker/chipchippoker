package com.chipchippoker.backend.api.member.repository;

import java.util.List;

import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.RecentPlayListResponse;
import com.chipchippoker.backend.common.entity.Member;

public interface MemberRepositoryCustom {

	ProfilePageResponse getProfilePage(Member member, boolean isMine,
		List<RecentPlayListResponse> recentPlayListResponseList);
}
