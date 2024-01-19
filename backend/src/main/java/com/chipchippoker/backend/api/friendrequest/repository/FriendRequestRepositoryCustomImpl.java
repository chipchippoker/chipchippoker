package com.chipchippoker.backend.api.friendrequest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendRequestRepositoryCustomImpl implements FriendRequestRepositoryCustom {
	private final JPAQueryFactory queryFactory;
}

