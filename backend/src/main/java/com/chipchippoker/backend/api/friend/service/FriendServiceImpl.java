package com.chipchippoker.backend.api.friend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.friend.model.dto.SearchFriendListResponse;
import com.chipchippoker.backend.api.friend.model.dto.SearchFriendResponse;
import com.chipchippoker.backend.api.friend.repository.FriendRepository;
import com.chipchippoker.backend.api.friendrequest.repository.FriendRequestRepository;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.FriendRequest;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.exception.NotFoundException;
import com.chipchippoker.backend.common.util.online.OnlineUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService {
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;
	private final FriendRequestRepository friendRequestRepository;
	private final OnlineUtil onlineUtil;

	@Override
	public SearchFriendResponse searchFriend(Long id, String nickname) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		Optional<Member> searchMember = memberRepository.findByNickname(nickname);
		if (searchMember.isPresent()) {
			Member searchedMember = searchMember.get();
			Boolean isOnline = onlineUtil.isOnline(searchedMember.getNickname());

			// 친구인지 확인
			Boolean isFriend = Boolean.FALSE;
			Friend friend = friendRepository.findByMemberAIdAndMemberBId(member.getId(), searchedMember.getId());
			if (friend != null)
				isFriend = Boolean.TRUE;

			// 친구 요청을 보냈고, 대기 중인지 확인
			Boolean isSent = Boolean.FALSE;
			FriendRequest friendRequest = friendRequestRepository.findByFromMemberIdAndToMemberId(member.getId(),
				searchedMember.getId());
			if (friendRequest != null && friendRequest.getStatus().equals("대기"))
				isSent = Boolean.TRUE;

			return SearchFriendResponse.searchFriendResponse(
				searchedMember.getNickname(), searchedMember.getIcon(), isOnline, isFriend, isSent);
		} else {
			return null;
		}
	}

	@Override
	public List<SearchFriendListResponse> searchFriendList(Long id, String nickname) {
		Member memberA = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		Optional<Member> memberB = memberRepository.findByNickname(nickname);

		List<Friend> friendList = new ArrayList<>();
		if (nickname.equals("")) {
			friendList = friendRepository.findByMemberA(memberA);
		} else if (memberB.isPresent()) {
			Friend friend = friendRepository.findByMemberAIdAndMemberBId(id, memberB.get().getId());
			if (friend != null)
				friendList.add(friend);
		} else {
			return null;
		}

		List<SearchFriendListResponse> searchFriendListResponses = new ArrayList<>();
		for (Friend friend : friendList) {
			Member member = friend.getMemberB();
			Point point = member.getPoint();
			String tier = point.tierByPoint(point.getPointScore());
			Boolean isOnline = onlineUtil.isOnline(member.getNickname());
			SearchFriendListResponse searchFriendListResponse = SearchFriendListResponse.searchFriendListResponse(
				member.getIcon(), tier, member.getNickname(), isOnline);
			searchFriendListResponses.add(searchFriendListResponse);
		}
		return searchFriendListResponses;
	}
}
