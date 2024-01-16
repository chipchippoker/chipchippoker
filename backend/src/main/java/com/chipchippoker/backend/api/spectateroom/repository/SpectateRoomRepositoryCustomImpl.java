package com.chipchippoker.backend.api.spectateroom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpectateRoomRepositoryCustomImpl implements SpectateRoomRepositoryCustom {
	private final JPAQueryFactory queryFactory;
}

