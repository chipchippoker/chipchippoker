package com.chipchippoker.backend.api.gameroom.service;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.DuplicateException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GameRoomServiceHelper {
	public static void isDuplicatedGameRoom(GameRoomRepository gameRoomRepository, String title) {
		if (gameRoomRepository.findByTitle(title).isPresent()) {
			throw new DuplicateException(ErrorBase.E409_DUPLICATE_ROOM_TITLE);
		}
	}
}
