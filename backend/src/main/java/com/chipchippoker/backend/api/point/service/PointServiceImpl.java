package com.chipchippoker.backend.api.point.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.api.point.repository.PointRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointServiceImpl {
	private final PointRepository pointRepository;
	private final MemberRepository memberRepository;

	public void saveGameResult(String nickname, Integer coin) {
		Member member = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		Point point = pointRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		point.updateResult(coin);
	}
}
