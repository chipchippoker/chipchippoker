package com.chipchippoker.backend.api.gameroomblacklist.respository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameRoomBlackListRepositoryCustomImpl implements GameRoomBlackListRepositoryCustom {
	private final JPAQueryFactory queryFactory;
}

