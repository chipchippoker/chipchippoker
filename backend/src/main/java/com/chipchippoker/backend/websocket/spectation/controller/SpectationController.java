package com.chipchippoker.backend.websocket.spectation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.websocket.spectation.dto.EnterSpectatorResponse;
import com.chipchippoker.backend.websocket.spectation.model.SpectationManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SpectationController {
	private final JwtUtil jwtUtil;
	private final SimpMessagingTemplate template;

	public static Map<String, SpectationManager> spectationManagerMap;

	@PostConstruct
	public void init() {
		spectationManagerMap = new HashMap<>();
	}

	@MessageMapping("/spectataion/enter/{gameRoomTitle}")
	public void enterSpectationRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle
	) {
		log.info("관전방 입장 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		SpectationManager spectationManager = spectationManagerMap.get(gameRoomTitle);
		if (spectationManager == null) {
			spectationManager = new SpectationManager(gameRoomTitle);
		}
		spectationManager.insertMember(nickname);
		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_NEW_SPECTATOR_ENTER,
			EnterSpectatorResponse.create(nickname));

		broadcastAllSpectatorInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_NEW_SPECTATOR_ENTER,
			EnterSpectatorResponse.create(nickname));
		log.info("관전방 입장 성공");
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
}
