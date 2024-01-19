package com.chipchippoker.backend.api.member.repository;

import static com.chipchippoker.backend.common.entity.QMember.*;
import static com.chipchippoker.backend.common.entity.QPoint.*;

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
        Point result = queryFactory
            .selectFrom(point)
            .where(point.member.id.eq(m.getId()))
            .leftJoin(point.member,member)
            .fetchJoin()
            .fetchOne();
        return ProfilePageResponse.createProfilePageResponse(result.getMember().getIcon(),12,6,result.getWin(),result.getDraw(),result.getLose(),result.getMaxWin(),result.getPointScore(),result.getMember().getNickname(),result.tierByPoint(result.getPointScore()),isMine,recentPlayListResponseList);
        //todo 조인 지우고 테스트
    }
}