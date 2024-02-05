package com.chipchippoker.backend.websocket.game.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chipchippoker.backend.api.gameresult.service.GameResultService;
import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.websocket.game.dto.GameRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.ReadyRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {
	private final GameResultService gameResultService;

	@Override
	public ResponseEntity<ApiResponse<ReadyRoomMessageResponse>> AllMemberInfoInReadyRoom(MessageBase messageBase,
		GameManager gameManager) {
		return ResponseEntity.ok(
			ApiResponse.messageSuccess(messageBase, ReadyRoomMessageResponse.create(gameManager))
		);
	}

	@Override
	public ResponseEntity<ApiResponse<GameRoomMessageResponse>> AllMemberWithOutMeInfoInGameRoom(
		GameManager gameManager, String gameRoomTitle, String nickname) {
		MemberManager isTurnMemberManager = gameManager.getMemberManagerMap().get(nickname);
		ArrayList<MemberManager> isNotTurnMemberManager = (ArrayList<MemberManager>)gameManager.getMemberManagerMap()
			.values()
			.stream()
			.filter(memberManager -> memberManager != isTurnMemberManager)
			.collect(Collectors.toList());

		return ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_IN_PLAY_INFO,
				GameRoomMessageResponse.createRoundProceed(gameManager,
					isNotTurnMemberManager,
					isTurnMemberManager)
			));
	}

	@Override
	public void saveGameResult(Collection<MemberManager> memberManagers, GameRoom gameRoom) {
		gameResultService.saveGameResult(
			memberManagers.stream()
				.map(memberManager1 -> memberManager1.getMemberInfo().getNickname())
				.collect(
					Collectors.toList()),
			memberManagers.stream()
				.map(memberManager1 -> memberManager1.getMemberGameInfo().getHaveCoin())
				.collect(
					Collectors.toList()),
			gameRoom.getId(),
			gameRoom.getType()
		);
	}
}
