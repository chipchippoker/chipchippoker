package com.chipchippoker.backend.api.gameroom.repository;

import static com.chipchippoker.backend.common.entity.QGameRoom.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.chipchippoker.backend.common.entity.GameRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameRoomRepositoryCustomImpl implements GameRoomRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public GameRoom findByTitleAndState(String title) {
		return queryFactory
			.selectFrom(gameRoom)
			.where(gameRoom.title.eq(title), gameRoom.state.eq("종료").not())
			.fetchOne();
	}

	public Page<GameRoom> findBySearchOption(String type, String title, Boolean isTwo, Boolean isThree,
		Boolean isFour,
		Boolean isEmpty, Pageable pageable) {
		if (!isTwo && !isThree && !isFour) {
			isTwo = true;
			isThree = true;
			isFour = true;
		}

		List<GameRoom> gameRoomList = queryFactory
			.selectFrom(gameRoom)
			.where(gameRoom.type.eq(type), gameRoom.title.contains(title),
				isTwo(isTwo).or(isThree(isThree)).or(isFour(isFour)), isEmpty(isEmpty), gameRoom.state.eq("종료").not())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<GameRoom> count = queryFactory
			.selectFrom(gameRoom)
			.where(gameRoom.type.eq(type), gameRoom.title.contains(title),
				isTwo(isTwo).or(isThree(isThree)).or(isFour(isFour)), isEmpty(isEmpty), gameRoom.state.eq("종료").not());

		return PageableExecutionUtils.getPage(gameRoomList, pageable, count::fetchCount);
	}

	private BooleanExpression isTwo(Boolean isTwo) {
		if (!isTwo) {
			return gameRoom.isNull(); // TODO: null -> gameRoom.isNull()로 변경 후 에러 수정함. 추후 이유 파악 필요 
		}
		return gameRoom.totalParticipantCnt.eq(2);
	}

	private BooleanExpression isThree(Boolean isThree) {
		if (!isThree) {
			return null;
		}
		return gameRoom.totalParticipantCnt.eq(3);
	}

	private BooleanExpression isFour(Boolean isFour) {
		if (!isFour) {
			return null;
		}
		return gameRoom.totalParticipantCnt.eq(4);
	}

	private BooleanExpression isEmpty(Boolean isEmpty) {
		if (!isEmpty) {
			return null;
		}
		return gameRoom.members.size().lt(gameRoom.totalParticipantCnt).and(gameRoom.state.eq("대기"));
	}

	public List<GameRoom> findByStartFriendlyMatchingSearchOption(Integer totalParticipantCnt) {
		return queryFactory
			.selectFrom(gameRoom)
			.where(gameRoom.type.eq("친선"), gameRoom.isPrivate.eq(Boolean.FALSE), gameRoom.state.eq("대기"),
				gameRoom.totalParticipantCnt.eq(totalParticipantCnt), gameRoom.members.size().lt(totalParticipantCnt))
			.orderBy(gameRoom.createdAt.asc())
			.fetch();
	}

	public GameRoom findByStartCompetitionMatchingSearchOption(Integer totalParticipantCnt) {
		return queryFactory
			.selectFrom(gameRoom)
			.where(gameRoom.type.eq("경쟁"), gameRoom.state.eq("대기"),
				gameRoom.totalParticipantCnt.eq(totalParticipantCnt))
			.fetchOne();
	}
}

