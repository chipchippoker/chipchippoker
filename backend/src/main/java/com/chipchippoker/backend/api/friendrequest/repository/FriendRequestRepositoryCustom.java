package com.chipchippoker.backend.api.friendrequest.repository;

import java.util.List;

import com.chipchippoker.backend.common.entity.FriendRequest;
import com.chipchippoker.backend.common.entity.Member;

public interface FriendRequestRepositoryCustom {
	List<FriendRequest> findByToMemberAndStatus(Member toMember);
}
