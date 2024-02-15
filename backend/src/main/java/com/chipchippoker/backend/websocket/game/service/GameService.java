package com.chipchippoker.backend.websocket.game.service;

import java.util.Collection;

import org.springframework.http.ResponseEntity;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.websocket.game.dto.GameRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.MemberAndSpectatorInfoMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.ReadyRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberManager;
import com.chipchippoker.backend.websocket.spectation.model.SpectationManager;

public interface GameService {
	/**
	 * 대기방에 있는 모든 사람에게 대기방의 상태를 전달하는 메서드
	 */
	ResponseEntity<ApiResponse<ReadyRoomMessageResponse>> AllMemberInfoInReadyRoom(MessageBase messageBase,
		GameManager gameManager);

	ResponseEntity<ApiResponse<MemberAndSpectatorInfoMessageResponse>> allMemberAndSpectatorInfoInReadyRoom(
		MessageBase messageBase,
		GameManager gameManager, SpectationManager spectationManager);

	/**
	 * 진행중인 게임방에 있는 모든 사람에게 게임방의 상태를 전달하는 메서드
	 * 각각의 사람들이 받는 정보가 다르다
	 * 본인의 카드정보는 전달받지 못한다.
	 */
	ResponseEntity<ApiResponse<GameRoomMessageResponse>> AllMemberWithOutMeInfoInGameRoom(GameManager gameManager,
		String gameRoomTitle, String nickname);

	void saveGameResult(Collection<MemberManager> memberManagers, GameRoom gameRoom);
}
