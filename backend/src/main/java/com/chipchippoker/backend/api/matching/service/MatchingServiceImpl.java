package com.chipchippoker.backend.api.matching.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.matching.model.dto.QuitMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartCompetitionMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingResponse;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.exception.NotFoundException;
import com.chipchippoker.backend.websocket.matching.controller.GameMatchingManager;
import com.chipchippoker.backend.websocket.matching.dto.CompleteMatchingMessageRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl implements MatchingService {
	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;
	private final GameMatchingManager gameMatchingManager;

	@Override
	public StartFriendlyMatchingResponse startFriendlyMatching(Long id, Integer totalParticipantCnt) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		// 친선, 공개, 대기, 총 인원수, 빈방을 만족하는 게임방 조회
		List<GameRoom> gameRoomList = gameRoomRepository.findByStartFriendlyMatchingSearchOption(totalParticipantCnt);

		if (!gameRoomList.isEmpty()) { // 조회된 게임방이 있는 경우
			GameRoom gameRoom = gameRoomList.get(0);
			member.enterGameRoom(gameRoom);
			// 게임방에 처음 들어가는 사용자인가 (친선전 빠른 시작은 무조건 처음 들어가는 사용자가 아님)
			return StartFriendlyMatchingResponse.startFriendlyMatchingResponse(gameRoom, Boolean.FALSE);
		} else { // 조회된 게임방이 없는 경우
			return null;
		}
	}

	@Override
	public StartCompetitionMatchingResponse startCompetitionMatching(Long id, Integer totalParticipantCnt) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		// 경쟁, 대기, 총 인원수를 만족하는 게임방 조회
		GameRoom gameRoom = gameRoomRepository.findByStartCompetitionMatchingSearchOption(
			totalParticipantCnt);

		if (gameRoom != null) { // 입장 가능한 게임방이 있다면
			member.enterGameRoom(gameRoom); // 게임방 입장

			if (gameRoom.getMembers().size() == totalParticipantCnt - 1) { // 인원 수가 모두 충족되었다면
				gameRoom.updateGameRoomState("진행");
				gameMatchingManager.completeMatching(gameRoom.getTitle(),
					new CompleteMatchingMessageRequest(gameRoom.getTotalParticipantCnt()), member.getNickname());
			}
		} else { // 입장 가능한 게임방이 없다면
			// 새로운 게임방 생성
			String title = "경쟁전 " + (gameRoomRepository.findAll().size() + 1);
			gameRoom = GameRoom.createGameRoom(title, null, totalParticipantCnt, Boolean.FALSE, "경쟁", null);
			gameRoomRepository.save(gameRoom);

			// 방에 입장
			member.enterGameRoom(gameRoom);
		}

		// 게임방에 처음 들어가는 사용자인가
		Boolean isFirstParticipant = Boolean.FALSE;
		if (gameRoom.getMembers() == null || gameRoom.getMembers().isEmpty())
			isFirstParticipant = Boolean.TRUE;
		return StartCompetitionMatchingResponse.startCompetitionMatchingResponse(gameRoom, isFirstParticipant); // 응답
	}

	@Override
	public QuitMatchingResponse quitMatching(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		GameRoom gameRoom = member.getGameRoom();
		member.leaveGameRoom();
		return QuitMatchingResponse.quitMatchingResponse(gameRoom);
	}
}
