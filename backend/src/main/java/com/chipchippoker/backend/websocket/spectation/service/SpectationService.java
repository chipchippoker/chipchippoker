package com.chipchippoker.backend.websocket.spectation.service;

import org.springframework.http.ResponseEntity;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.spectation.dto.GameRoomForSpectatorMessageResponse;

public interface SpectationService {

	ResponseEntity<ApiResponse<GameRoomForSpectatorMessageResponse>> gameInfoToSpectator(GameManager gameManager);

	void deliveryGameInfoForSpectator(String gameRoomTitle, GameManager gameManager);
}
