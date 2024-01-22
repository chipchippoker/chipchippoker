package com.chipchippoker.backend.api.rank.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.api.rank.model.dto.TotalRankResponse;
import com.chipchippoker.backend.common.entity.Point;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RankServiceImpl implements RankService{
	private final MemberRepository memberRepository;

	@Override
	public List<TotalRankResponse> getTotalRank() {
		List<Point> totalRankList = memberRepository.getTotalRank();
		log.info(totalRankList.toString());
		return IntStream.range(0, totalRankList.size())
			.mapToObj(index -> {
				Point point = totalRankList.get(index);
				return new TotalRankResponse(index+1, Point.tierByPoint(point.getPointScore()), point.getMember().getIcon(), point.getMember().getNickname(), point.getPointScore());
			})
			.collect(Collectors.toList());
	}
}
