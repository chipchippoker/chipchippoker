package com.chipchippoker.backend.api.friendrequest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.friend.repository.FriendRepository;
import com.chipchippoker.backend.api.friendrequest.model.dto.GetFriendRequestListResponse;
import com.chipchippoker.backend.api.friendrequest.repository.FriendRequestRepository;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.FriendRequest;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.exception.ForbiddenException;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {
	private final FriendRequestRepository friendRequestRepository;
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;

	@Override
	public void requestFriend(Long fromMemberId, String toMemberNickname) {
		Member fromMember = memberRepository.findById(fromMemberId)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		Member toMember = memberRepository.findByNickname(toMemberNickname)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		FriendRequest friendRequest = friendRequestRepository.findByFromMemberIdAndToMemberId(fromMember.getId(),
			toMember.getId());

		if (fromMemberId.equals(toMember.getId()))
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN);

		FriendRequestServiceHelper.isAlreadyFriend(friendRepository, fromMember.getId(), toMember.getId());

		if (friendRequest != null) {
			FriendRequestServiceHelper.isFriendRequestExist(friendRequest);
		} else {
			friendRequest = FriendRequest.createFriendRequest("대기", fromMember, toMember);
			friendRequestRepository.save(friendRequest);
		}
	}

	@Override
	public void acceptFriendRequest(Long toMemberId, String fromMemberNickname) {
		Member toMember = memberRepository.findById(toMemberId)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		Member fromMember = memberRepository.findByNickname(fromMemberNickname)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		FriendRequestServiceHelper.isAlreadyFriend(friendRepository, toMember.getId(), fromMember.getId());

		FriendRequest fromMemberFriendRequest = friendRequestRepository.findByFromMemberIdAndToMemberId(
			fromMember.getId(),
			toMember.getId());
		fromMemberFriendRequest.updateStatus("수락");

		Friend fromMemberFriend = Friend.createFriend(fromMember, toMember);
		friendRepository.save(fromMemberFriend);
		Friend toMemberFriend = Friend.createFriend(toMember, fromMember);
		friendRepository.save(toMemberFriend);
	}

	@Override
	public void rejectFriendRequest(Long toMemberId, String fromMemberNickname) {
		Member toMember = memberRepository.findById(toMemberId)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		Member fromMember = memberRepository.findByNickname(fromMemberNickname)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		FriendRequestServiceHelper.isAlreadyFriend(friendRepository, toMember.getId(), fromMember.getId());

		FriendRequest friendRequest = friendRequestRepository.findByFromMemberIdAndToMemberId(fromMember.getId(),
			toMember.getId());
		friendRequest.updateStatus("거절");
	}

	@Override
	public List<GetFriendRequestListResponse> getFriendRequestList(Long id) {
		// 사용자가 받은 친구 요청 중 대기 상태만을 조회
		Member toMember = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		List<FriendRequest> friendRequestList = friendRequestRepository.findByToMemberAndStatus(toMember);

		List<GetFriendRequestListResponse> responseList = new ArrayList<>();
		for (FriendRequest friendRequest : friendRequestList) {
			Member fromMember = friendRequest.getFromMember();
			Friend friend = friendRepository.findByMemberAIdAndMemberBId(fromMember.getId(),
				toMember.getId());
			if (friend == null) {
				GetFriendRequestListResponse getFriendRequestListResponse = GetFriendRequestListResponse.getFriendRequestListResponse(
					fromMember.getNickname());
				responseList.add(getFriendRequestListResponse);
			}
		}
		return responseList;
	}
}
