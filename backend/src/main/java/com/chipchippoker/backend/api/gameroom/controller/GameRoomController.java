package com.chipchippoker.backend.api.gameroom.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.GetGameRoomListResponse;
import com.chipchippoker.backend.api.gameroom.service.GameRoomServiceImpl;
import com.chipchippoker.backend.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class GameRoomController {
	private final GameRoomServiceImpl gameRoomService;

	// 방 생성
	@PostMapping("/api/rooms")
	public ResponseEntity<ApiResponse<CreateGameRoomResponse>> createGameRoom(
		@RequestBody CreateGameRoomRequest createGameRoomRequest) {
		CreateGameRoomResponse createGameRoomResponse = gameRoomService.createGameRoom(createGameRoomRequest);
		return ResponseEntity.ok(ApiResponse.success(createGameRoomResponse));
	}

	// 방 입장
	@PostMapping("/api/rooms/enter")
	public ResponseEntity<ApiResponse<Void>> enterGameRoom(@RequestBody EnterGameRoomRequest enterGameRoomRequest) {
		gameRoomService.enterGameRoom(enterGameRoomRequest);
		return ResponseEntity.ok(ApiResponse.success());
	}

	// 방 목록
	@GetMapping("/api/rooms")
	public ResponseEntity<ApiResponse<Page<GetGameRoomListResponse>>> getGameRoomList(
		@RequestParam(value = "type") String type,
		@RequestParam(value = "title") String title,
		@RequestParam(value = "isTwo") Boolean isTwo, @RequestParam(value = "isThree") Boolean isThree,
		@RequestParam(value = "isFour") Boolean isFour, @RequestParam(value = "isEmpty") Boolean isEmpty,
		Pageable pageable) {
		Page<GetGameRoomListResponse> getGameRoomListResponse = gameRoomService.getGameRoomList(type, title, isTwo,
			isThree, isFour,
			isEmpty, pageable);
		return ResponseEntity.ok(ApiResponse.success(getGameRoomListResponse));
	}
}
