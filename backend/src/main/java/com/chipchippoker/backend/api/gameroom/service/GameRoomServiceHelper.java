package com.chipchippoker.backend.api.gameroom.service;

import java.util.Objects;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.membergameroomblacklist.respository.MemberGameRoomBlackListRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.MemberGameRoomBlackList;
import com.chipchippoker.backend.common.exception.DuplicateException;
import com.chipchippoker.backend.common.exception.ForbiddenException;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GameRoomServiceHelper {
	public static void isDuplicatedGameRoom(GameRoomRepository gameRoomRepository, String title) {
		if (gameRoomRepository.findByTitleAndState(title) != null) {
			throw new DuplicateException(ErrorBase.E409_DUPLICATE_ROOM_TITLE);
		}
	}

	public static void isExistGameRoom(GameRoom gameRoom) {
		if (gameRoom == null)
			throw new NotFoundException(ErrorBase.E404_NOT_EXISTS);
	}

	public static void isCorrectGameRoomPassword(Integer userPassword, Integer gameRoomPassword) {
		if (!Objects.equals(userPassword, gameRoomPassword)) {
			throw new ForbiddenException(ErrorBase.E403_INVALID_GAME_ROOM_PASSWORD);
		}
	}

	public static void isStartedGameRoom(String state) {
		if (Objects.equals(state, "진행") || Objects.equals(state, "종료")) {
			throw new ForbiddenException(ErrorBase.E403_ALREADY_STARTED_GAME_ROOM);
		}
	}

	public static void isFullGameRoom(GameRoom gameRoom) {
		if (gameRoom.getMembers().size() >= gameRoom.getTotalParticipantCnt()) {
			throw new ForbiddenException(ErrorBase.E403_ALREADY_FULL_GAME_ROOM);
		}
	}

	public static void isGameRoomManager(String requestMemberNickname, String gameRoomManagerNickname) {
		if (!requestMemberNickname.equals(gameRoomManagerNickname)) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_BAN_NOT_GAME_ROOM_MANAGER);
		}
	}

	public static void isBlackListMember(MemberGameRoomBlackListRepository memberGameRoomBlackListRepository,
		Member member, GameRoom gameRoom) {
		MemberGameRoomBlackList memberGameRoomBlackList = memberGameRoomBlackListRepository.findByMemberAndGameRoom(
			member, gameRoom);
		if (memberGameRoomBlackList != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN);
		}
	}
}
