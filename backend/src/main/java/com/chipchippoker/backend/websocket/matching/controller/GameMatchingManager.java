package com.chipchippoker.backend.websocket.matching.controller;

import static com.chipchippoker.backend.websocket.game.controller.GameController.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.api.gameroom.service.GameRoomService;
import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.matching.dto.CompleteMatchingMessageRequest;
import com.chipchippoker.backend.websocket.matching.dto.CompleteMatchingMessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameMatchingManager {
	private final SimpMessagingTemplate template;
	private final GameRoomService gameRoomService;

	//경쟁전 매칭이 완료 되었을 때
	public void completeMatching(
		String gameRoomTitle,
		CompleteMatchingMessageRequest completeMatchingMessageRequest,
		String nickname
	) {
		log.info("경쟁전 매칭 완료");
		GameManager gameManager = new GameManager(gameRoomTitle,
			completeMatchingMessageRequest.getCountOfPeople(), null);
		// 게임방 제목으로 해당 게임방 멤버들의 닉네임을 가져와 게임 매니저에게 넣어준다.
		List<String> memberNicknames = gameRoomService.findMemberNicknames(gameRoomTitle);
		for (String memberNickname : memberNicknames) {
			gameManager.insertMember(memberNickname, Boolean.FALSE);
		}
		gameManager.insertMember(nickname, Boolean.FALSE);
		gameManagerMap.put(gameRoomTitle, gameManager);
		broadcastAllMemberInMainRoom(gameRoomTitle);
		log.info("경쟁전 게임 시작");
	}

	private void broadcastAllMemberInMainRoom(String gameRoomTitle) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.success(CompleteMatchingMessageResponse.create(gameManagerMap.get(gameRoomTitle)))));
	}

	public void broadcastAllConnected(String gameRoomTitle, Object object) {
		log.info("메인룸에 있는 모든 사람에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/checkConnect/".concat(gameRoomTitle), object);
		log.info("메인룸에 있는 모든 사람들에게 메시지 전달 완료");
	}
}
