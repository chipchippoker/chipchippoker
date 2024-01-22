package com.chipchippoker.backend.api.friendrequest.service;

import java.util.List;

import com.chipchippoker.backend.api.friendrequest.model.dto.GetFriendRequestListResponse;

public interface FriendRequestService {
	void requestFriend(Long fromMemberId, String toMemberNickname);

	void acceptFriendRequest(Long toMemberId, String fromMemberNickname);

	void rejectFriendRequest(Long toMemberId, String fromMemberNickname);

	List<GetFriendRequestListResponse> getFriendRequestList(Long id);
}
