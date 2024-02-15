package com.chipchippoker.backend.api.spectateroom.service;

import com.chipchippoker.backend.api.spectateroom.model.dto.EnterSpectateRoomResponse;

public interface SpectateRoomService {
	EnterSpectateRoomResponse enterSpectateRoom(Long id, String title, Integer password);

	void leaveSpectateRoom(Long id);
}
