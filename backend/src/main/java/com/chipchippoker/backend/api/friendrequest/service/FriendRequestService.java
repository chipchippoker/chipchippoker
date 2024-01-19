package com.chipchippoker.backend.api.friendrequest.service;

public interface FriendRequestService {
	void requestFriend(Long fromMemberId, String toMemberNickname);

	void acceptFriendRequest(Long toMemberId, String fromMemberNickname);

	void rejectFriendRequest(Long toMemberId, String fromMemberNickname);
}
