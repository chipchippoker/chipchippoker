package com.chipchippoker.backend.websocket.game.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.websocket.game.dto.BettingMessage;
import com.chipchippoker.backend.websocket.game.dto.CreateGameRoomMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.GameReddyMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.GameRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.ReddyRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberInfo;
import com.chipchippoker.backend.websocket.game.model.MemberManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
	private final SimpMessagingTemplate template;

	private static Map<String, GameManager> gameManagerMap;

	@PostConstruct
	public void init() {
		gameManagerMap = new HashMap<>();
	}

	/**
	 * 게임방 생성 REST API에서 성공 응답이 오면 WEB SOCKET API를 호출하여 실시간 통신을 준비한다.
	 */
	@MessageMapping("/game/create/{gameRoomTitle}")
	public void createGameRoom(@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		CreateGameRoomMessageRequest createGameRoomMessageRequest) {
		log.info("게임방 생성 시작");

		gameManagerMap.put(gameRoomTitle,
			new GameManager(gameRoomTitle, createGameRoomMessageRequest.getCountOfPeople(), "hwan"));
		insertMember(gameRoomTitle, "hwan");
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);

		ReddyRoomMessageResponse reddyRoomMessageResponse = ReddyRoomMessageResponse.create(gameManager);
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(ApiResponse.success(reddyRoomMessageResponse)));
		log.info("게임방 생성 완료");
	}

	/**
	 * 새로운 유저가 게임방(대기방)에 입장하면 모든 사람에게 알린다.
	 */
	@MessageMapping("/game/enter/{gameRoomTitle}")
	public void enterGameRoom(@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle, String nickname) {
		log.info("게임방 입장 시작");
		insertMember(gameRoomTitle, nickname);
		broadcastAllMemberInfoInReddyRoom(gameRoomTitle);
		log.info("게임방 입장 성공");
	}

	/**
	 * 게임방(대기방)에서 나가면 모든 사람에게 나갔다고 알린다.
	 */
	@MessageMapping("/game/exit/{gameRoomTitle}")
	public void exitGameRoom(@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle, String nickname) {
		log.info("게임방 퇴장 시작");
		deleteMember(gameRoomTitle, nickname);
		broadcastAllMemberInfoInReddyRoom(gameRoomTitle);
		log.info("게임방 퇴장 성공");
	}

	/**
	 * 게임방(대기방)에서 회원이 레디를 하면 모든 사람에게 알린다.
	 */
	@MessageMapping("/game/reddy/{gameRoomTitle}")
	public void reddyGameRoom(@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		GameReddyMessageRequest gameReddyMessageRequest) {
		log.info("게임방 레디 시작");
		log.info(String.valueOf(gameReddyMessageRequest.getIsReddy()));
		gameManagerMap.get(gameRoomTitle)
			.getMemberManagerMap()
			.get("hwan")
			.getMemberInfo()
			.setIsReady(gameReddyMessageRequest.getIsReddy());
		broadcastAllMemberInfoInReddyRoom(gameRoomTitle);
		log.info("게임방 레디 완료");
	}

	/**
	 * 방장이 게임 시작을 누르면 게임방(대기방)의 모든 사람에게 알린다. 메시지를 받은 모든 사람은 게임방(플레이)로 이동한다.
	 */
	@MessageMapping("/game/start/{gameRoomTitle}")
	public void startGameRoom(@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle) {
		log.info("게임방 게임 시작");
		gameManagerMap.get(gameRoomTitle).setGameState("진행");
		gameManagerMap.get(gameRoomTitle)
			.getOrder()
			.addAll(gameManagerMap.get(gameRoomTitle).getMemberManagerMap().keySet().stream().toList());
		broadcastAllMemberInfoInGameRoom(gameRoomTitle);
		log.info("게임방 게임 시작 완료");
	}

	/**
	 * 베팅과 관련된 로직
	 */
	@MessageMapping("/game/betting/{gameRoomTitle}")
	public void bettingGameRoom(@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		BettingMessage bettingMessage) {
		/*
		검증 내역
		- 현재 라운드가 진행 라운드와 동일한지
		1. 아니다 -> 불가능
		- 해당 유저의 턴이 맞는지
		1. 현재 다이 상태이다 -> 불가능
		2. 현재 차례가 아니다 -> 불가능
		------
		해당 유저의 턴이 맞다면 해당 유저가 베팅할 수 있는 코인을 베팅한 것이 맞는지?
		1. 해당 유저의 코인보다 많다 -> 불가능
		2. 베팅에 참여중인 사용자 중 가장 적은 토큰의 수보다 많은 토큰을 베팅하는 경우 -> 불가능

		배팅을 했다면 다음 차례에 배팅 차례를 넘겨준다.
		 */
		log.info("베팅 로직 시작");

		// 게임 액션
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		MemberManager memberManager = gameManager.getMemberManagerMap().get("hwan");
		if (bettingMessage.getAction().equals("RUN")) {
			memberManager.setIsState("RUN");
			memberManager.setHaveCoin(memberManager.getHaveCoin() - bettingMessage.getBettingCoin());
		} else if (bettingMessage.getAction().equals("CALL")) {
			memberManager.setIsState("CALL");
			memberManager.setHaveCoin(memberManager.getHaveCoin() - bettingMessage.getBettingCoin());
		} else {
			memberManager.setIsState("DIE");
		}

		// 다음 차레로
		gameManager.setIsDone(gameManager.getIsDone() + 1);
		gameManager.nextTurn();

		if (Objects.equals(gameManager.getIsDone(), gameManager.getCountOfPeople())) {

		}
		log.info("베팅 로직 완료");
	}

	/*
	대기방에 플레이어를 추가하는 메서드
	 */
	public void insertMember(String gameRoomTitle, String nickname) {
		gameManagerMap.get(gameRoomTitle)
			.getMemberManagerMap()
			.put(nickname, MemberManager.create(MemberInfo.create(nickname)));
	}

	/*
	대기방에 플레이어를 제외하는 메서드
	 */
	public void deleteMember(String gameRoomTitle, String nickname) {
		gameManagerMap.get(gameRoomTitle).getMemberManagerMap().remove(nickname);
	}

	/*
	방에 있는 모든 사람에게 메시지를 전달하는 메서드
	 */

	public void broadcastAllConnected(String gameRoomTitle, Object object) {
		log.info("방에 있는 모든 사람에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/checkConnect/".concat(gameRoomTitle), object);
		log.info("방에 있는 모든 사람들에게 메시지 전달 완료");
	}

	/*
	대기방에 있는 모든 사람에게 대기방의 상태를 전달하는 메서드
	 */

	private void broadcastAllMemberInfoInReddyRoom(String gameRoomTitle) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.success(ReddyRoomMessageResponse.create(gameManagerMap.get(gameRoomTitle)))));
	}

	/*
	진행중인 게임방에 있는 모든 사람에게 게임방의 상태를 전달하는 메서드
	 */
	private void broadcastAllMemberInfoInGameRoom(String gameRoomTitle) {
		broadcastAllConnected(gameRoomTitle,
			ResponseEntity.ok(ApiResponse.success(GameRoomMessageResponse.create(gameManagerMap.get(gameRoomTitle)))));
	}
}
