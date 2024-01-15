package com.chipchippoker.backend.api.gameroom.service;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomRequest;

public interface GameRoomService {
	CreateGameRoomResponse createGameRoom(CreateGameRoomRequest createGameRoomRequest);

	void enterGameRoom(EnterGameRoomRequest enterGameRoomRequest);
}
