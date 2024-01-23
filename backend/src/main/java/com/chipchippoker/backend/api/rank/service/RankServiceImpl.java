package com.chipchippoker.backend.api.rank.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.api.rank.model.dto.FriendRankResponse;
import com.chipchippoker.backend.api.rank.model.dto.MyselfRankResponse;
import com.chipchippoker.backend.api.rank.model.dto.TotalRankResponse;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RankServiceImpl implements RankService {
	private final MemberRepository memberRepository;

	@Override
	public List<TotalRankResponse> getTotalRank() {
		List<Point> totalRankList = memberRepository.getTotalRank();
		return IntStream.range(0, totalRankList.size())
			.mapToObj(index -> {
				Point point = totalRankList.get(index);
				return new TotalRankResponse(index + 1, Point.tierByPoint(point.getPointScore()),
					point.getMember().getIcon(), point.getMember().getNickname(), point.getPointScore());
			})
			.collect(Collectors.toList());
	}

	@Override
	public List<FriendRankResponse> getFriendRank(Long id) {
		List<Friend> friendRankList = memberRepository.getFriendRank(id);
		Friend myRankInFriend = memberRepository.getMyRankInFriend(id);
		friendRankList.add(myRankInFriend);

		//친구가 한명도 없는 경우
		if(myRankInFriend==null){
			Member member = memberRepository.findById(id).orElseThrow(()->new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
			FriendRankResponse friendRankResponse = new FriendRankResponse(1,
				Point.tierByPoint(member.getPoint().getPointScore()),
				member.getIcon(), member.getNickname(),
				member.getPoint().getPointScore());
			return List.of(friendRankResponse);
		}

		//친구가 있는 경우 내 랭킹을 추가했으니 재 정렬 후 30명까지만 보여줌
		Collections.sort(friendRankList,
			(a, b) -> b.getMemberB().getPoint().getPointScore() - a.getMemberB().getPoint().getPointScore());

		return IntStream.range(0, Math.min(friendRankList.size(), 30))
			.mapToObj(index -> {
				Friend friend = friendRankList.get(index);
				return new FriendRankResponse(index + 1,
					Point.tierByPoint(friend.getMemberB().getPoint().getPointScore()),
					friend.getMemberB().getIcon(), friend.getMemberB().getNickname(),
					friend.getMemberB().getPoint().getPointScore());
			})
			.collect(Collectors.toList());
	}

	@Override
	public MyselfRankResponse getMyselfRank(Long id) {
		//인원 제한이 없는 전체 조회 메서드
		List<Point> totalRank = memberRepository.getTotalRankAll();
		for(int i=0;i<totalRank.size();i++){
			if(totalRank.get(i).getMember().getId()==id){
				Point p = totalRank.get(i);
				return new MyselfRankResponse(i+1,Point.tierByPoint(p.getPointScore()),p.getMember().getIcon(),p.getMember().getNickname(),p.getPointScore());
			}
		}
		throw new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER);
	}
}
