package com.chipchippoker.backend.api.member.repository;

import java.util.List;

import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.RecentPlayListResponse;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;

public interface MemberRepositoryCustom {

	ProfilePageResponse getProfilePage(Member member, Integer myTotalRank, Integer myFriendRank, boolean isMine,
		boolean isFriend, boolean isSent,
		List<RecentPlayListResponse> recentPlayListResponseList);

	List<Point> getTotalRank(Integer limit);

	List<Friend> getFriendRank(Long id, Integer limit);

	Friend getMyInformInFriend(Long id);

}
