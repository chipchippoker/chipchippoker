package com.chipchippoker.backend.websocket.matching.controller;

import static com.chipchippoker.backend.websocket.game.controller.GameController.*;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.matching.dto.GameMatchingMessageRequest;
import com.chipchippoker.backend.websocket.matching.dto.GameMatchingMessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameMatchingController {
	private final SimpMessagingTemplate template;
	private final JwtUtil jwtUtil;

	//경쟁전 매칭이 되었을 때
	@MessageMapping("/game/matching/{gameRoomTitle}")
	public void gameMatching(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		GameMatchingMessageRequest gameMatchingMessageRequest
	) {
		log.info("매칭 시작");
		String nickname = jwtUtil.getNickName(accessToken, null);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		if (gameManager == null) {
			gameManager = new GameManager(gameRoomTitle, gameMatchingMessageRequest.getCountOfPeople(), null);
		}
		gameManager.insertMember(nickname, Boolean.FALSE);
		gameManagerMap.put(gameRoomTitle, gameManager);
		broadcastAllMemberInMainRoom(gameRoomTitle, MessageBase.S200_GAME_MATCHING,
			GameMatchingMessageResponse.create(gameManagerMap.get(gameRoomTitle)));
		log.info("매칭 로직 종료");
	}

	private void broadcastAllMemberInMainRoom(String gameRoomTitle, MessageBase messageBase, Object object) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(messageBase, object)));
	}

	public void broadcastAllConnected(String gameRoomTitle, Object object) {
		log.info("메인룸에 있는 모든 사람에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/checkConnect/".concat(gameRoomTitle), object);
		log.info("메인룸에 있는 모든 사람들에게 메시지 전달 완료");
	}
}
