package com.chipchippoker.backend.api.friendrequest.repository;

import static com.chipchippoker.backend.common.entity.QFriendRequest.*;

import java.util.List;

import com.chipchippoker.backend.common.entity.FriendRequest;
import com.chipchippoker.backend.common.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendRequestRepositoryCustomImpl implements FriendRequestRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<FriendRequest> findByToMemberAndStatus(Member toMember) {
		return queryFactory
			.selectFrom(friendRequest)
			.where(friendRequest.toMember.eq(toMember), friendRequest.status.eq("대기"))
			.fetch();
	}
}

