package com.chipchippoker.backend.websocket.game.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.gameresult.service.GameResultService;
import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.membergameroomblacklist.respository.MemberGameRoomBlackListRepository;
import com.chipchippoker.backend.api.point.service.PointService;
import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.websocket.game.dto.BanMemberMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.BettingMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.CreateGameRoomMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.GameReadyMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.GameRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.NormalGameEndMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.RankGameEndMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.ReadyRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
	private final GameRoomRepository gameRoomRepository;
	private final MemberGameRoomBlackListRepository memberGameRoomBlackListRepository;
	private final PointService pointService;
	private final GameResultService gameResultService;
	private final JwtUtil jwtUtil;
	private final SimpMessagingTemplate template;

	public static Map<String, GameManager> gameManagerMap;

	@PostConstruct
	public void init() {
		gameManagerMap = new HashMap<>();
	}

	/**
	 * 게임방 생성 REST API에서 성공 응답이 오면 WEB SOCKET API를 호출하여 실시간 통신을 준비한다.
	 * 게임방(대기방)을 생성하고 메시지를 반환한다.
	 */
	@MessageMapping("/game/create/{gameRoomTitle}")
	public void createGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		CreateGameRoomMessageRequest createGameRoomMessageRequest
	) {
		log.info("게임방 생성 시작");
		log.info("닉네임 찍어보기");
		String nickname = jwtUtil.getNickname(accessToken);

		makeGameManager(gameRoomTitle,
			createGameRoomMessageRequest.getCountOfPeople(),
			nickname);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		gameManager.insertMember(nickname, Boolean.TRUE);

		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_CREATED,
			ReadyRoomMessageResponse.create(gameManager));
		log.info("게임방 생성 완료");
	}

	/**
	 * 새로운 유저가 게임방(대기방)에 입장하면 모든 사람에게 알린다.
	 */
	@MessageMapping("/game/enter/{gameRoomTitle}")
	public void enterGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle
	) {
		log.info("게임방 입장 시작");
		// todo 블랙리스트라면 들어가지 못하게 만들기
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		gameManager.insertMember(nickname, Boolean.FALSE);
		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_NEW_MEMBER_ENTER,
			ReadyRoomMessageResponse.create(gameManager));
		log.info("게임방 입장 성공");
	}

	/**
	 * 게임방(대기방)에서 나가면 모든 사람에게 나갔다고 알린다.
	 */
	@MessageMapping("/game/exit/{gameRoomTitle}")
	public void exitGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle
	) {
		log.info("게임방 퇴장 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		gameManager.deleteMember(nickname);
		// 게임방 가져오기 REST API 연결시
		// GameRoom gameRoom = gameRoomRepository.findByTitle(gameRoomTitle)
		// 	.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS));
		// String roomManagerNickname = gameRoom.getRoomManagerNickname();
		// gameManager.setRoomManager(roomManagerNickname);
		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_MEMBER_EXIT,
			ReadyRoomMessageResponse.create(gameManager));
		log.info("게임방 퇴장 성공");
	}

	@MessageMapping("/game/ban/{gameRoomTitle}")
	public void banGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		BanMemberMessageRequest banMemberMessageRequest
	) {
		log.info("게임방 강퇴 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		String roomManager = gameManager.getRoomManager();
		if (nickname.equals(roomManager)) {
			gameManager.banMember(banMemberMessageRequest.getNickname());
			broadcastToMember(banMemberMessageRequest.getNickname(),
				ResponseEntity.ok(ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_BANED_MEMBER)));
			broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_BAN_MEMBER,
				ReadyRoomMessageResponse.create(gameManager));
		} else {
			broadcastToMember(nickname, ResponseEntity.badRequest().body(ApiResponse.messageError(
				MessageBase.E400_CAN_NOT_BAN)));
		}
	}

	/**
	 * 게임방(대기방)에서 회원이 레디를 하면 모든 사람에게 알린다.
	 */
	@MessageMapping("/game/ready/{gameRoomTitle}")
	public void readyGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		GameReadyMessageRequest gameReadyMessageRequest
	) {
		log.info("게임방 레디 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		gameManager.playReady(nickname, gameReadyMessageRequest.getIsReady());
		broadcastAllMemberInfoInReadyRoom(gameRoomTitle, MessageBase.S200_GAME_ROOM_MEMBER_READY,
			ReadyRoomMessageResponse.create(gameManager));
		log.info("게임방 레디 완료");
	}

	/**
	 * 방장이 게임 시작을 누르면 게임방(대기방)의 모든 사람에게 알린다. 메시지를 받은 모든 사람은 게임방(플레이)로 이동한다.
	 */
	@MessageMapping("/game/start/{gameRoomTitle}")
	public void startGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle
	) {
		log.info("게임방 게임 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		if (gameManager.getRoomManager().equals(nickname)) {
			try {
				// 게임시작
				gameManager.gameStart();
				deliveryAnotherMessage(gameRoomTitle, gameManager);
				log.info("게임방 게임 시작 성공");
			} catch (InvalidException e) {
				// 모두 준비완료 상태가 아니라 시작 불가
				// 이미 진행중인 게임방이라 시작 불가
				broadcastToMember(gameManager.getRoomManager(),
					ResponseEntity.badRequest()
						.body(ApiResponse.messageError(e.getMessageBase())));
				log.info(e.getMessage());
			}
		} else {
			broadcastToMember(nickname,
				ResponseEntity.badRequest()
					.body(ApiResponse.messageError(MessageBase.E400_CAN_NOT_START_NOT_ROOM_MANAGER)));
			log.info("게임방 게임 시작 실패(NOT MANAGER)");
		}
	}

	/**
	 * 베팅과 관련된 로직
	 */
	@MessageMapping("/game/betting/{gameRoomTitle}")
	public void bettingGameRoom(
		@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		BettingMessageRequest bettingMessageRequest
	) {
		/*
		검증 내역
		- 현재 라운드가 진행 라운드와 동일한지
		1. 아니다 -> 불가능 ( DONE )
		- 해당 유저의 턴이 맞는지
		1. 현재 다이 상태이다 -> 불가능 ( DONE )
		2. 현재 차례가 아니다 -> 불가능 ( DONE )
		------
		해당 유저의 턴이 맞다면 해당 유저가 베팅할 수 있는 코인을 베팅한 것이 맞는지?
		1. 해당 유저의 코인보다 많다 -> 불가능 ( DONE )
		2. 0 이하의 코인을 베팅하려고 한다. -> 불가능 ( DONE )
		2. 최소 배팅 미만이거나 최대 배팅 초과인 경우 -> 불가능

		배팅을 했다면 다음 차례에 배팅 차례를 넘겨준다.
		 */
		log.info("베팅 로직 시작");
		String nickname = jwtUtil.getNickname(accessToken);

		// 게임 액션
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		MemberManager memberManager = gameManager.getMemberManagerMap().get(nickname);
		Collection<MemberManager> memberManagers = gameManager.getMemberManagerMap().values();

		// 진행 라운드와 게임의 현재 라운드가 다른 경우
		if (!gameManager.getCurrentRound().equals(bettingMessageRequest.getCurrentRound())) {
			broadcastToMember(nickname, ResponseEntity.badRequest().body(ApiResponse.messageError(
				MessageBase.E400_CAN_NOT_BET_ROUND_MISMATCH,
				"현재 라운드는 ".concat(String.valueOf(gameManager.getCurrentRound()).concat("입니다."))
			)));
			return;
		}

		if (!nickname.equals(gameManager.getOrder().get(gameManager.getTurnNumber()))) {
			// 니 차례가 아닙니다.
			broadcastToMember(nickname, ResponseEntity.badRequest().body(ApiResponse.messageError(
				MessageBase.E400_CAN_NOT_BET_TURN_MISMATCH,
				gameManager.getOrder().get(gameManager.getTurnNumber()).concat("의 차례입니다."))));
			return;
		}

		// 배팅 내용 확인하기
		try {
			gameManager.betting(bettingMessageRequest, memberManager);
		} catch (InvalidException e) {
			log.info("베팅이 불가능합니다.");
			broadcastToMember(nickname, ResponseEntity.badRequest().body(ApiResponse.error(e.getErrorBase())));
			return;
		}

		// 라운드 종료인지 확인
		if (gameManager.checkDone()) {
			// 라운드종료
			gameManager.roundEnd();
			broadcastAllConnected(gameRoomTitle,
				ResponseEntity.ok(ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_ROUND_END,
					GameRoomMessageResponse.roundEnd(gameManager,
						memberManagers))));
			try {
				// 새로운 라운드 세팅
				gameManager.newRoundSetting(bettingMessageRequest.getCurrentRound());
			} catch (RuntimeException e) {
				// 게임 전체 종료
				// 라운드 종료 메시지는 이미 보내짐.
				// 아래에는 게임 전체종료 메시지를 내려준다.
				/*
				1. 유저의 닉네임
				2. 승/무/패 여부
				3. 포인트 변동
				 */

				GameRoom gameRoom = gameRoomRepository.findByTitleAndState(gameRoomTitle);
				if (gameRoom.getType().equals("경쟁")) {
					// 1. Point 저장
					for (MemberManager manager : memberManagers) {
						pointService.saveGameResult(manager.getMemberInfo().getNickname(),
							manager.getMemberGameInfo().getHaveCoin(), gameRoom.getType());
					}
					// 2. GameResult 저장
					saveGameResult(memberManagers, gameRoom);
					// 3. 게임방 상태 변경
					gameRoom.updateGameRoomState("종료");
					gameRoomRepository.save(gameRoom);

					// 3. 게임종료 메시지 출력
					broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
						ApiResponse.messageSuccess(
							MessageBase.S200_GAME_ROOM_RANK_MATCH_END,
							RankGameEndMessageResponse.create(memberManagers)
						)
					));

					// 4. GameManager 제거
					gameManagerMap.remove(gameRoomTitle);
				} else if (gameRoom.getType().equals("친선")) {
					// 1. GameResult 저장
					saveGameResult(memberManagers, gameRoom);

					// 2. 게임방 상태 변경
					gameRoom.updateGameRoomState("대기");
					gameRoomRepository.save(gameRoom);

					// 3. 게임종료 메시지 출력
					broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
							ApiResponse.messageSuccess(
								MessageBase.S200_GAME_ROOM_NORMAL_MATCH_END,
								NormalGameEndMessageResponse.create(memberManagers)
							)
						)
					);

					// 4. GameManager 상태 변경
					gameManager.setGameState("대기");
				}
				return;
			}
			// 새로운 라운드 시작 메시지 출력
			deliveryAnotherMessage(gameRoomTitle, gameManager);
			log.info("새로운 라운드가 시작되었습니다.");
		} else {
			// 다음 차례로 넘어간다.
			gameManager.nextTurn();
			log.info("베팅 차례가 넘어갑니다.");
			// 각각 다른 메시지 내려주기
			deliveryAnotherMessage(gameRoomTitle, gameManager);
			log.info("새로운 배팅 차례입니다.");
		}
		log.info("베팅 로직 완료");
	}

	private void saveGameResult(Collection<MemberManager> memberManagers, GameRoom gameRoom) {
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

	private void deliveryAnotherMessage(String gameRoomTitle, GameManager gameManager) {
		Collection<MemberManager> values = gameManager.getMemberManagerMap().values();
		for (MemberManager manager : values) {
			broadcastAllMemberInfoInGameRoom(gameRoomTitle, manager.getMemberInfo().getNickname());
		}
	}

	/**
	 * 방에 있는 모든 사람에게 메시지를 전달하는 메서드
	 */
	public void broadcastAllConnected(String gameRoomTitle, Object object) {
		log.info("방에 있는 모든 사람에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/checkConnect/".concat(gameRoomTitle), object);
		log.info("방에 있는 모든 사람들에게 메시지 전달 완료");
	}

	private void broadcastToMember(String nickname, Object object) {
		log.info("개인에게 메시지 전송");
		template.convertAndSend("/from/chipchippoker/member/".concat(nickname), object);
	}

	/**
	 * 대기방에 있는 모든 사람에게 대기방의 상태를 전달하는 메서드
	 */
	private void broadcastAllMemberInfoInReadyRoom(String gameRoomTitle, MessageBase messageBase, Object object) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(messageBase, object)
		));
	}

	/**
	 * 진행중인 게임방에 있는 모든 사람에게 게임방의 상태를 전달하는 메서드
	 * 각각의 사람들이 받는 정보가 다르다
	 * 본인의 카드정보는 전달받지 못한다.
	 */
	private void broadcastAllMemberInfoInGameRoom(String gameRoomTitle, String nickname) {
		GameManager gameManager = gameManagerMap.get(gameRoomTitle);
		MemberManager isTurnMemberManager = gameManager.getMemberManagerMap().get(nickname);
		ArrayList<MemberManager> isNotTurnMemberManager = (ArrayList<MemberManager>)gameManager.getMemberManagerMap()
			.values()
			.stream()
			.filter(memberManager -> memberManager != isTurnMemberManager)
			.collect(Collectors.toList());

		broadcastToMember(nickname,
			ResponseEntity.ok(
				ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_IN_PLAY_INFO,
					GameRoomMessageResponse.createRoundProceed(gameManager,
						isNotTurnMemberManager,
						isTurnMemberManager))
			));
	}

	/**
	 * 게임방 & 대기방 생성
	 */
	static void makeGameManager(String gameRoomTitle,
		Integer countOfPeople,
		String nickname) {
		gameManagerMap.put(gameRoomTitle, new GameManager(gameRoomTitle, countOfPeople, nickname));
	}
}
