package com.chipchippoker.backend.api.member.repository;

import java.util.List;

import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.RecentPlayListResponse;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;

public interface MemberRepositoryCustom {

	ProfilePageResponse getProfilePage(Member member, boolean isMine,
		List<RecentPlayListResponse> recentPlayListResponseList);

	List<Point> getTotalRank();

	List<Friend> getFriendRank(Long id);

	Friend getMyRankInFriend(Long id);
}
