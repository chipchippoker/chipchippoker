package com.chipchippoker.backend.api.gameroom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.GetGameRoomListResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.MemberOutGameRoomRequest;

public interface GameRoomService {
	CreateGameRoomResponse createGameRoom(CreateGameRoomRequest createGameRoomRequest, Long id);

	void enterGameRoom(EnterGameRoomRequest enterGameRoomRequest, Long id);

	Page<GetGameRoomListResponse> getGameRoomList(String type, String title, Boolean isTwo, Boolean isThree,
		Boolean isFour,
		Boolean isEmpty, Pageable pageable);

	void leaveGameRoom(String title, Long id);

	void playGameRoom(String title);

	void memberOutGameRoom(MemberOutGameRoomRequest memberOutGameRoomRequest, Long id);
}
