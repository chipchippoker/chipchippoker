package com.chipchippoker.backend.api.matching.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.matching.model.dto.QuitMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartCompetitionMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingResponse;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.api.membergameroomblacklist.respository.MemberGameRoomBlackListRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.MemberGameRoomBlackList;
import com.chipchippoker.backend.common.exception.ForbiddenException;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl implements MatchingService {
	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;
	private final MemberGameRoomBlackListRepository memberGameRoomBlackListRepository;

	@Override
	public StartFriendlyMatchingResponse startFriendlyMatching(Long id, Integer totalParticipantCnt) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		if (member.getGameRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_GAME_ROOM);
		}

		// 친선, 공개, 대기, 총 인원수, 빈방을 만족하는 게임방 조회
		List<GameRoom> gameRoomList = gameRoomRepository.findByStartFriendlyMatchingSearchOption(totalParticipantCnt);

		if (!gameRoomList.isEmpty()) {
			for (GameRoom gameRoom : gameRoomList) {
				// 블랙리스트 사용자인가
				MemberGameRoomBlackList memberGameRoomBlackList = memberGameRoomBlackListRepository.findByMemberAndGameRoom(
					member, gameRoom);
				if (memberGameRoomBlackList == null) { // 해당 방의 블랙리스트에 등록되지 않는 사용자의 경우
					member.enterGameRoom(gameRoom);
					return StartFriendlyMatchingResponse.startFriendlyMatchingResponse(gameRoom,
						Boolean.FALSE); // 게임방에 처음 들어가는 사용자인가 (친선전 빠른 시작은 무조건 처음 들어가는 사용자가 아님)
				}
			}
		}
		// 블랙리스트에 등록되어 조회된 모든 방에 입장할 수 없는 사용자의 경우, 조회된 게임방이 없는 경우
		return null;
	}

	@Override
	public StartCompetitionMatchingResponse startCompetitionMatching(Long id, Integer totalParticipantCnt) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		if (member.getGameRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_GAME_ROOM);
		}

		// 경쟁, 대기, 총 인원수를 만족하는 게임방 조회
		GameRoom gameRoom = gameRoomRepository.findByStartCompetitionMatchingSearchOption(
			totalParticipantCnt);

		if (gameRoom != null) {
			member.enterGameRoom(gameRoom);

			if (gameRoom.getMembers().size() == totalParticipantCnt - 1) { // 인원 수가 모두 충족되었다면
				gameRoom.updateGameRoomState("진행");
			}
		} else { // 입장 가능한 게임방이 없다면
			String title = "경쟁전 " + (gameRoomRepository.findAll().size() + 1);
			gameRoom = GameRoom.createGameRoom(title, null, totalParticipantCnt, Boolean.FALSE, "경쟁", null);
			gameRoomRepository.save(gameRoom);

			member.enterGameRoom(gameRoom);
		}

		Boolean isFirstParticipant = Boolean.FALSE;
		if (gameRoom.getMembers() == null || gameRoom.getMembers().isEmpty())
			isFirstParticipant = Boolean.TRUE;
		return StartCompetitionMatchingResponse.startCompetitionMatchingResponse(gameRoom, isFirstParticipant);
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
