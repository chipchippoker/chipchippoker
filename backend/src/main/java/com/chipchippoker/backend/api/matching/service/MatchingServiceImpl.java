package com.chipchippoker.backend.api.matching.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingResponse;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl implements MatchingService {
	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;

	@Override
	public StartFriendlyMatchingResponse startFriendlyMatching(Long id, Integer totalParticipantCnt) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		// 친선, 공개, 대기, 총 인원수, 빈방을 만족하는 게임방 조회
		List<GameRoom> gameRoomList = gameRoomRepository.findByStartFriendlyMatchingSearchOption(totalParticipantCnt);

		if (!gameRoomList.isEmpty()) { // 조회된 게임방이 있는 경우
			GameRoom gameRoom = gameRoomList.get(0);
			member.enterGameRoom(gameRoom);
			return StartFriendlyMatchingResponse.startFriendlyMatchingResponse(gameRoom);
		} else { // 조회된 게임방이 없는 경우
			return null;
		}
	}
}
