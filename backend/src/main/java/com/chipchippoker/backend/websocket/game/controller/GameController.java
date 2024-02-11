package com.chipchippoker.backend.websocket.game.controller;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.point.service.PointService;
import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.exception.NotFoundException;
import com.chipchippoker.backend.common.manager.MapManager;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.websocket.game.dto.BanMemberMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.BettingMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.CreateGameRoomMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.GameReadyMessageRequest;
import com.chipchippoker.backend.websocket.game.dto.GameRoomMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.NormalGameEndMessageResponse;
import com.chipchippoker.backend.websocket.game.dto.RankGameEndMessageResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberManager;
import com.chipchippoker.backend.websocket.game.service.GameService;
import com.chipchippoker.backend.websocket.spectation.model.SpectationManager;
import com.chipchippoker.backend.websocket.spectation.service.SpectationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {
	private final GameRoomRepository gameRoomRepository;
	private final PointService pointService;
	private final JwtUtil jwtUtil;
	private final SimpMessagingTemplate template;
	private final MapManager mapManager;
	private final GameService gameService;
	private final SpectationService spectationService;

	/**
	 * 게임방 생성 REST API에서 성공 응답이 오면 WEB SOCKET API를 호출하여 실시간 통신을 준비한다.
	 * 게임방(대기방)을 생성하고 메시지를 반환한다.
	 */
	@MessageMapping("/game/create/{gameRoomTitle}")
	public void createGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		CreateGameRoomMessageRequest createGameRoomMessageRequest) {
		log.info("게임방 생성 시작");
		log.info("닉네임 찍어보기");
		String nickname = jwtUtil.getNickname(accessToken);

		makeGameManager(gameRoomTitle, createGameRoomMessageRequest, nickname);
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		gameManager.insertMember(nickname, Boolean.TRUE);

		broadcastAllConnected(gameRoomTitle,
			gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_CREATED, gameManager));
		log.info("게임방 생성 완료");
	}

	/**
	 * 새로운 유저가 게임방(대기방)에 입장하면 모든 사람에게 알린다.
	 */
	@MessageMapping("/game/enter/{gameRoomTitle}")
	public void enterGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle) {
		log.info("게임방 입장 시작");
		// todo 블랙리스트라면 들어가지 못하게 만들기
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		gameManager.insertMember(nickname, Boolean.FALSE);

		SpectationManager spectationManager = mapManager.getSpectationManagerMap().get(gameRoomTitle);
		if (spectationManager == null) {
			spectationManager = new SpectationManager(gameRoomTitle);
			mapManager.getSpectationManagerMap().put(gameRoomTitle, spectationManager);
		}

		broadcastAllConnected(gameRoomTitle,
			gameService.allMemberAndSpectatorInfoInReadyRoom(MessageBase.S200_GAME_ROOM_NEW_MEMBER_ENTER, gameManager,
				spectationManager));

		broadcastAllSpectatorConnected(gameRoomTitle,
			gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_NEW_MEMBER_ENTER, gameManager));
		log.info("게임방 입장 성공");
	}

	/**
	 * 게임방(대기방)에서 나가면 모든 사람에게 나갔다고 알린다.
	 */
	@MessageMapping("/game/exit/{gameRoomTitle}")
	public void exitGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle) {
		log.info("게임방 퇴장 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		Collection<MemberManager> memberManagers = gameManager.getMemberManagerMap().values();
		SpectationManager spectationManager = mapManager.getSpectationManagerMap().get(gameRoomTitle);
		if (spectationManager == null) {
			spectationManager = new SpectationManager(gameRoomTitle);
			mapManager.getSpectationManagerMap().put(gameRoomTitle, spectationManager);
		}
		try {
			// 게임방 가져오기 REST API 연결시
			GameRoom gameRoom = gameRoomRepository.findByTitleAndState(gameRoomTitle);
			if (gameRoom == null)
				throw new NotFoundException(MessageBase.E404_CAN_NOT_FIND_GAME_ROOM);

			// 방장이 마지막으로 나가서 게임방이 종료상태로 전환된 경우
			if (gameRoom.getState().equals("종료")) {
				broadcastAllSpectatorConnected(gameRoomTitle,
					gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_DISAPPEAR, gameManager));
				// 게임방 관리를 더 이상 하지 않는다.
				mapManager.getGameManagerMap().remove(gameRoomTitle);
				return;
			}

			// 게임 중간에 나가는 경우
			if (gameManager.getGameState().equals("진행")) {
				gameManager.deleteMemberInGameRoom(nickname);
				// 본인의 턴 중간에 나가는 경우
				/*
				게임에서 내보내고 다음턴으로 넘겨주어야 한다.
				1. 게임이 끝나는 경우
				2. 게임이 지속되는 경우
				 */
				/*
				본인의 턴이 아닌 데 나가는 경우
				1. 이미 베팅을 한 상태이다.

				2. 이미 다이를 한 상태이다.
				 */
				// 라운드가 종료되는 경우
				if (gameManager.checkRoundEnd()) {
					String winnerNickname = gameManager.roundEnd();
					// 라운드 종료 메시지 출력
					sendRoundEndMessage(gameRoomTitle, gameManager, memberManagers, winnerNickname);
					try {
						gameManager.newRoundSetting(gameManager.getCurrentRound());
					} catch (RuntimeException e) {
						// 1. Point 저장
						for (MemberManager manager : memberManagers) {
							pointService.saveGameResult(manager.getMemberInfo().getNickname(),
								manager.getMemberGameInfo().getHaveCoin(), gameRoom.getType());
						}
						// 2. GameResult 저장
						gameService.saveGameResult(memberManagers, gameRoom);
						if (gameRoom.getType().equals("경쟁")) {
							// 4. 랭크 게임종료 메시지 출력
							sendRandGameEndMessage(gameRoomTitle, memberManagers);
						} else if (gameRoom.getType().equals("친선")) {
							// 4. 친선 게임종료 메시지 출력
							sendNormalGameEndMessage(gameRoomTitle, memberManagers);
						}
						// 5. GameManager 제거
						mapManager.getGameManagerMap().remove(gameRoomTitle);
						return;
					}
					log.info("새로운 라운드가 시작되었습니다.");
				}
				// 라운드가 종료되지 않았고, 나간 사람의 턴이었다.
				// 라운드가 종료되지 않았고, 나간 사람의 턴이 아니었다.
				else if (gameManager.getOrder().get(gameManager.getTurnNumber()).equals(nickname)) {
					gameManager.nextTurn();
					log.info("새로운 배팅 차례입니다.");
				}
				deliveryAnotherMessage(gameRoomTitle, gameManager);
				spectationService.deliveryGameInfoForSpectator(gameRoomTitle, gameManager, spectationManager);
				gameManager.getOrder().remove(nickname);
				return;
			}

			// 나가는 사람이 방장인 경우
			if (nickname.equals(gameManager.getRoomManager())) {
				String roomManagerNickname = gameRoom.getRoomManagerNickname();
				gameManager.setRoomManager(roomManagerNickname);
				gameManager.deleteMember(nickname);

				broadcastAllConnected(gameRoomTitle,
					gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_MANAGER_EXIT, gameManager));

				broadcastAllSpectatorConnected(gameRoomTitle,
					gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_MANAGER_EXIT, gameManager));
			}
			// 방장이 아닌 경우
			else {
				gameManager.deleteMember(nickname);
				broadcastAllConnected(gameRoomTitle,
					gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_MEMBER_EXIT, gameManager));

				broadcastAllSpectatorConnected(gameRoomTitle,
					gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_MEMBER_EXIT, gameManager));
			}

		}
		// 방이 존재하지 않는 경우
		catch (NotFoundException e) {
			broadcastToMember(nickname, ResponseEntity.badRequest().body(ApiResponse.messageError(e.getMessageBase())));
			return;
		}
		log.info("게임방 퇴장 성공");
	}

	@MessageMapping("/game/ban/{gameRoomTitle}")
	public void banGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		BanMemberMessageRequest banMemberMessageRequest) {
		log.info("게임방 강퇴 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		String roomManager = gameManager.getRoomManager();
		if (nickname.equals(roomManager)) {
			gameManager.banMember(banMemberMessageRequest.getNickname());
			broadcastToMember(banMemberMessageRequest.getNickname(),
				ResponseEntity.ok(ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_BANED_MEMBER)));
			broadcastAllConnected(gameRoomTitle,
				gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_BAN_MEMBER, gameManager));
			broadcastAllSpectatorConnected(gameRoomTitle,
				gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_BAN_MEMBER, gameManager));
		} else {
			broadcastToMember(nickname,
				ResponseEntity.badRequest().body(ApiResponse.messageError(MessageBase.E400_CAN_NOT_BAN)));
		}
	}

	/**
	 * 게임방(대기방)에서 회원이 레디를 하면 모든 사람에게 알린다.
	 */
	@MessageMapping("/game/ready/{gameRoomTitle}")
	public void readyGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		GameReadyMessageRequest gameReadyMessageRequest) {
		log.info("게임방 레디 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		gameManager.playReady(nickname, gameReadyMessageRequest.getIsReady());
		broadcastAllConnected(gameRoomTitle,
			gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_MEMBER_READY, gameManager));
		broadcastAllSpectatorConnected(gameRoomTitle,
			gameService.AllMemberInfoInReadyRoom(MessageBase.S200_GAME_ROOM_MEMBER_READY, gameManager));
		log.info("게임방 레디 완료");
	}

	/**
	 * 방장이 게임 시작을 누르면 게임방(대기방)의 모든 사람에게 알린다. 메시지를 받은 모든 사람은 게임방(플레이)로 이동한다.
	 */
	@MessageMapping("/game/start/{gameRoomTitle}")
	public void startGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle) {
		log.info("게임방 게임 시작");
		String nickname = jwtUtil.getNickname(accessToken);
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		SpectationManager spectationManager = mapManager.getSpectationManagerMap().get(gameRoomTitle);
		if (spectationManager == null) {
			spectationManager = new SpectationManager(gameRoomTitle);
			mapManager.getSpectationManagerMap().put(gameRoomTitle, spectationManager);
		}
		if (gameManager.getRoomManager().equals(nickname)) {
			try {
				// 게임시작
				gameManager.gameStart();
				sendGameStartMessage(gameRoomTitle);
				deliveryAnotherMessage(gameRoomTitle, gameManager);
				spectationService.deliveryGameInfoForSpectator(gameRoomTitle, gameManager, spectationManager);
				log.info("게임방 게임 시작 성공");
			} catch (InvalidException e) {
				// 모두 준비완료 상태가 아니라 시작 불가
				// 이미 진행중인 게임방이라 시작 불가
				broadcastToMember(gameManager.getRoomManager(),
					ResponseEntity.badRequest().body(ApiResponse.messageError(e.getMessageBase())));
				log.info(e.getMessage());
			}
		} else {
			broadcastToMember(nickname, ResponseEntity.badRequest()
				.body(ApiResponse.messageError(MessageBase.E400_CAN_NOT_START_NOT_ROOM_MANAGER)));
			log.info("게임방 게임 시작 실패(NOT MANAGER)");
		}
	}

	/**
	 * 베팅과 관련된 로직
	 */
	@MessageMapping("/game/betting/{gameRoomTitle}")
	public void bettingGameRoom(@Header(name = "access-token") String accessToken,
		@DestinationVariable(value = "gameRoomTitle") String gameRoomTitle,
		BettingMessageRequest bettingMessageRequest) {
		log.info("베팅 로직 시작");
		String nickname = jwtUtil.getNickname(accessToken);

		// 게임 액션
		GameManager gameManager = mapManager.getGameManagerMap().get(gameRoomTitle);
		SpectationManager spectationManager = mapManager.getSpectationManagerMap().get(gameRoomTitle);
		if (spectationManager == null) {
			spectationManager = new SpectationManager(gameRoomTitle);
			mapManager.getSpectationManagerMap().put(gameRoomTitle, spectationManager);
		}
		MemberManager memberManager = gameManager.getMemberManagerMap().get(nickname);
		Collection<MemberManager> memberManagers = gameManager.getMemberManagerMap().values();

		if (!gameManager.getCurrentRound().equals(bettingMessageRequest.getCurrentRound())) {
			broadcastToMember(nickname, ResponseEntity.badRequest()
				.body(ApiResponse.messageError(MessageBase.E400_CAN_NOT_BET_ROUND_MISMATCH,
					"현재 라운드는 ".concat(String.valueOf(gameManager.getCurrentRound()).concat("입니다.")))));
			return;
		}

		if (!nickname.equals(gameManager.getOrder().get(gameManager.getTurnNumber()))) {
			broadcastToMember(nickname, ResponseEntity.badRequest()
				.body(ApiResponse.messageError(MessageBase.E400_CAN_NOT_BET_TURN_MISMATCH,
					gameManager.getOrder().get(gameManager.getTurnNumber()).concat("의 차례입니다."))));
			return;
		}

		// 배팅 내용 확인하기
		try {
			gameManager.betting(bettingMessageRequest, memberManager);
		} catch (InvalidException e) {
			log.info("베팅이 불가능합니다.");
			broadcastToMember(nickname, ResponseEntity.badRequest().body(ApiResponse.messageError(e.getMessageBase())));
			return;
		}

		if (gameManager.checkRoundEnd()) {
			String winnerNickname = gameManager.roundEnd();
			// 라운드 종료 메시지 출력
			sendRoundEndMessage(gameRoomTitle, gameManager, memberManagers, winnerNickname);
			try {
				gameManager.newRoundSetting(gameManager.getCurrentRound());
			} catch (RuntimeException e) {
				GameRoom gameRoom = gameRoomRepository.findByTitleAndState(gameRoomTitle);
				// 1. Point 저장
				for (MemberManager manager : memberManagers) {
					pointService.saveGameResult(manager.getMemberInfo().getNickname(),
						manager.getMemberGameInfo().getHaveCoin(), gameRoom.getType());
				}
				// 2. GameResult 저장
				gameService.saveGameResult(memberManagers, gameRoom);
				if (gameRoom.getType().equals("경쟁")) {
					// 4. 랭크 게임종료 메시지 출력
					sendRandGameEndMessage(gameRoomTitle, memberManagers);
				} else if (gameRoom.getType().equals("친선")) {
					// 4. 친선 게임종료 메시지 출력
					sendNormalGameEndMessage(gameRoomTitle, memberManagers);
				}
				// 5. GameManager 제거
				mapManager.getGameManagerMap().remove(gameRoomTitle);
				return;
			}
			log.info("새로운 라운드가 시작되었습니다.");
		} else {
			gameManager.nextTurn();
			log.info("새로운 배팅 차례입니다.");
		}
		deliveryAnotherMessage(gameRoomTitle, gameManager);
		spectationService.deliveryGameInfoForSpectator(gameRoomTitle, gameManager, spectationManager);
		log.info("베팅 로직 완료");
	}

	/**
	 * 게임이 시작되었음을 방에 있는 사람들에게 알리는 메서드
	 */
	private void sendGameStartMessage(String gameRoomTitle) {
		broadcastAllConnected(gameRoomTitle,
			ResponseEntity.ok(ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_START)));
		broadcastAllSpectatorConnected(gameRoomTitle,
			ResponseEntity.ok(ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_START)));
	}

	/**
	 * 랭크게임이 종료되었음을 방에 있는 사람들에게 알리는 메서드
	 */
	private void sendRoundEndMessage(String gameRoomTitle, GameManager gameManager,
		Collection<MemberManager> memberManagers,
		String winnerNickname) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_ROUND_END,
				GameRoomMessageResponse.roundEnd(gameManager, memberManagers, winnerNickname))));
		broadcastAllSpectatorConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_ROUND_END,
				GameRoomMessageResponse.roundEnd(gameManager, memberManagers, winnerNickname))));
	}

	private void sendRandGameEndMessage(String gameRoomTitle, Collection<MemberManager> memberManagers) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_RANK_MATCH_END,
				RankGameEndMessageResponse.create(memberManagers))));
		broadcastAllSpectatorConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_RANK_MATCH_END,
				RankGameEndMessageResponse.create(memberManagers))));
	}

	/**
	 * 친선게임이 종료되었음을 방에 있는 사람들에게 전달하는 메서드
	 */
	private void sendNormalGameEndMessage(String gameRoomTitle, Collection<MemberManager> memberManagers) {
		broadcastAllConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_NORMAL_MATCH_END,
				NormalGameEndMessageResponse.create(memberManagers))));
		broadcastAllSpectatorConnected(gameRoomTitle, ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_GAME_ROOM_NORMAL_MATCH_END,
				NormalGameEndMessageResponse.create(memberManagers))));
	}

	/**
	 * 본인의 카드정보를 제외한 나머지 상황을 각각에게 전달하는 메서드
	 */
	private void deliveryAnotherMessage(String gameRoomTitle, GameManager gameManager) {
		Collection<MemberManager> values = gameManager.getMemberManagerMap().values();
		for (MemberManager manager : values) {
			broadcastToMember(manager.getMemberInfo().getNickname(),
				gameService.AllMemberWithOutMeInfoInGameRoom(gameManager, gameRoomTitle,
					manager.getMemberInfo().getNickname()));
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

	/**
	 * 관전 중인 모든 사람에게 메시지를 전달하는 메서드
	 */
	public void broadcastAllSpectatorConnected(String gameRoomTitle, Object object) {
		log.info("방에 있는 모든 관전자들에게 메시지 전달 시작");
		template.convertAndSend("/from/chipchippoker/spectation/checkConnect/".concat(gameRoomTitle), object);
		log.info("방에 있는 모든 관전자들에게 메시지 전달 완료");
	}

	/**
	 * 방에 있는 개인에게 메시지를 전달하는 메서드
	 */
	private void broadcastToMember(String nickname, Object object) {
		log.info("개인에게 메시지 전송");
		template.convertAndSend("/from/chipchippoker/member/".concat(nickname), object);
	}

	/**
	 * 게임방 생성
	 */
	private void makeGameManager(String gameRoomTitle, CreateGameRoomMessageRequest createGameRoomMessageRequest,
		String nickname) {
		mapManager.getGameManagerMap()
			.put(gameRoomTitle,
				new GameManager(gameRoomTitle, createGameRoomMessageRequest.getCountOfPeople(), nickname));
	}
}
