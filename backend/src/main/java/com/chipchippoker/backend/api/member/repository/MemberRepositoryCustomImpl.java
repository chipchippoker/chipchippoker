package com.chipchippoker.backend.api.member.repository;

import static com.chipchippoker.backend.common.entity.QFriend.*;
import static com.chipchippoker.backend.common.entity.QMember.*;
import static com.chipchippoker.backend.common.entity.QPoint.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.RecentPlayListResponse;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public ProfilePageResponse getProfilePage(Member m, Integer myTotalRank, Integer myFriendRank, boolean isMine,
		boolean isFriend, boolean isSent,
		List<RecentPlayListResponse> recentPlayListResponseList) {
		Point result = queryFactory
			.selectFrom(point)
			.where(point.member.id.eq(m.getId()))
			.fetchOne();
		return ProfilePageResponse.createProfilePageResponse(result.getMember().getIcon(), myTotalRank, myFriendRank,
			result.getFriendlyWin(), result.getFriendlyDraw(), result.getFriendlyLose(),
			result.getCompetitiveWin(), result.getCompetitiveDraw(), result.getCompetitiveLose(), result.getMaxWin(),
			result.getPointScore(),
			result.getMember().getNickname(), result.tierByPoint(result.getPointScore()), isMine, isFriend, isSent,
			result.getMember().getKakaoLinkState(),
			recentPlayListResponseList);
	}

	public List<Point> getTotalRank(Integer limit) {
		if (limit != 0) {
			return queryFactory
				.selectFrom(point)
				.orderBy(point.pointScore.desc())
				.limit(limit)
				.fetch();
		}
		return queryFactory
			.selectFrom(point)
			.orderBy(point.pointScore.desc())
			.fetch();

	}

	public List<Friend> getFriendRank(Long id, Integer limit) {
		if (limit != 0) {
			return queryFactory
				.selectFrom(friend)
				.join(friend.memberA, member)
				.join(member.point, point)
				.where(member.id.eq(id))
				.orderBy(friend.memberB.point.pointScore.desc())
				.limit(limit)
				.fetch();
		}
		return queryFactory
			.selectFrom(friend)
			.join(friend.memberA, member)
			.join(member.point, point)
			.where(member.id.eq(id))
			.orderBy(friend.memberB.point.pointScore.desc())
			.fetch();
	}

	public Friend getMyInformInFriend(Long id) {
		return queryFactory
			.selectFrom(friend)
			.join(friend.memberB, member)
			.join(member.point, point)
			.where(member.id.eq(id))
			.fetchFirst();
	}

}