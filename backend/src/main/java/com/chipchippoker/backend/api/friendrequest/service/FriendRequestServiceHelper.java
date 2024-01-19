package com.chipchippoker.backend.api.friendrequest.service;

import com.chipchippoker.backend.api.friend.repository.FriendRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.FriendRequest;
import com.chipchippoker.backend.common.exception.ForbiddenException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestServiceHelper {
	public static void isFriendRequestExist(FriendRequest friendRequest) {
		// 친구 요청 상태가 대기인 경우
		if (friendRequest.getStatus().equals("대기"))
			throw new ForbiddenException(ErrorBase.E403_ALREADY_SENT_FRIEND_REQUEST);
		// 친구 요청 상태가 거절인 경우
		if (friendRequest.getStatus().equals("거절"))
			friendRequest.updateStatus("대기");
	}

	public static void isAlreadyFriend(FriendRepository friendRepository, Long fromMemberId, Long toMemberId) {
		Friend friend = friendRepository.findByMemberAIdAndMemberBId(fromMemberId, toMemberId);
		if (friend != null) {
			throw new ForbiddenException(ErrorBase.E403_ALREADY_FRIEND);
		}
	}
}
