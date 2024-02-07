package com.chipchippoker.backend.common.dto;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum MessageBase {
	/**
	 * 200 OK (성공한 요청)
	 */
	S200(OK, false, "MS000", "성공"),
	S200_GAME_ROOM_CREATED(OK, false, "MS001", "게임방이 생성되었습니다."),
	S200_GAME_ROOM_NEW_MEMBER_ENTER(OK, false, "MS002", "새로운 멤버가 게임방에 입장했습니다."),
	S200_GAME_ROOM_MEMBER_EXIT(OK, false, "MS003", "회원이 게임방에서 나갔습니다."),
	S200_GAME_ROOM_BAN_MEMBER(OK, false, "MS004", "방장이 게임방에서 회원을 강퇴했습니다."),
	S200_GAME_ROOM_BANED_MEMBER(OK, false, "MS005", "방장이 나를 강퇴했습니다."),
	S200_GAME_ROOM_MEMBER_READY(OK, false, "MS006", "회원이 준비를 마쳤습니다."),
	S200_GAME_ROOM_IN_PLAY_INFO(OK, false, "MS007", "진행중인 게임의 메시지입니다."),
	S200_GAME_ROOM_ROUND_END(OK, false, "MS008", "라운드가 종료되었습니다."),
	S200_GAME_ROOM_RANK_MATCH_END(OK, false, "MS009", "경쟁 매치가 종료되었습니다."),
	S200_GAME_ROOM_NORMAL_MATCH_END(OK, false, "MS010", "친선 매치가 종료되었습니다."),
	S200_GAME_MATCHING(OK, false, "MS011", "게임 매칭 중입니다."),
	S200_FRIEND_REQUEST(OK, false, "MS012", "친구 요청을 받았습니다."),
	S200_GAME_ROOM_NEW_SPECTATOR_ENTER(OK, false, "MS013", "새로운 관전자가 방에 입장했습니다."),
	S200_GAME_ROOM_SPECTATOR_EXIT(OK, false, "MS014", "관전자가 게임방에서나갔습니다."),
	S200_GAME_ROOM_MANAGER_EXIT(OK, false, "MS015", "게임방 방장이 게임방에서 나갔습니다."),
	S200_GAME_ROOM_START(OK, false, "MS016", "게임방이 시작되었습니다."),
	S200_GAME_ROOM_NORMAL_MATCH_END_READY_ROOM(OK, false, "MS017", "친선 매치가 종료된 후 대기방의 상태입니다."),
	S200_GAME_ROOM_PENALTY_HAPPEN(OK, false, "MS018", "패널티를 받았습니다."),
	/**
	 * 400 Bad Request (잘못된 요청)
	 */
	E400_CAN_NOT_BAN(BAD_REQUEST, false, "MB001", "방장만 강제퇴장을 요청할 수 있습니다."),
	E400_CAN_NOT_START_NOT_READY(BAD_REQUEST, false, "MB002", "모두 준비상태가 아닙니다."),
	E400_CAN_NOT_START_NOT_ROOM_MANAGER(BAD_REQUEST, false, "MB003", "방장이 아니라 시작할 수 없습니다."),
	E400_CAN_NOT_BET_ROUND_MISMATCH(BAD_REQUEST, false, "MB004", "베팅 라운드가 잘못되었습니다."),
	E400_CAN_NOT_BET_TURN_MISMATCH(BAD_REQUEST, false, "MB005", "해당 멤버의 베팅 턴이 아닙니다."),
	E400_CAN_NOT_BET_BET_COIN_MISMATCH(BAD_REQUEST, false, "MB006", "베팅이 불가능 한 코인 개수입니다."),
	E400_CAN_NOT_START_ALREADY_START(BAD_REQUEST, false, "MB007", "이미 시작한 게임방입니다."),
	E400_CAN_NOT_START_ALONE(BAD_REQUEST, false, "MB008", "혼자서는 게임할 수 없습니다."),
	E400_CAN_NOT_BET_ALREADY_DIE(BAD_REQUEST, false, "MB009", "플레이어는 DIE 상태로 배팅에 참여할 수 없습니다."),

	/**
	 * 404 Not Found (존재하지 않는 리소스)
	 */
	E404_CAN_NOT_FIND_GAME_ROOM(NOT_FOUND, false, "MN001", "찾을 수 없는 방입니다.");

	private final HttpStatusCode statusCode;
	private final boolean sendNotification;
	private final String code;
	private final String message;

	MessageBase(HttpStatusCode statusCode, boolean sendNotification, String code, String message) {
		this.statusCode = statusCode;
		this.sendNotification = sendNotification;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return statusCode.value();
	}
}