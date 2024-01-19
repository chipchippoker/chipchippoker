package com.chipchippoker.backend.api.member.repository;

import static com.chipchippoker.backend.common.entity.QMember.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.RecentPlayListResponse;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.entity.QPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public ProfilePageResponse getProfilePage(Member m, boolean isMine,
		List<RecentPlayListResponse> recentPlayListResponseList) {
        Point point = queryFactory
            .select(QPoint.point)
            .from(QPoint.point)
            .where(QPoint.point.member.id.eq(m.getId()))
            .leftJoin(QPoint.point.member,member)
            .fetchJoin()
            .fetchOne();
        return ProfilePageResponse.createProfilePageResponse(point.getMember().getIcon(),12,6,point.getWin(),point.getDraw(),point.getLose(),point.getMaxWin(),point.getPointScore(),point.getMember().getNickname(),point.tierByPoint(point.getPointScore()),isMine,recentPlayListResponseList);
    }
}