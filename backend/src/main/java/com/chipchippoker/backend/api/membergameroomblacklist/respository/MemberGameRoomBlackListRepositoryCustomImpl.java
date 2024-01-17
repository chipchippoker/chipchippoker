package com.chipchippoker.backend.api.membergameroomblacklist.respository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberGameRoomBlackListRepositoryCustomImpl implements MemberGameRoomBlackListRepositoryCustom {
	private final JPAQueryFactory queryFactory;
}

