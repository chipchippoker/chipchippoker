package com.chipchippoker.backend.websocket.spectation.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberManager;
import com.chipchippoker.backend.websocket.spectation.dto.GameRoomForSpectatorMessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpectationServiceImpl implements SpectationService {
	private final SimpMessagingTemplate template;

	@Override
	public ResponseEntity<ApiResponse<GameRoomForSpectatorMessageResponse>> gameInfoToSpectator(
		GameManager gameManager) {
		ArrayList<MemberManager> memberManagers = new ArrayList<>(gameManager.getMemberManagerMap().values());
		return ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_IN_PLAY_INFO,
				GameRoomForSpectatorMessageResponse.getCurrentState(gameManager, memberManagers
				)
			));
	}

	public void deliveryGameInfoForSpectator(String gameRoomTitle, GameManager gameManager) {
		broadcastAllSpectatorConnected(gameRoomTitle, gameInfoToSpectator(gameManager));
	}

	public void broadcastAllSpectatorConnected(String gameRoomTitle, Object object) {
		log.info("방에 있는 모든 관전자들에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/spectation/checkConnect/".concat(gameRoomTitle), object);
		log.info("방에 있는 모든 관전자들에게 메시지 전달 완료");
	}
}
