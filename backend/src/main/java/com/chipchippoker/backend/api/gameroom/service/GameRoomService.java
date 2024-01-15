package com.chipchippoker.backend.api.gameroom.service;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;

public interface GameRoomService {
	CreateGameRoomResponse createGameRoom(CreateGameRoomRequest createGameRoomRequest);
}
