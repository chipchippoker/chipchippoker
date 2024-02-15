package com.chipchippoker.backend.websocket.spectation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.manager.MapManager;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.spectation.dto.EnterSpectatorRequest;
import com.chipchippoker.backend.websocket.spectation.dto.EnterSpectatorResponse;
import com.chipchippoker.backend.websocket.spectation.dto.ExitSpectatorResponse;
import com.chipchippoker.backend.websocket.spectation.model.SpectationManager;
import com.chipchippoker.backend.websocket.spectation.service.SpectationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SpectationController {
	private final JwtUtil jwtUtil;
	private final SimpMessagingTemplate template;
	private final MapManager mapManager;
	private final SpectationService spectationService;

	@MessageMapping("/spectation/enter/{gameRoomTitle}")
	public void enterSpectationRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		EnterSpectatorRequest enterSpectatorRequest
	) {
		log.info("관전방 입장 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		SpectationManager spectationManager = mapManager.getSpectationManagerMap().get(gameRoomTitle);
		if (spectationManager == null) {
			spectationManager = new SpectationManager(gameRoomTitle);
			mapManager.getSpectationManagerMap().put(gameRoomTitle, spectationManager);
		}
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		spectationManager.insertMember(nickname);
		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_NEW_SPECTATOR_ENTER,
			EnterSpectatorResponse.create(gameManager, spectationManager));

		broadcastAllSpectatorInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_NEW_SPECTATOR_ENTER,
			EnterSpectatorResponse.create(gameManager, spectationManager));
		log.info(enterSpectatorRequest.getGameState());

		if (enterSpectatorRequest.getGameState().equals("진행")) {
			log.info("진행중인 게임에 입장");
			// 들어온 사람한테만 전달해줌
			broadcastToMember(nickname, spectationService.gameInfoToSpectator(gameManager, spectationManager));
		}
		log.info("관전방 입장 성공");
	}

	@MessageMapping("/spectation/exit/{gameRoomTitle}")
	public void exitSpectationRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle
	) {
		log.info("관전방 나가기 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		SpectationManager spectationManager = mapManager.getSpectationManagerMap().get(gameRoomTitle);
		spectationManager.deleteMember(nickname);
		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_SPECTATOR_EXIT,
			ExitSpectatorResponse.create(spectationManager.getSpectatorList()));

		broadcastAllSpectatorInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_SPECTATOR_EXIT,
			ExitSpectatorResponse.create(spectationManager.getSpectatorList()));
		log.info("관전방 나가기 성공");
	}

	private void broadcastAllMemberInfoInReadyRoom(String gameRoomTitle, MessageBase messageBase, Object object) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(messageBase, object)
		));
	}

	private void broadcastAllSpectatorInReadyRoom(String gameRoomTitle, MessageBase messageBase, Object object) {
		broadcastAllSpectatorConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(messageBase, object)
		));
	}

	public void broadcastAllConnected(String gameRoomTitle, Object object) {
		log.info("방에 있는 모든 사람들에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/checkConnect/".concat(gameRoomTitle), object);
		log.info("방에 있는 모든 사람들에게 메시지 전달 완료");
	}

	public void broadcastAllSpectatorConnected(String gameRoomTitle, Object object) {
		log.info("방에 있는 모든 관전자들에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/spectation/checkConnect/".concat(gameRoomTitle), object);
		log.info("방에 있는 모든 관전자들에게 메시지 전달 완료");
	}

	private void broadcastToMember(String nickname, Object object) {
		log.info("개인에게 메시지 전송");
		template.convertAndSend("/from/chipchippoker/member/".concat(nickname), object);
	}
}
